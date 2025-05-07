package com.clinica.aura.models.patient.dto;

import com.clinica.aura.models.school.model.SchoolModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class PatientResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
    private String dni;
    private String email;
    private boolean hasInsurance;
    private String insuranceName;
//    private String school;  05/05/2025 es una entidad ahora
    private String address;
    private String tutorName;
    private String relationToPatient;
    private List<Long> professionalIds;
    private String level;//level y shift eran de escuela y ahora son de paciente
    private String shift;
    private Long schoolId;

}
