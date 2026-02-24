package com.fundly.domain.user.ui.adapter.view;

import com.fundly.common.enums.Currency;
import com.fundly.common.exception.UserAlreadyExistsException;
import com.fundly.domain.user.core.model.enums.EmploymentStatus;
import com.fundly.domain.user.core.port.in.UserService;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import com.fundly.domain.user.ui.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserViewController {
    private UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        RegisterRequest registerDto = new RegisterRequest();

        model.addAttribute("user", registerDto);
        model.addAttribute("employmentStatuses", EmploymentStatus.values());
        model.addAttribute("currencies", Currency.values());

        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid RegisterRequest registerDto,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employmentStatuses", EmploymentStatus.values());
            model.addAttribute("currencies", Currency.values());

            return "user/register";
        }

        try {
            userService.register(registerDto);
        } catch (UserAlreadyExistsException ex) {
            bindingResult.rejectValue("email", "register.error.user_exists", ex.getMessage());

            model.addAttribute("employmentStatuses", EmploymentStatus.values());
            model.addAttribute("currencies", Currency.values());

            return "user/register";
        }

        return "/auth/login";
    }

    @GetMapping("/profile/{userId}")
    public String showProfile(@PathVariable UUID userId, Model model) {
        UserDTO userDto = userService.findById(userId);

        model.addAttribute("user", userDto);

        return "user/profile";
    }

    @GetMapping("/edit/{userId}")
    public String showEditUserForm(@PathVariable UUID expenseId, Model model) {
        UserDTO userDto = userService.findById(expenseId);

        model.addAttribute("user", userDto);

        return "expense/edit";
    }

    @PatchMapping("/edit/{userId}")
    public String updateUser(@PathVariable UUID userId,
                             @ModelAttribute("user") @Valid UserDTO updateUserDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("employmentStatuses", EmploymentStatus.values());
            model.addAttribute("currencies", Currency.values());

            return "expense/edit/";
        }

        userService.update(updateUserDto, userId);

        redirectAttributes.addAttribute("userId", userId);

        return "redirect:/expense/details/{userId}";
    }
}
