package com.clinica.aura.entities.receptionist.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionistRequestUpdateDto {

    @Schema(description = "Correo electrónica del paciente", example = "juan.perez@example.com")
    @Email(message = "El correo electrónica no es válido")
    private String email;

    @Schema(description = "DNI del profesional", example = "12345678A")
    private String dni;

    @Schema(description = "Nombre del paciente", example = "Juan")
    private String name;

    @Schema(description = "Apellido del paciente", example = "Pérez")
    private String lastName;

    @Schema(description = "Número de teléfono del paciente", example = "+573001112233")
    private String phoneNumber;
//
//    @Schema(description = "País de residencia del paciente", example = "Colombia")
//    private String country; // campo que se pide eliminar 02/05/2025
//
//    @Schema(description = "URL de la foto del paciente", example = "https://example.com/foto.jpg")
//    private String photoUrl; // campo que se pide eliminar 02/05/2025
//
//    @Past
//    @Schema(description = "Fecha de nacimiento del paciente", example = "1990-05-20")
//    private LocalDate birthDate; // campo que se pide eliminar 02/05/2025
}
