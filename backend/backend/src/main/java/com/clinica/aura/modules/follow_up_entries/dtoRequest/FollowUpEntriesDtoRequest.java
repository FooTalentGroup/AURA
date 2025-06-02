package com.clinica.aura.modules.follow_up_entries.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para crear una nueva entrada de seguimiento en una historia clínica.
 * Contiene los datos requeridos para registrar observaciones, intervenciones e instrucciones.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpEntriesDtoRequest {

    /**
     * ID de la historia clínica a la que pertenece esta entrada.
     */
    @NotNull(message = "El ID de la historia clínica es obligatorio")
    @Schema(description = "El ID de la historia clínica", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long medicalRecordId;

    /**
     * Observaciones realizadas durante la sesión.
     */
    @NotBlank(message = "Las observaciones no pueden estar vacías")
    @Schema(description = "Las observaciones", example = "Estoy bien", requiredMode = Schema.RequiredMode.REQUIRED)
    private String observations;

    /**
     * Intervenciones realizadas durante la sesión.
     */
    @NotBlank(message = "Las intervenciones no pueden estar vacías")
    @Schema(description = "Las intervenciones", example = "No hay", requiredMode = Schema.RequiredMode.REQUIRED)
    private String interventions;

    /**
     * Instrucciones para la próxima sesión.
     */
    @NotBlank(message = "Las instrucciones para la próxima sesión no pueden estar vacías")
    @Schema(description = "Las instrucciones para la próxima sesión", example = "No hay", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nextSessionInstructions;
}
