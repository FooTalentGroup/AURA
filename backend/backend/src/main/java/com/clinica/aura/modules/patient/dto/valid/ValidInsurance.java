package com.clinica.aura.modules.patient.dto.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Anotación para aplicar la validación personalizada del seguro médico en PatientRequestDto.
 * Debe usarse a nivel de clase.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InsuranceValidator.class)
@Documented
public @interface ValidInsurance {
    String message() default "Datos de seguro médico inválidos.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
