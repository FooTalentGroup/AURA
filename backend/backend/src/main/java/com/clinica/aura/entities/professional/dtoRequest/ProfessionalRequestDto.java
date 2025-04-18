package com.clinica.aura.entities.professional.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalRequestDto {
    @NotBlank(message = "El email es obligatorio")
    @Schema(description = "Correo del usuario", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin1@example.com")
    @Email
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario",requiredMode = Schema.RequiredMode.REQUIRED, example = "admin123")
    private String password;

    @NotBlank(message = "El DNI es obligatorio")
    @Schema(description = "DNI del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345678A")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "John")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "Doe")
    private String lastName;

    @NotBlank(message = "El telefono es obligatorio")
    @Schema(description = "Telefono del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456789")
    private String phoneNumber;

    @NotBlank(message = "El pais es obligatorio")
    @Schema(description = "Pais del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "Spain")
    private String country;

    @Schema(description = "URL de la foto del profesional", example = "https://example.com/photo.jpg")
    private String photoUrl;

    @Schema(description = "Fecha de nacimiento del profesional", example = "1990-01-01")
    private LocalDate birthDate;

    @NotBlank(message = "El numero de licencia es obligatorio")
    @Schema(description = "Numero de licencia del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345678")
    private String licenseNumber;

    @NotBlank(message = "La especialidad es obligatoria")
    @Schema(description = "Especialidad del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "Medicina")
    private String specialty;
}
