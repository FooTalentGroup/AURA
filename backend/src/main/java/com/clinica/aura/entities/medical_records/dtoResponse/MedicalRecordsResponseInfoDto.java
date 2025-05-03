package com.clinica.aura.entities.medical_records.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordsResponseInfoDto {

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

    private ProfessionalSummaryDto createdBy;

    private ProfessionalSummaryDto updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
