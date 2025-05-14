package com.clinica.aura.models.medical_records.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordsRequestDto {

    @Positive(message = "El ID del paciente debe ser un número positivo")
    @NotNull(message = "El ID del paciente es obligatorio")
    @Schema(description = "ID del paciente al que pertenece el historial médico", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long patientId;
}
