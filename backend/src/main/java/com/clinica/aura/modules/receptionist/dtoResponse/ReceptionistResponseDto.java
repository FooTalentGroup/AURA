package com.clinica.aura.modules.receptionist.dtoResponse;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceptionistResponseDto {
    private Long id;

    private String dni;

    private String email;

    private String name;

    private String lastName;

    private String phoneNumber;

    private String address;

    private String locality;

    private String cuil;

    private LocalDate birthDate;
}
