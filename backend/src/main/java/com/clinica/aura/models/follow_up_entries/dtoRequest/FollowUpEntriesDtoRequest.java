package com.clinica.aura.models.follow_up_entries.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpEntriesDtoRequest {

    @NotNull(message = "El ID de la historia clínica es obligatorio")
    private Long medicalRecordId;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private Date date;

    @NotBlank(message = "Las notas no pueden estar vacías")
    private String notes;
}
