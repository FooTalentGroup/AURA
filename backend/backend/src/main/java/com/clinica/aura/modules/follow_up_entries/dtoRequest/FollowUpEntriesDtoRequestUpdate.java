package com.clinica.aura.modules.follow_up_entries.dtoRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpEntriesDtoRequestUpdate {

    /**
     * Observaciones actualizadas realizadas durante la sesión.
     */
    @NotBlank(message = "Las observaciones no pueden estar vacías")
    private String observations;

    /**
     * Intervenciones actualizadas realizadas durante la sesión.
     */
    @NotBlank(message = "Las intervenciones no pueden estar vacías")
    private String interventions;

    /**
     * Instrucciones actualizadas para la próxima sesión.
     */
    @NotBlank(message = "Las instrucciones para la próxima sesión son obligatorias")
    private String nextSessionInstructions;

}
