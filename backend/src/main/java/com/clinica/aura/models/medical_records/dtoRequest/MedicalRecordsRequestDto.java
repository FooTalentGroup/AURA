package com.clinica.aura.models.medical_records.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordsRequestDto {

    @Positive(message = "El ID del paciente debe ser un número positivo")
    @NotNull(message = "El ID del paciente es obligatorio")
    @Schema(description = "ID del paciente al que pertenece el historial médico", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long patientId;

//    @Positive(message = "El ID del profesional debe ser un número positivo")
//    @NotNull(message = "El ID del profesional es obligatorio")
//    @Schema(description = "ID del profesional al que pertenece el historial médico", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Long professionalId;

    @NotBlank(message = "Las notas médicas no pueden estar vacías")
    @Size(max = 1000, message = "Las notas no pueden tener más de 1000 caracteres")
    @Schema(description = "Notas médicas del paciente", example = "Paciente con antecedentes de hipertensión.")
    private String notes;

//    @Pattern(regexp = "^$|\\S+(?: \\S+)*$", message = "El campo no puede contener solo espacios")
//    @Size(max = 500, message = "Las alergias no pueden tener más de 500 caracter")
//    @Schema(description = "Alergias del paciente", example = "Alergia a la penicilina")
//    private String allergies;

    @Pattern(regexp = "^$|\\S+(?: \\S+)*$", message = "El campo no puede contener solo espacios")
    @Size(max = 500, message = "Las condiciones previas no pueden tener más de 500 caracteres")
    @Schema(description = "Condiciones médicas previas del paciente", example = "Asma diagnosticada en 2015")
    private String previousConditions;
}
