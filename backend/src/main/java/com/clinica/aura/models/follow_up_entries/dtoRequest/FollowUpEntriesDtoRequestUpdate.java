package com.clinica.aura.models.follow_up_entries.dtoRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpEntriesDtoRequestUpdate {

    @NotBlank(message = "La especialidad es obligatoria")
    private String specialty;

    @NotBlank(message = "Las observaciones no pueden estar vacías")
    private String observations;

    @NotBlank(message = "Las intervenciones no pueden estar vacías")
    private String interventions;

    @NotBlank(message = "Las instrucciones para la próxima sesión son obligatorias")
    private String nextSessionInstructions;

    private LocalDateTime updatedAt;

}
