package com.clinica.aura.models.professional.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter


@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalResponseDto {

    @Schema(description = "ID del profesional", example = "1")
    private Long id;

  //  @Schema(description = "Correo del usuario", example = "profesional@example.com")
  //  private String email;

    @Schema(description = "DNI del profesional", example = "12345678")
    private String dni;

    @Schema(description = "Nombre del profesional", example = "Juan")
    private String name;

    @Schema(description = "Apellido del profesional", example = "Perez")
    private String lastName;

    @Schema(description = "Teléfono del profesional", example = "123456789")
    private String phoneNumber;

    @Schema(description = "Dirección del profesional", example = "Av. Siempre Viva 742")
    private String address;

    @Schema(description = "Fecha de nacimiento del profesional", example = "1990-01-01")
    private LocalDate birthDate;

    @Schema(description = "Localidad del profesional", example = "Tandil")
    private String locality;

    @Schema(description = "CUIL del profesional", example = "20-12345678-9")
    private String cuil;

    @Schema(description = "Número de matrícula del profesional", example = "12345678")
    private String licenseNumber;

    @Schema(description = "Especialidad del profesional", example = "Fonoudiología")
    private String specialty;

    @Schema(
            description = "Lista de IDs de los pacientes asignados al profesional",
            example = "[1, 2, 3]"
    )
    private List<Long> patientIds;
}