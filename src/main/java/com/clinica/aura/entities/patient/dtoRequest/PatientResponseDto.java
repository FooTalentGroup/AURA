package com.clinica.aura.entities.patient.dtoRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PatientResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String country;
    private String photoUrl;
    private LocalDate birthDate;
    private String dni;
    private String email;
    private boolean hasInsurance;
    private String insuranceName;
    private String school;
    private String paymentType;
}
