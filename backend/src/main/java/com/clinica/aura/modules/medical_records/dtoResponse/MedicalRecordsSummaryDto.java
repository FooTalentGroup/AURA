package com.clinica.aura.modules.medical_records.dtoResponse;

import lombok.*;

import java.time.LocalDate;

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
