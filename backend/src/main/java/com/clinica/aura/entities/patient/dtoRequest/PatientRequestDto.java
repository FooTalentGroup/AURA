package com.clinica.aura.entities.patient.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDto {
    // Datos del usuario
    @NotBlank
    @Email(message = "El correo electrónica no es válido")
    @Schema(description = "Correo electrónico del paciente", example = "juan.perez@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    // Datos de la persona
    @Pattern(regexp = "\\d+", message = "El DNI solo debe tener números")
    @Size(min =8, max=8, message = "El dni debe tener entre 8 caracteres")
    @NotBlank(message = "El DNI es obligatorio")
    @Schema(description = "DNI del paciente", requiredMode = Schema.RequiredMode.REQUIRED, example = "40650777")
    private String dni;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo debe contener letras")
    @NotBlank
    @Schema(description = "Nombre del paciente", example = "Juan Carlos", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El apellido solo debe contener letras")
    @NotBlank
    @Schema(description = "Apellido del paciente", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;


    @Schema(description = "Número de teléfono del paciente", example = "+541155448833")
    private String phoneNumber;

//    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El país solo debe contener letras")
//    @Schema(description = "País de residencia del paciente", example = "Costa Rica")
//    private String country; //02/05/25 campo quitado a partir de la reunion de hoy con Axel, ux y pm dando ok

//    @Schema(description = "URL de la foto del paciente", example = "https://example.com/foto.jpg")
//    private String photoUrl; //campo quitado a partir de la reunion de hoy 2/5/2025



    // Datos específicos del paciente
    @Schema(description = "Indica si el paciente tiene seguro médico", example = "true")
    private boolean hasInsurance;

    @Schema(description = "Nombre del seguro médico (si aplica)", example = "Sanitas EPS")
    private String insuranceName;

    @Schema(description = "Nombre del colegio o institución educativa", example = "Colegio San Juan")
    private String school;

//    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El método de pago solo debe tener letras")
//    @Schema(description = "Tipo de pago preferido", example = "Efectivo")
//    private String paymentType; //02/05/2025 campo que piden que sea eliminado

    @Past //cambie la fecha aca que ahora es un campo de paciente 02/05/25
    @Schema(description = "Fecha de nacimiento del paciente", example = "2015-05-20")
    private LocalDate birthDate;

    @Schema(description = "Los IDs de los Profesionales asignados al paciente", example = "[1, 2, 3]")
    private List<Long> professionalIds;

}
