package com.clinica.aura.modules.medical_records.dtoResponse;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordsResponseDto {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long patientId;
    private Long professionalId;
    private List<Long> diagnosisIds;
    private List<Long> followUpIds;
}
