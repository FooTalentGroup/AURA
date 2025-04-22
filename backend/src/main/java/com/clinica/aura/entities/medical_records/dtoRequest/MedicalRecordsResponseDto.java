package com.clinica.aura.entities.medical_records.dtoRequest;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordsResponseDto {
    private Long id;
    private String notes;
    private String allergies;
    private String previousConditions;
    private Long patientId;
}
