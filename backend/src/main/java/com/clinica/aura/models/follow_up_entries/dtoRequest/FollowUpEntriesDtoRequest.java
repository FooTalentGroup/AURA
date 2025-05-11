package com.clinica.aura.models.follow_up_entries.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpEntriesDtoRequest {

    @NotNull(message = "El ID de la historia clínica es obligatorio")
    @Schema(description = "El ID de la historia clínica", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long medicalRecordId;

    @NotBlank(message = "Las observaciones no pueden estar vacías")
    @Schema(description = "Las observaciones", example = "Estoy bien", requiredMode = Schema.RequiredMode.REQUIRED)
    private String observations;

    @NotBlank(message = "Las intervenciones no pueden estar vacías")
    @Schema(description = "Las intervenciones", example = "No hay", requiredMode = Schema.RequiredMode.REQUIRED)
    private String interventions;

    @NotBlank(message = "Las instrucciones para la próxima sesión son obligatorias")
    @Schema(description = "Las instrucciones para la próxima sesión", example = "No hay", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nextSessionInstructions;
}
