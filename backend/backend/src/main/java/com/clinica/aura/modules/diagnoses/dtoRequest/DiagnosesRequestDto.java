package com.clinica.aura.modules.diagnoses.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosesRequestDto {

    @NotNull(message = "El id de la historia clinica es requerido")
    @Schema(description = "El id de la historia clinica", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long medicalRecordId;

    @NotBlank(message = "El titulo es requerido")
    @Schema(description = "El titulo del diagnostico", example = "Diagnostico", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank(message = "El detalle es requerido")
    @Schema(description = "El detalle del diagnostico", example = "Detalle", requiredMode = Schema.RequiredMode.REQUIRED)
    private String details;
}
