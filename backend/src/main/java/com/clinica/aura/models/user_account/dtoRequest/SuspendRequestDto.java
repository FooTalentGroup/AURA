package com.clinica.aura.models.user_account.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuspendRequestDto {
    @Schema(description = "Duración del suspension", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La duración de tiempo es obligatoria")
    private int duration;
    private TimeUnit unit;

    public enum TimeUnit {
        HOURS, DAYS, WEEKS, MONTHS
    }
}
