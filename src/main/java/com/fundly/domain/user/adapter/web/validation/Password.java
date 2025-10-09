package com.fundly.domain.user.adapter.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String password();

    String confirmPassword();

    @interface List {
        Password[] value();
    }

    public String message() default "Invalid password format.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
