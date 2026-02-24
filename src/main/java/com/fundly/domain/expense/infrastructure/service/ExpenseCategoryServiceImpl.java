package com.fundly.domain.expense.infrastructure.service;

import com.fundly.common.exception.ExpenseCategoryNotFoundException;
import com.fundly.common.exception.ForbiddenOperationException;
import com.fundly.domain.auth.core.port.in.SecurityUserService;
import com.fundly.domain.expense.adapter.web.dto.CategoryWithExpensesResponse;
import com.fundly.domain.expense.adapter.web.dto.CreateExpenseCategoryRequest;
import com.fundly.domain.expense.adapter.web.dto.UpdateExpenseCategoryRequest;
import com.fundly.domain.expense.core.model.Expense;
import com.fundly.domain.expense.core.model.ExpenseCategory;
import com.fundly.domain.expense.core.port.in.ExpenseCategoryService;
import com.fundly.domain.expense.core.port.out.ExpenseCategoryRepository;
import com.fundly.domain.expense.core.port.out.ExpenseRepository;
import com.fundly.domain.expense.infrastructure.dto.ExpenseCategoryDTO;
import com.fundly.domain.expense.infrastructure.mapper.ExpenseCategoryMapper;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import com.fundly.domain.user.infrastructure.mapper.UserDtoToModelMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {
    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseCategoryMapper categoryMapper;
    private final UserDtoToModelMapper userDtoToModelMapper;
    private final SecurityUserService securityUserService;
    private final ExpenseRepository expenseRepository;

    public ExpenseCategoryServiceImpl(
            ExpenseCategoryRepository categoryRepository,
            ExpenseCategoryMapper categoryMapper,
            UserDtoToModelMapper userDtoToModelMapper,
            SecurityUserService securityUserService,
            ExpenseRepository expenseRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.userDtoToModelMapper = userDtoToModelMapper;
        this.securityUserService = securityUserService;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ExpenseCategoryDTO findById(UUID id) {
        try {
            ExpenseCategory savedCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new ExpenseCategoryNotFoundException("Expense category with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (!userDto.getId().equals(savedCategory.getUser().getId())) {
                log.warn("Failed attempt of user {} to view expense category {}.", userDto.getId(), id);
                throw new ForbiddenOperationException("You do not have permission to update the category " + savedCategory.getId());
            }

            ExpenseCategoryDTO categoryDto = categoryMapper.toDto(savedCategory);

            log.info("Successfully retrieved expense category with id {}.", id);

            return categoryDto;
        } catch (Exception ex) {
            log.warn("Failed to get expense category {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<ExpenseCategoryDTO> findAllForUser() {
        try {
            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            List<ExpenseCategory> savedCategories = categoryRepository.findByUserIdOrUserIdIsNull(userDto.getId());

            List<ExpenseCategoryDTO> categoriesDto = categoryMapper.toDtoList(savedCategories);

            log.info("Successfully retrieved expense categories for user with id {}.", userDto.getId());

            return categoriesDto;
        } catch (Exception ex) {
            log.warn("Failed to get all expense categories for logged in user. With exception: {}", ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public UUID create(CreateExpenseCategoryRequest createDto) {
        try {
            ExpenseCategory category = categoryMapper.fromCreateDtoToEntity(createDto);

            UserDTO userDto = securityUserService.getLoggedInUserInfo();
            category.setUser(userDtoToModelMapper.toEntity(userDto));

            ExpenseCategory savedCategory = categoryRepository.save(category);

            log.info("Successfully created expense category with id {}.", savedCategory.getId());

            return savedCategory.getId();
        } catch (Exception ex) {
            log.warn("Failed to create expense category {}. With exception: {}", createDto.getName(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public void update(UpdateExpenseCategoryRequest updateDto, UUID id) {
        try {
            ExpenseCategory savedCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new ExpenseCategoryNotFoundException("Expense category with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (!userDto.getId().equals(savedCategory.getUser().getId())) {
                log.warn("Failed attempt of user {} to update expense category {}.", userDto.getId(), id);
                throw new ForbiddenOperationException("You do not have permission to update the category " + savedCategory.getId());
            }

            categoryMapper.updateFromDto(updateDto, savedCategory);
            categoryRepository.save(savedCategory);

            log.info("Successfully updated expense category with id {}.", savedCategory.getId());
        } catch (Exception ex) {
            log.warn("Failed to update expense category {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public void delete(UUID id, boolean withExpenses) {
        try {
            ExpenseCategory savedCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new ExpenseCategoryNotFoundException("Expense category with id " + id + " not found."));

            if (savedCategory.getUser() == null)
                throw new ForbiddenOperationException("You can't delete the default category " + savedCategory.getId());

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (!userDto.getId().equals(savedCategory.getUser().getId())) {
                log.warn("Failed attempt of user {} to delete expense category {}.", userDto.getId(), id);
                throw new ForbiddenOperationException("You do not have permission to delete the category " + savedCategory.getId());
            }

            if (withExpenses) {
                this.deleteExpensesForCategory(id);
            } else {
                this.reassignExpensesToOtherCategory(id);
            }

            savedCategory.setDeleted(true);
            categoryRepository.save(savedCategory);

            log.info("Successfully deleted expense category with id {}.", savedCategory.getId());
        } catch (Exception ex) {
            log.warn("Failed to delete expense category {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    private void deleteExpensesForCategory(UUID categoryId) {
        try {
            List<Expense> expensesToUpdate = expenseRepository.findAllByCategoryId(categoryId);
            for (Expense expense : expensesToUpdate) {
                expense.setDeleted(true);
                expense.setExpenseCategory(null);
            }
            expenseRepository.saveAll(expensesToUpdate);

            log.info("Deleted {} expenses for category {}.", expensesToUpdate.size(), categoryId);
        } catch (Exception ex) {
            log.warn("Failed to delete expenses for category {}. With exception: {}", categoryId, ex.getMessage());
            throw ex;
        }
    }

    private void reassignExpensesToOtherCategory(UUID categoryId) {
        try {
            ExpenseCategory otherCategory = categoryRepository.findByNameAndUserIsNull("Other")
                    .orElseThrow(() -> new ExpenseCategoryNotFoundException("Category other not found."));

            List<Expense> expensesToUpdate = expenseRepository.findAllByCategoryId(categoryId);
            for (Expense expense : expensesToUpdate) {
                expense.setExpenseCategory(otherCategory);
            }
            expenseRepository.saveAll(expensesToUpdate);

            log.info("Reassigned {} expenses from category {} to 'Other' category.", expensesToUpdate.size(), categoryId);
        } catch (Exception ex) {
            log.warn("Failed to reassign expenses from category {} to 'Other'. With exception: {}", categoryId, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<CategoryWithExpensesResponse> findCategoriesWithExpensesForUser(UUID id) {
        List<ExpenseCategory> categoriesWithExpenses = categoryRepository.findAllWithExpensesForUser(id);

        return categoryMapper.toCategoryWithExpensesDtoList(categoriesWithExpenses);
    }
}