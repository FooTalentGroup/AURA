package com.clinica.aura.entities.patient.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDto {
    // Datos del usuario
    @NotBlank
    @Email
    @Schema(description = "Correo electrónico del paciente", example = "juan.perez@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña del paciente", example = "securePassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    // Datos de la persona
    @NotBlank(message = "El DNI es obligatorio")
    @Schema(description = "DNI del profesional", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345678A")
    private String dni;

    @NotBlank
    @Schema(description = "Nombre del paciente", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description = "Apellido del paciente", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Schema(description = "Número de teléfono del paciente", example = "+573001112233")
    private String phoneNumber;

    @Schema(description = "País de residencia del paciente", example = "Colombia")
    private String country;

    @Schema(description = "URL de la foto del paciente", example = "https://example.com/foto.jpg")
    private String photoUrl;

    @Past
    @Schema(description = "Fecha de nacimiento del paciente", example = "1990-05-20")
    private LocalDate birthDate;

    // Datos específicos del paciente
    @Schema(description = "Indica si el paciente tiene seguro médico", example = "true")
    private boolean hasInsurance;

    @Schema(description = "Nombre del seguro médico (si aplica)", example = "Sanitas EPS")
    private String insuranceName;

    @Schema(description = "Nombre del colegio o institución educativa", example = "Colegio San Juan")
    private String school;

    @Schema(description = "Tipo de pago preferido", example = "Efectivo")
    private String paymentType;
}
