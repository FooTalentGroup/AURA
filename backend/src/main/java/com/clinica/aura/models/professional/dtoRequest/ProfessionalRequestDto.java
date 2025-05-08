package com.clinica.aura.models.professional.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalRequestDto {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Schema(description = "Correo del usuario", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin1@example.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin123")
    private String password;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos numéricos")
    @Schema(description = "DNI del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345678")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "John")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "Doe")
    private String lastName;

    @NotBlank(message = "El teléfono es obligatorio")
    @Schema(description = "Teléfono del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456789")
    private String phoneNumber;

    @NotBlank(message = "La dirección es obligatoria")
    @Schema(description = "Dirección del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "Av. Siempre Viva 742")
    private String address;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Schema(description = "Fecha de nacimiento del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "1990-01-01")
    private LocalDate birthDate;

    @NotBlank(message = "La localidad es obligatoria")
    @Schema(description = "Localidad del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "Tandil")
    private String locality;

    @NotBlank(message = "El CUIL es obligatorio")
    @Pattern(regexp = "^(20|23|24|27|30|33|34)-?\\d{8}-?\\d$", message = "CUIL inválido. Debe tener el formato XX-XXXXXXXX-X o 11 dígitos")
    @Schema(description = "CUIL del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "20-12345678-9")
    private String cuil;

    @NotBlank(message = "La matrícula es obligatorio")
    @Schema(description = "Número de matrícula del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345678")
    private String licenseNumber;

    @NotBlank(message = "La especialidad es obligatoria")
    @Schema(description = "Especialidad del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "Medicina")
    private String specialty;

    @Schema(
            description = "Lista de IDs de los pacientes asignados al profesional",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "[1, 2, 3]"
    )
    private List<Long> patientIds;
}