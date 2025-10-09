package com.fundly.domain.goal.adapter.web.controller;

import com.fundly.common.exception.AlreadyParticipatingException;
import com.fundly.common.exception.UserNotFoundException;
import com.fundly.domain.goal.adapter.web.dto.AddContributionRequest;
import com.fundly.domain.goal.adapter.web.dto.AddParticipantRequest;
import com.fundly.domain.goal.adapter.web.dto.GoalFeedResponse;
import com.fundly.domain.goal.core.port.in.GoalActivityService;
import com.fundly.domain.goal.core.port.in.SavingsGoalService;
import com.fundly.domain.goal.infrastructure.dto.GoalActivityDTO;
import com.fundly.domain.goal.infrastructure.dto.SavingsGoalDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/goal")
public class GoalActivityController {
    private final SavingsGoalService savingsGoalService;
    private final GoalActivityService activityService;

    public GoalActivityController(SavingsGoalService savingsGoalService, GoalActivityService activityService) {
        this.savingsGoalService = savingsGoalService;
        this.activityService = activityService;
    }

    @GetMapping("/{savingsGoalId}/feed")
    public String showActivityFeed(@PathVariable(name = "savingsGoalId") UUID savingsGoalId,
                                   @RequestParam(defaultValue = "0") int pageNumber,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        SavingsGoalDTO goalDto = savingsGoalService.findById(savingsGoalId);

        if (goalDto.getParticipantsIds().isEmpty()) {
            model.addAttribute("errorMessage", "This goal doesn't have participants. Redirecting to details page...");
            redirectAttributes.addAttribute("savingsGoalId", savingsGoalId);

            return "redirect:/goal/details/{savingsGoalId}";
        }

        List<GoalActivityDTO> activityHistory = activityService.getRecentEventsByGoalId(savingsGoalId, pageNumber);

        SavingsGoalDTO savingsGoalDto = savingsGoalService.findById(savingsGoalId);

        GoalFeedResponse feedResponse = GoalFeedResponse.builder()
                .goalId(savingsGoalId)
                .goalName(savingsGoalDto.getName())
                .amountLeft(savingsGoalDto.getTargetAmount().subtract(savingsGoalDto.getCurrentAmount()))
                .activityHistory(activityHistory)
                .build();

        model.addAttribute("feedData", feedResponse);

        if (!model.containsAttribute("contribution"))
            model.addAttribute("contribution", new AddContributionRequest());

        return "/goal/feed";
    }

    @GetMapping("/{savingsGoalId}/participants/add")
    public String showAddParticipantForm(@PathVariable("savingsGoalId") UUID savingsGoalId,
                                         Model model) {
        SavingsGoalDTO savingsGoalDto = savingsGoalService.findById(savingsGoalId);

        model.addAttribute("savingsGoal", savingsGoalDto);
        model.addAttribute("participant", new AddParticipantRequest());

        return "goal/add-participant";
    }

    @PostMapping("/{savingsGoalId}/participants/add")
    public String addParticipant(@PathVariable(name = "savingsGoalId") UUID savingsGoalId,
                                 @ModelAttribute(name = "participant") AddParticipantRequest addParticipantDto,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            activityService.addParticipant(addParticipantDto, savingsGoalId);

            redirectAttributes.addFlashAttribute("successMessage", "Participant " + addParticipantDto.getEmail() + " added to goal!");
            redirectAttributes.addAttribute("savingsGoalId", savingsGoalId);

            return "redirect:/goal/{savingsGoalId}/feed";
        } catch (AlreadyParticipatingException | UserNotFoundException ex) {
            SavingsGoalDTO savingsGoalDto = savingsGoalService.findById(savingsGoalId);

            model.addAttribute("savingsGoal", savingsGoalDto);
            model.addAttribute("participant", addParticipantDto);
            model.addAttribute("errorMessage", ex.getMessage());

            return "goal/add-participant";
        }
    }

    @PostMapping("/{savingsGoalId}/contribute")
    public String addContribution(@PathVariable(name = "savingsGoalId") UUID savingsGoalId,
                                  @ModelAttribute(name = "contribution") AddContributionRequest contributionDto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (bindingResult.hasErrors())
            return "/goal/feed";

        activityService.processContribution(contributionDto, savingsGoalId);

        redirectAttributes.addAttribute("savingsGoalId", savingsGoalId);

        return "redirect:/goal/{savingsGoalId}/feed";
    }
}
