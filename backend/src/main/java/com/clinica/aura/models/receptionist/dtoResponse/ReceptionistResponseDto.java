package com.clinica.aura.models.receptionist.dtoResponse;

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

    private String name;

    private String lastName;

    private String phoneNumber;

    private String country;

    private String photoUrl;

    private LocalDate birthDate;
}
