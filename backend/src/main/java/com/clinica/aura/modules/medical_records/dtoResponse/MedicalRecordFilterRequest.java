package com.clinica.aura.modules.medical_records.dtoResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MedicalRecordFilterRequest {
    private Long patientId;
    private Long professionalId;
    private String specialty;
    private LocalDateTime startDate;
}
