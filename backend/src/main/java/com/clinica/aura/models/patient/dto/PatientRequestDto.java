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
    @Email(message = "Formato de email inválido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(\\.[A-Za-z]{2,})?$",
            message = "El email debe tener un dominio válido, como .com o .com.ar"
    )
    @Schema(description = "Email del paciente", example = "9k6w5@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    // Datos de la persona
    @Pattern(regexp = "^[1-9]\\d{7}$", message = "El DNI solo debe tener números y maximo 8 caracteres")
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

    // Datos específicos del paciente                                //cambiar a femenino/masculino/otro
    @Schema(description = "Indica el genero del paciente ", example = "femenino/masculino/otro (no permite otras palabras)")
    @Pattern(regexp = "^(?i)(femenino|masculino|otro)$", message = "El sexo debe ser 'femenino', 'masculino' u 'otro'")
    private String genre;

    @Schema(description = "Indica si el paciente tiene seguro médico", example = "true")
    private boolean hasInsurance;


    @Pattern(
            regexp = "^[a-zA-Z0-9]{3,20}$",
            message = "Debe tener entre 3 y 20 caracteres, solo letras y números."
    )
    @Schema(description = "Nombre de la obra social (si tiene).Debe tener entre 3 y 20 caracteres, solo letras y números.)", example = "OSDE")
    private String insuranceName;


    @Pattern(
            regexp = "^[a-zA-Z0-9]{3,20}$",
            message = "Debe tener entre 3 y 20 caracteres, solo letras y números."
    )
    @Schema(description = "Nombre del plan obra social del paciente", example = "210")
    private String insurancePlan;


    @Size(min = 5, max = 20, message = "El número de afiliado debe tener entre 5 y 20 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9/-]+$", message = "El número de afiliado solo puede contener letras, números, guiones o barras.")
    @Schema(description = "Número de afiliado en la obra social. El número de afiliado solo puede contener letras, números, guiones o barras." +
            "El número de afiliado, se conforma de distintas maneras según la obra social del paciente. A continuación se dejan varios ejemplos." +
            "OSDE (ej: 156150-06)" +
            "Swiss Medical (ej: 000012345678)" +
            "Medicus(ej:1234567-01) " +
            "IOSFA(ej:123456/A) ")
    @Pattern(regexp = "^[a-zA-Z0-9/-]+$", message = "El número de afiliado solo puede contener letras, números, guiones o barras.")
        private String memberShipNumber;


    @Schema(description = "Dirección del paciente", example = "Av. Libertador 1925, CABA")
    @Size(min = 5, max = 30, message = "La dirección del paciente debe tener entre 5 y 30 caracteres.")
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

    @Schema(description = "ID de la escuela. Este campo no es obligatio considerando que los pacientes tienen entre 3 y 13 años inclusive por lo cual, " +
            "si el menor si tiene 3 años podría no estar escolarizado. En caso de estar escolarizado, primero debe crearse la escuela " +
            "y luego añadir acá el Id de la escuela", example = "1")
    private Long schoolId;


}
