package com.clinica.aura.models.diagnoses.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosesResponseDto {

    private Long id;
    private LocalDate date;
    private String title;
    private String details;
    private Long idProfessional;
    private Long medicalRecordId;

}
