package com.clinica.aura.models.clinical_report.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para crear o actualizar un informe clínico")
public class ClinicalReportRequestDto {

    @Schema(description = "Nombre del informe clínico", example = "Informe neurológico")
    @NotNull(message = "El nombre no puede ser nulo")
    private String name;

    @Schema(description = "Fecha del informe clínico", example = "2024-05-12")
    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate date;

    @Schema(description = "ID del profesional que realizó el informe", example = "1")
    @NotNull(message = "El ID del profesional no puede ser nulo")
    private Long professionalId;

    @Schema(description = "ID del antecedente médico al que pertenece el informe", example = "2")
    @NotNull(message = "El ID del antecedente médico no puede ser nulo")
    private Long medicalBackgroundId;
}