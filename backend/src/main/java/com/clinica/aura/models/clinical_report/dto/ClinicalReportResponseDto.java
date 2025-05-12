package com.clinica.aura.models.clinical_report.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para responder con los datos de un informe clínico")
public class ClinicalReportResponseDto {

    @Schema(description = "ID del informe clínico", example = "10")
    private Long id;

    @Schema(description = "Nombre del informe clínico", example = "Informe neurológico")
    private String name;

    @Schema(description = "Fecha del informe clínico", example = "2024-05-12")
    private LocalDate date;

    @Schema(description = "ID del profesional que realizó el informe", example = "1")
    private Long professionalId;

    @Schema(description = "Nombre del profesional", example = "Dra. Laura González")
    private String professionalName;

    @Schema(description = "ID del antecedente médico", example = "2")
    private Long medicalBackgroundId;


}
