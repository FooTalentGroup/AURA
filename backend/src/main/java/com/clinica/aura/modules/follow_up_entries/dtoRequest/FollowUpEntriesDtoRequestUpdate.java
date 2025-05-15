package com.clinica.aura.modules.follow_up_entries.dtoRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpEntriesDtoRequestUpdate {

    @NotBlank(message = "Las observaciones no pueden estar vacías")
    private String observations;

    @NotBlank(message = "Las intervenciones no pueden estar vacías")
    private String interventions;

    @NotBlank(message = "Las instrucciones para la próxima sesión son obligatorias")
    private String nextSessionInstructions;

}
