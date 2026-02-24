package com.fundly.domain.user.ui.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordConstraintValidator implements ConstraintValidator<Password, Object> {
    private String passwordFieldName;
    private String confirmPasswordFieldName;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.passwordFieldName = constraintAnnotation.password();
        this.confirmPasswordFieldName = constraintAnnotation.confirmPassword();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // Use BeanWrapperImpl to get the field values from the DTO
        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(passwordFieldName);
        Object confirmPasswordValue = new BeanWrapperImpl(value).getPropertyValue(confirmPasswordFieldName);

        String passwordString = (String) passwordValue;
        if (passwordString == null) {
            return true;
        }

        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$");
        Matcher matcher = pattern.matcher(passwordString);
        boolean isComplex = matcher.matches();

        if (!isComplex) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password must have at least one uppercase letter, one lowercase letter, one number, one special character, and be at least 8 characters long.")
                    .addPropertyNode(passwordFieldName)
                    .addConstraintViolation();
            return false;
        }

        if (passwordValue == null || confirmPasswordValue == null || !passwordValue.equals(confirmPasswordValue)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match.")
                    .addPropertyNode(confirmPasswordFieldName)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}