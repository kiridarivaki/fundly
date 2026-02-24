package com.fundly.domain.expense.infrastructure.service;

import com.fundly.common.exception.ExpenseCategoryNotFoundException;
import com.fundly.common.exception.ExpenseNotFoundException;
import com.fundly.common.exception.ForbiddenOperationException;
import com.fundly.domain.auth.core.port.in.SecurityUserService;
import com.fundly.domain.expense.adapter.web.dto.CreateExpenseRequest;
import com.fundly.domain.expense.adapter.web.dto.UpdateExpenseRequest;
import com.fundly.domain.expense.core.model.Expense;
import com.fundly.domain.expense.core.model.ExpenseCategory;
import com.fundly.domain.expense.core.port.in.ExpenseService;
import com.fundly.domain.expense.core.port.out.ExpenseCategoryRepository;
import com.fundly.domain.expense.core.port.out.ExpenseRepository;
import com.fundly.domain.expense.infrastructure.dto.ExpenseDTO;
import com.fundly.domain.expense.infrastructure.mapper.ExpenseMapper;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import com.fundly.domain.user.infrastructure.mapper.UserDtoToModelMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfNextMonth;

@Service
@Log4j2
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseMapper expenseMapper;
    private final SecurityUserService securityUserService;
    private final UserDtoToModelMapper userDtoToModelMapper;

    public ExpenseServiceImpl(
            ExpenseRepository expenseRepository,
            ExpenseCategoryRepository categoryRepository,
            SecurityUserService securityUserService,
            ExpenseMapper expenseMapper,
            UserDtoToModelMapper userDtoToModelMapper) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.securityUserService = securityUserService;
        this.expenseMapper = expenseMapper;
        this.userDtoToModelMapper = userDtoToModelMapper;
    }

    @Override
    public ExpenseDTO findById(UUID id) {
        try {
            Expense savedExpense = expenseRepository.findById(id)
                    .orElseThrow(() -> new ExpenseNotFoundException("Expense with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (!userDto.getId().equals(savedExpense.getUser().getId())) {
                log.warn("Failed attempt of user {} to view expense {}.", userDto.getId(), id);
                throw new ForbiddenOperationException("You do not have permission to view this resource.");
            }

            ExpenseDTO expenseDto = expenseMapper.toDto(savedExpense);

            log.info("Successfully retrieved expense with id {}.", id);

            return expenseDto;
        } catch (Exception ex) {
            log.warn("Failed to get expense {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Page<ExpenseDTO> findAllPaged(Pageable pageable, Optional<String> searchTerm) {
        try {
            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            String keyword = searchTerm
                    .filter(k -> !k.isBlank())
                    .map(String::toLowerCase)
                    .orElse(null);

            Page<ExpenseDTO> expensesPaged = expenseRepository.findAllByUserIdAndKeyword(
                    userDto.getId(),
                    keyword,
                    pageable
            ).map(expenseMapper::toDto);

            log.info("Successfully retrieved all expenses.");

            return expensesPaged;
        } catch (Exception ex) {
            log.warn("Failed to get expense history. With exception: {}", ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public UUID create(CreateExpenseRequest createDto) {
        try {
            if (createDto.getCategoryId() == null)
                throw new IllegalArgumentException("Expense must have a valid category ID.");

            Expense expense = expenseMapper.fromCreateDtoToEntity(createDto);

            ExpenseCategory expenseCategory = categoryRepository.findById(createDto.getCategoryId())
                    .orElseThrow(() -> new ExpenseCategoryNotFoundException("Expense category with id " + createDto.getCategoryId() + " not found."));

            expense.setExpenseCategory(expenseCategory);

            UserDTO userDto = securityUserService.getLoggedInUserInfo();
            expense.setUser((userDtoToModelMapper.toEntity(userDto)));

            Expense savedExpense = expenseRepository.save(expense);

            log.info("Successfully created expense with id {}.", savedExpense.getId());

            return savedExpense.getId();
        } catch (Exception ex) {
            log.warn("Failed to create expense {}. With exception: {}", createDto.getName(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public void update(UpdateExpenseRequest updateDto, UUID id) {
        try {
            Expense savedExpense = expenseRepository.findById(id)
                    .orElseThrow(() -> new ExpenseNotFoundException("Expense with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (userDto.getId().equals(savedExpense.getUser().getId())) {
                log.warn("Failed attempt of user {} to update expense {}.", userDto.getId(), id);
                throw new ForbiddenOperationException("You do not have permission to update this resource.");
            }

            expenseMapper.updateFromDto(updateDto, savedExpense);

            expenseRepository.save(savedExpense);

            log.info("Successfully updated expense with id {}.", savedExpense.getId());
        } catch (Exception ex) {
            log.warn("Failed to update expense {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        try {
            Expense savedExpense = expenseRepository.findById(id)
                    .orElseThrow(() -> new ExpenseNotFoundException("Expense with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (!userDto.getId().equals(savedExpense.getUser().getId())) {
                log.warn("Failed attempt of user {} to delete expense {}.", userDto.getId(), id);
                throw new ForbiddenOperationException("You do not have permission to delete this resource.");
            }

            savedExpense.setDeleted(true);

            expenseRepository.save(savedExpense);

            log.info("Successfully deleted expense with id {}.", savedExpense.getId());
        } catch (Exception ex) {
            log.warn("Failed to delete expense {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public BigDecimal calculateTotalAmountForCurrentMonth() {
        try {
            LocalDate today = LocalDate.now();

            LocalDateTime startOfMonth = today.with(firstDayOfMonth()).atStartOfDay();

            LocalDateTime endOfPeriod = today.with(firstDayOfNextMonth()).atStartOfDay();

            UUID userId = securityUserService.getLoggedInUserInfo().getId();

            BigDecimal total = expenseRepository.calculateTotalAmountSpentForPeriod(userId, startOfMonth, endOfPeriod);

            log.info("Successfully calculated current month's expense total.");

            return total;
        } catch (Exception ex) {
            log.warn("Failed to calculated current month's expense total. With exception: {}", ex.getMessage());
            throw ex;
        }
    }
}
