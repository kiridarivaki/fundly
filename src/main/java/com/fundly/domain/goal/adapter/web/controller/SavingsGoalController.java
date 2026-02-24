package com.fundly.domain.goal.adapter.web.controller;

import com.fundly.domain.goal.adapter.web.dto.CreateSavingsGoalRequest;
import com.fundly.domain.goal.adapter.web.dto.UpdateSavingsGoalRequest;
import com.fundly.domain.goal.core.model.enums.PriorityCategory;
import com.fundly.domain.goal.core.port.in.SavingsGoalService;
import com.fundly.domain.goal.infrastructure.dto.SavingsGoalDTO;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/goal")
public class SavingsGoalController {
    private final SavingsGoalService savingsGoalService;

    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @GetMapping("/list")
    public String showGoalsList() {
        return "goal/list";
    }

    @GetMapping("/list-fragment")
    public String getGoalFragmentByScope(@RequestParam String scope, Model model) {
        boolean isPersonal = scope.equalsIgnoreCase("personal");
        List<SavingsGoalDTO> goals = savingsGoalService.findGoalsForUser(isPersonal);

        model.addAttribute("goals", goals);
        model.addAttribute("scope", scope);

        return "goal/_goalsList :: goalsListFragment";
    }

    @GetMapping("/add")
    public String showAddGoalForm(Model model) {
        CreateSavingsGoalRequest savingsGoal = new CreateSavingsGoalRequest();

        model.addAttribute("savingsGoal", savingsGoal);
        model.addAttribute("priorityCategories", PriorityCategory.values());

        return "/goal/add";
    }

    @PostMapping("/add")
    public String addGoal(@ModelAttribute(name = "savingsGoal") CreateSavingsGoalRequest createDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("priorityCategories", PriorityCategory.values());

            return "/goal/add";
        }

        UUID id = savingsGoalService.create(createDto);

        redirectAttributes.addAttribute("savingsGoalId", id);

        return "redirect:/goal/{savingsGoalId}";
    }

    @GetMapping("/{savingsGoalId}")
    public String showGoalDetails(@PathVariable UUID savingsGoalId, Model model) {
        SavingsGoalDTO savingsGoalDto = savingsGoalService.findById(savingsGoalId);

        model.addAttribute("savingsGoal", savingsGoalDto);

        return "goal/details";
    }

    @GetMapping("/edit/{savingsGoalId}")
    public String showEditGoalForm(@PathVariable UUID savingsGoalId, Model model) {
        SavingsGoalDTO savingsGoalDto = savingsGoalService.findById(savingsGoalId);

        model.addAttribute("savingsGoal", savingsGoalDto);

        return "goal/edit";
    }

    @PostMapping("/edit/{savingsGoalId}")
    public String updateExpense(@PathVariable UUID savingsGoalId,
                                @ModelAttribute("savingsGoal") @Valid UpdateSavingsGoalRequest updateDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("priorityCategories", PriorityCategory.values());

            return "goal/edit";
        }

        savingsGoalService.update(updateDto, savingsGoalId);

        redirectAttributes.addAttribute("savingsGoalId", savingsGoalId);

        return "redirect:/goal/{savingsGoalId}";
    }

    @PostMapping("/delete/{savingsGoalId}")
    public String deleteExpense(@PathVariable UUID savingsGoalId) {
        savingsGoalService.delete(savingsGoalId);

        return "redirect:/goal/list";
    }

    @GetMapping("/{savingsGoalId}/participants")
    public String showGoalParticipants(@PathVariable UUID savingsGoalId) {
        List<UserDTO> participants = savingsGoalService.findParticipantsForGoal(savingsGoalId);

        return "goal/participants";
    }
}

