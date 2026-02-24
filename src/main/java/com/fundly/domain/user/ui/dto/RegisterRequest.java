package com.fundly.domain.user.ui.dto;

import com.fundly.common.enums.Currency;
import com.fundly.domain.user.core.model.enums.EmploymentStatus;
import com.fundly.domain.user.ui.validation.Password;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Password(password = "password", confirmPassword = "confirmPassword")
public class RegisterRequest {
    @NotBlank(message = "Email is required.")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Password confirmation is required.")
    private String confirmPassword;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotNull(message = "Employment status is required.")
    private EmploymentStatus employmentStatus;

    @NotNull(message = "Monthly allowance is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly allowance must be greater than 0.")
    private BigDecimal monthlyAllowance;

    @NotNull(message = "Currency allowance is required.")
    private Currency localCurrency;
}
