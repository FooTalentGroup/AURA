package com.clinica.aura.models.medical_records.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordsRequestUpdateDto {
    private String notes;
//    private String allergies;
    private String previousConditions;
}
