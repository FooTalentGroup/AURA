package com.clinica.aura.models.school.dto;

import com.clinica.aura.models.patient.model.PatientModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SchoolResponseDto {
    private Long id;
    private String schoolName;
    private String emailSchool;
    private String phoneSchool;

   }
