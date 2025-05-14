package com.clinica.aura.models.patient.dto.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InsuranceValidator.class)
@Documented
public @interface ValidInsurance {
    String message() default "Datos de seguro médico inválidos.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
