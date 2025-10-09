package com.fundly.domain.user.infrastructure.dto;

import com.fundly.common.enums.Currency;
import com.fundly.domain.user.core.model.enums.EmploymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private UUID id;

    private String email;

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
