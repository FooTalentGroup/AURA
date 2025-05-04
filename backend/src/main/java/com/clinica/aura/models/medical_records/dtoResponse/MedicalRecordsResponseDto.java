package com.clinica.aura.models.medical_records.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordsResponseDto {

    @Schema(description = "ID del historial médico", example = "10")
    private Long id;

    @Schema(description = "Notas médicas del paciente", example = "Paciente con diabetes tipo 2")
    private String notes;

    @Schema(description = "Alergias del paciente", example = "Alergia al polen")
    private String allergies;

    @Schema(description = "Condiciones médicas previas", example = "Hipotiroidismo")
    private String previousConditions;

    @Schema(description = "ID del paciente", example = "1")
    private Long patientId;
}
