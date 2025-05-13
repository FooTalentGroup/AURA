package com.clinica.aura.models.medical_records.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
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
    //private String specialty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long patientId;
    private Long professionalId;
    private List<Long> diagnosisIds;
    private List<Long> followUpIds;
}
