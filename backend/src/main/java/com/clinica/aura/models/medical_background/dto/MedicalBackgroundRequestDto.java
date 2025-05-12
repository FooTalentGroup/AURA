package com.clinica.aura.models.medical_background.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalBackgroundRequestDto {

    @Schema(description = "ID del paciente asociado", example = "1", required = true)
    @NotNull(message = "El ID del paciente no puede ser nulo")
    private Long patientId;

    @Schema(description = "Informe escolar del paciente", example = "Informe del colegio sobre el comportamiento y desempeño académico")
    private String schoolReports;

    @Schema(description = "Lista de alergias del paciente", example = "[\"Polen\", \"Penicilina\"]")
    @NotEmpty(message = "Debe especificar al menos una alergia")
    private List<String> allergies;

    @Schema(description = "Lista de discapacidades del paciente", example = "[\"Dislexia\"]")
    @NotEmpty(message = "Debe especificar al menos una discapacidad")
    private List<String> disabilities;
}
