package com.clinica.aura.models.follow_up_entries.dtoRequest;

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
    private Long medicalRecordId;

    @NotBlank(message = "La especialidad es obligatoria")
    private String specialty;

    @NotBlank(message = "Las observaciones no pueden estar vacías")
    private String observations;

    @NotBlank(message = "Las intervenciones no pueden estar vacías")
    private String interventions;

    @NotBlank(message = "Las instrucciones para la próxima sesión son obligatorias")
    private String nextSessionInstructions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
