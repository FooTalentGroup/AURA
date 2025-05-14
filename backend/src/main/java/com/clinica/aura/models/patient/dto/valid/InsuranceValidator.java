package com.clinica.aura.models.patient.dto.valid;

import com.clinica.aura.models.patient.dto.PatientRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validador para los campos relacionados al seguro médico dentro de PatientRequestDto.
 * - Si el paciente tiene seguro (hasInsurance = true):
 *   - insuranceName y insurancePlan deben tener entre 3 y 20 caracteres, solo letras y números.
 *   - memberShipNumber debe tener entre 5 y 20 caracteres y puede incluir letras, números, guiones o barras.
 * - Si el paciente NO tiene seguro (hasInsurance = false):
 *   - insuranceName debe ser exactamente "Particular".
 * Este validador se utiliza con la anotación @ValidInsurance sobre la clase PatientRequestDto.
 */
public class InsuranceValidator implements ConstraintValidator<ValidInsurance, PatientRequestDto> {

    private static final Pattern INSURANCE_NAME_PLAN_PATTERN = Pattern.compile("^[a-zA-Z0-9]{3,20}$");
    private static final Pattern MEMBERSHIP_NUMBER_PATTERN = Pattern.compile("^[a-zA-Z0-9/-]{5,20}$");

    @Override
    public boolean isValid(PatientRequestDto dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        boolean hasInsurance = dto.isHasInsurance();
        String name = dto.getInsuranceName();
        String plan = dto.getInsurancePlan();
        String member = dto.getMemberShipNumber();

        context.disableDefaultConstraintViolation();

        if (hasInsurance) {
            if (name == null || !INSURANCE_NAME_PLAN_PATTERN.matcher(name).matches()) {
                context.buildConstraintViolationWithTemplate("insuranceName debe tener entre 3 y 20 caracteres, solo letras y números.")
                        .addPropertyNode("insuranceName").addConstraintViolation();
                return false;
            }

            if (plan == null || !INSURANCE_NAME_PLAN_PATTERN.matcher(plan).matches()) {
                context.buildConstraintViolationWithTemplate("insurancePlan debe tener entre 3 y 20 caracteres, solo letras y números.")
                        .addPropertyNode("insurancePlan").addConstraintViolation();
                return false;
            }

            if (member == null || !MEMBERSHIP_NUMBER_PATTERN.matcher(member).matches()) {
                context.buildConstraintViolationWithTemplate("memberShipNumber debe tener entre 5 y 20 caracteres y solo puede contener letras, números, guiones o barras.")
                        .addPropertyNode("memberShipNumber").addConstraintViolation();
                return false;
            }

        } else {
            if (name == null || !name.equalsIgnoreCase("Particular")) {
                context.buildConstraintViolationWithTemplate("insuranceName debe ser 'Particular' si no tiene seguro.")
                        .addPropertyNode("insuranceName").addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
