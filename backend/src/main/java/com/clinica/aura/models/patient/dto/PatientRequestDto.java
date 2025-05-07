package com.clinica.aura.models.patient.dto;

import com.clinica.aura.models.school.model.SchoolModel;
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
    @Size(min =8, max=8, message = "El dni debe 8 caracteres")
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

    @Schema(description = "Teléfono del tutor del paciente. El teléfono esta pensado en modo argentino, se permite el mínimo de 8 números en caso de no agregar el 011 ", example = "02320484070, 1155150791, 01144697500", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^\\d{8,11}$", message = "El teléfono del tutor debe tener entre 8 y 11 dígitos numéricos")
    @NotBlank(message = "El teléfono del tutor es obligatorio")
    private String phoneNumber;

    @Past
    @Schema(description = "Fecha de nacimiento del paciente", example = "2015-05-20")
    private LocalDate birthDate;

    // Datos específicos del paciente
    @Schema(description = "Indica si el paciente tiene seguro médico", example = "true")
    private boolean hasInsurance;

    @Schema(description = "Nombre de la obra social (si tiene)", example = "Sanitas EPS")
    private String insuranceName;

    @Schema(description = "Dirección del paciente", example = "Av. Libertador 1925, CABA")
    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @Schema(description = "Nombre del tutor paciente", example = "Mariela Peres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre del tutor solo debe contener letras")
    @NotBlank(message = "El nombre del tutor es obligatoria")
    private String tutorName;

    @Schema(description = "Vinculo del tutor con el paciente. Madre, padre y/o tutor.No acepta otras palabras solo esas 3", example = "Madre, padre y/o tutor.")
    @Pattern(regexp = "^(?i)(madre|padre|tutor)$", message = "El vínculo debe ser 'madre', 'padre' o 'tutor'")
    @NotBlank(message = "La relación  del tutor con el paciente es obligatoria")
    private String relationToPatient;

    @Schema(description = "Los IDs de los Profesionales asignados al paciente", example = "[1, 2, 3]")
    private List<Long> professionalIds;

    @Schema(description = "Grado en el que esta el  paciente está actualmente", example ="Primaria - 2° Grado", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nivel es obligatorio")
    private String level;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El turno solo debe contener letras")
    @Schema(description = "Turno en el que el paciente asiste a la escuela", example = "Turno mañana", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El turno es obligatorio")
    private String shift;

    @Schema(description = "ID de la escuela. Este campo no es obligatio considerando que los pacientes tienen entre 3 y 13 años inclusive por lo cual, " +
            "si el menor si tiene 3 años podría no estar escolarizado. En caso de estar escolarizado, primero debe crearse la escuela " +
            "y luego añadir acá el Id de la escuela", example = "1")
   // @NotNull(message = "El ID de la escuela es obligatorio")
    private Long schoolId;


}
