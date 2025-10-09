package com.fundly.domain.expense.adapter.web.controller;

import com.fundly.domain.expense.adapter.web.dto.CreateExpenseCategoryRequest;
import com.fundly.domain.expense.adapter.web.dto.UpdateExpenseCategoryRequest;
import com.fundly.domain.expense.core.model.enums.BudgetType;
import com.fundly.domain.expense.core.port.in.ExpenseCategoryService;
import com.fundly.domain.expense.infrastructure.dto.ExpenseCategoryDTO;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/category")
@Log4j2
public class ExpenseCategoryController {
    private final ExpenseCategoryService categoryService;

    public ExpenseCategoryController(
            ExpenseCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String showCategoryList(Model model) {

        return "category/list";
    }

    @GetMapping("/add")
    public String showAddCategoryForm(@RequestParam(name = "from", required = false) String fromPage, Model model) {
        ExpenseCategoryDTO createCategoryDto = new ExpenseCategoryDTO();

        model.addAttribute("expenseCategory", createCategoryDto);
        model.addAttribute("fromPage", fromPage);
        model.addAttribute("budgetTypes", BudgetType.values());

        return "category/add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") @Valid CreateExpenseCategoryRequest createCategoryDto,
                              BindingResult bindingResult,
                              @RequestParam(name = "from", required = false) String fromPage,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("budgetTypes", BudgetType.values());

            return "category/add";
        }

        UUID id = categoryService.create(createCategoryDto);
        redirectAttributes.addAttribute("categoryId", id);

        if ("add-expense".equalsIgnoreCase(fromPage))
            return "redirect:/expense/add";

        return "redirect:/category/details/{categoryId}";
    }

    @GetMapping("/details/{categoryId}")
    public String showCategoryDetails(@PathVariable UUID categoryId, Model model) {
        ExpenseCategoryDTO categoryDto = categoryService.findById(categoryId);

        model.addAttribute("expenseCategory", categoryDto);

        return "category/details";
    }

    @GetMapping("/edit/{categoryId}")
    public String showEditCategoryForm(@PathVariable UUID categoryId, Model model) {
        ExpenseCategoryDTO categoryDto = categoryService.findById(categoryId);

        model.addAttribute("expenseCategory", categoryDto);

        return "category/edit";
    }

    @PostMapping("/edit/{categoryId}")
    public String updateCategory(@PathVariable UUID categoryId,
                                 @ModelAttribute("expense") @Valid UpdateExpenseCategoryRequest updateCategoryDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("budgetTypes", BudgetType.values());

            return "category/edit/";
        }

        categoryService.update(updateCategoryDto, categoryId);

        redirectAttributes.addAttribute("categoryId", categoryId);

        return "redirect:/category/details/{categoryId}";
    }

    @GetMapping("/delete/{categoryId}")
    public String showDeleteConfirmation(@PathVariable UUID categoryId, Model model) {
        ExpenseCategoryDTO categoryDto = categoryService.findById(categoryId);

        if (categoryDto.getUserId() != null) {
            log.warn("Unable to delete default expense category {}.", categoryDto.getName());

            return "auth/access-denied";
        }

        model.addAttribute("expenseCategory", categoryDto);

        return "category/delete";
    }

    @PostMapping("/delete/{categoryId}")
    public String deleteCategory(@PathVariable UUID categoryId,
                                 @RequestParam(name = "withExpenses", required = true) boolean withExpenses,
                                 Model model) {

        categoryService.delete(categoryId, withExpenses);

        return "redirect:/expense/expense-manager";
    }
}
