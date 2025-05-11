package com.clinica.aura.models.follow_up_entries.dtoRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpEntriesDtoRequestUpdate {

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private Date date;

    @NotBlank(message = "Las notas no pueden estar vacías")
    private String notes;

}
