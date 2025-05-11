package com.clinica.aura.models.medical_records.dtoResponse;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordsSummaryDto {
    private Long id;
    private Long patientId;
    private String notes;
    private String previousConditions;
    private Long createdById;
    private Long updatedById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
