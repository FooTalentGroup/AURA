package com.clinica.aura.entities.receptionist.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
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
