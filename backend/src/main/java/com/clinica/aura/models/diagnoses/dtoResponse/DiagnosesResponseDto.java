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

    private String id;
    private LocalDate date;
    private Long idProfessional;
    private String title;
    private String details;

}
