package com.clinica.aura.models.patient.dto;

import com.clinica.aura.models.school.model.SchoolModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de salida que representa los datos completos de un paciente
 * devueltos por la API en respuestas a consultas o registros.
 * Incluye información personal, contacto, obra social, vínculo con el tutor,
 * IDs de profesionales asignados y la escuela asociada (si aplica).
 */
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
    private int age;
    private String genre;
    private boolean hasInsurance;
    private String insuranceName;
    private String insurancePlan;
    private String memberShipNumber;
    private String address;
    private String tutorName;
    private String relationToPatient;
    private List<Long> professionalIds;
    private Long schoolId;

}
