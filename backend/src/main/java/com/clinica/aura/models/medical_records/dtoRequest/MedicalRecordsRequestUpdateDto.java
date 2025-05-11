package com.clinica.aura.models.medical_records.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordsRequestUpdateDto {

    @NotBlank(message = "La especialidad es obligatoria")
    @Schema(description = "Especialidad del paciente", example = "Cardiologo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String speciality;
}
