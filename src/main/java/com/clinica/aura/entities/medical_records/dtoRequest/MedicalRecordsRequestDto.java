package com.clinica.aura.entities.medical_records.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordsRequestDto {

    @NotNull(message = "El ID del paciente es obligatorio")
    @Schema(description = "ID del paciente al que pertenece el historial médico", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long patientId;

    @NotBlank(message = "Las notas médicas no pueden estar vacías")
    @Size(max = 1000, message = "Las notas no pueden tener más de 1000 caracteres")
    @Schema(description = "Notas médicas del paciente", example = "Paciente con antecedentes de hipertensión.")
    private String notes;

    @Size(max = 500, message = "Las alergias no pueden tener más de 500 caracter")
    @Schema(description = "Alergias del paciente", example = "Alergia a la penicilina")
    private String allergies;

    @Size(max = 500, message = "Las condiciones previas no pueden tener más de 500 caracteres")
    @Schema(description = "Condiciones médicas previas del paciente", example = "Asma diagnosticada en 2015")
    private String previousConditions;

    }
