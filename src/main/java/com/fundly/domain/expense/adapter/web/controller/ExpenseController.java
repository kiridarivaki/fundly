package com.fundly.domain.expense.adapter.web.controller;

import com.fundly.domain.auth.core.port.in.SecurityUserService;
import com.fundly.domain.expense.adapter.web.dto.CategoryWithExpensesResponse;
import com.fundly.domain.expense.adapter.web.dto.CreateExpenseRequest;
import com.fundly.domain.expense.adapter.web.dto.UpdateExpenseRequest;
import com.fundly.domain.expense.core.model.enums.ExpenseSortingOptions;
import com.fundly.domain.expense.core.model.enums.FrequencyCategory;
import com.fundly.domain.expense.core.port.in.ExpenseCategoryService;
import com.fundly.domain.expense.core.port.in.ExpenseService;
import com.fundly.domain.expense.infrastructure.dto.ExpenseCategoryDTO;
import com.fundly.domain.expense.infrastructure.dto.ExpenseDTO;
import com.fundly.domain.expense.infrastructure.mapper.ExpenseCategoryMapper;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ExpenseCategoryService categoryService;
    private final SecurityUserService securityUserService;
    private final ExpenseCategoryMapper categoryMapper;

    public ExpenseController(
            ExpenseService expenseService,
            ExpenseCategoryService categoryService,
            SecurityUserService securityUserService,
            ExpenseCategoryMapper categoryMapper) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.securityUserService = securityUserService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping("/expense-manager")
    public String showExpenseManagerHome(Model model) {
        BigDecimal total = expenseService.calculateTotalAmountForCurrentMonth();

        model.addAttribute("totalAmountSpent", total);

        return "expense/expense-manager";
    }

    @GetMapping("/history")
    public String showExpenseHistory(Model model,
                                     @RequestParam(required = false) String searchTerm,
                                     @PageableDefault(page = 0, size = 10, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        Order sortOrder = pageable.getSort().stream()
                .findFirst()
                .orElse(Sort.Order.by("date").with(Sort.Direction.DESC));

        Page<ExpenseDTO> expensesPaged;

        expensesPaged = expenseService.findAllPaged(pageable, Optional.ofNullable(searchTerm));

        model.addAttribute("expensesPaged", expensesPaged);
        model.addAttribute("currentPage", expensesPaged.getNumber() + 1);
        model.addAttribute("totalExpenses", expensesPaged.getTotalElements());
        model.addAttribute("totalPages", expensesPaged.getTotalPages());
        model.addAttribute("pageSize", pageable.getPageSize());

        model.addAttribute("sortField", sortOrder.getProperty());
        model.addAttribute("sortDir", sortOrder.getDirection().name());
        model.addAttribute("sortOptions", ExpenseSortingOptions.values());
        model.addAttribute("searchTerm", searchTerm == null ? "" : searchTerm);

        return "expense/history";
    }

    @GetMapping("/list")
    public String listByCategory(Model model) {
        UserDTO userDto = securityUserService.getLoggedInUserInfo();
        model.addAttribute("user", userDto);

        List<CategoryWithExpensesResponse> categoriesWithExpenses = categoryService.findCategoriesWithExpensesForUser(userDto.getId());
        model.addAttribute("categoriesWithExpenses", categoriesWithExpenses);

        return "expense/list-by-category";
    }

    @GetMapping("/add")
    public String showAddExpenseForm(Model model, @RequestParam(name = "categoryId", required = false) UUID categoryId) {
        CreateExpenseRequest createDto = new CreateExpenseRequest();

        if (categoryId != null) {
            createDto.setCategoryId(categoryId);
        }

        model.addAttribute("expense", createDto);

        List<ExpenseCategoryDTO> expenseCategories = categoryService.findAllForUser();
        model.addAttribute("expenseCategories", expenseCategories);

        model.addAttribute("frequencies", FrequencyCategory.values());

        return "expense/add";
    }

    @PostMapping("/add")
    public String addExpense(@ModelAttribute("expense") @Valid CreateExpenseRequest createDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("frequencies", FrequencyCategory.values());

            return "expense/add";
        }

        UUID id = expenseService.create(createDto);

        redirectAttributes.addAttribute("expenseId", id);

        return "redirect:/expense/details/{expenseId}";
    }

    @GetMapping("/details/{expenseId}")
    public String showExpenseDetails(@PathVariable UUID expenseId, Model model) {
        ExpenseDTO expenseDto = expenseService.findById(expenseId);

        model.addAttribute("expense", expenseDto);

        return "expense/details";
    }

    @GetMapping("/edit/{expenseId}")
    public String showEditExpenseForm(@PathVariable UUID expenseId, Model model) {
        ExpenseDTO expenseDto = expenseService.findById(expenseId);

        model.addAttribute("expense", expenseDto);

        return "expense/edit";
    }

    @PostMapping("/edit/{expenseId}")
    public String updateExpense(@PathVariable UUID expenseId,
                                @ModelAttribute("expense") @Valid UpdateExpenseRequest updateDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("frequencies", FrequencyCategory.values());

            return "expense/edit";
        }

        expenseService.update(updateDto, expenseId);

        redirectAttributes.addAttribute("expenseId", expenseId);

        return "redirect:/expense/details/{expenseId}";
    }

    @PostMapping("/delete/{expenseId}")
    public String deleteExpense(@PathVariable UUID expenseId) {

        expenseService.delete(expenseId);

        return "redirect:/expense/expense-manager";
    }
}
