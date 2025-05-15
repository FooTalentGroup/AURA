package com.clinica.aura.modules.medical_records.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalSummaryDto {
    private Long id;
    private String name;
    private String specialty;
}
