package com.clinica.aura.models.patient.dtoRequest;

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
//    private String country;  // campo que se pide eliminar 02/05/2025
//    private String photoUrl; // campo que se pide eliminar 02/05/2025
    private LocalDate birthDate; //solo debe estar en paciente 02/05/2025
    private String dni;
    private String email;
    private boolean hasInsurance;
    private String insuranceName;
    private String school;
//    private String paymentType; // campo que se pide eliminar 02/05/2025
    private List<Long> professionalIds;



}
