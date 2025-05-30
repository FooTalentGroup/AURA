package com.clinica.aura.modules.medical_background.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalBackgroundRequestDto {

    @Schema(description = "ID del paciente asociado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del paciente no puede ser nulo")
    private Long patientId;

    @Schema(description = "Lista de alergias del paciente", example = "[\"Polen\", \"Penicilina\"]")
    private List<String> allergies;

    @Schema(description = "Lista de discapacidades del paciente", example = "[\"Dislexia\"]")
    private List<String> disabilities;


}
