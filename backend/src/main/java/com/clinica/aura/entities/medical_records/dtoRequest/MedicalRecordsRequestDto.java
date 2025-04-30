package com.clinica.aura.entities.medical_records.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordsRequestDto {
    private Long patientId;
    private Long professionalId;
    private String notes;
    private String allergies;
    private String previousConditions;
}
