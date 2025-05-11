package com.clinica.aura.models.medical_records.dtoResponse;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordsSummaryDto {
    private String specialty;
    private String professionalName;
    private LocalDate createdAt;
}
