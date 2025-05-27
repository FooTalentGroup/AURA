package com.clinica.aura.modules.patient.dto;

import com.clinica.aura.modules.patient.dto.valid.ValidInsurance;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para recibir y validar datos de entrada al registrar o actualizar un paciente.
 * Este DTO contiene datos personales, información de contacto, datos del tutor,
 * datos de la obra social (si corresponde), y referencias a profesionales y escuela.
 * Aplica validaciones estándar y una validación personalizada con {@link com.clinica.aura.modules.patient.dto.valid.ValidInsurance}
 * para garantizar coherencia entre los campos de seguro médico.
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ValidInsurance
public class PatientRequestDto {
    @NotBlank
    @Email(message = "Formato de email inválido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(\\.[A-Za-z]{2,})?$",
            message = "El email debe tener un dominio válido, como .com o .com.ar"
    )
    @Schema(description = "Email del paciente", example = "juanLopez@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;


    @Pattern(regexp = "^[1-9]\\d{7}$", message = "El DNI debe tener exactamente 8 caracteres numéricos")
    @Size(min =8, max=8, message = "El dni debe 8 caracteres")
    @NotBlank(message = "El DNI es obligatorio")
    @Schema(description = "DNI del paciente", requiredMode = Schema.RequiredMode.REQUIRED, example = "40650777")
    private String dni;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo debe contener letras")
    @Size(min = 3, max = 15, message = "El nombre del paciente debe tener entre 3 y 15 caracteres")
    @NotBlank
    @Schema(description = "Nombre del paciente", example = "Camila", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El apellido solo debe contener letras")
    @Size(min = 3, max = 15, message = "El apellido debe tener entre 3 y 15 caracteres")
    @NotBlank
    @Schema(description = "Apellido del paciente", example = "Lopez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Schema(description = "Teléfono del tutor del paciente. El teléfono está pensado en formato argentino, se permite el mínimo de 8 números en caso de no agregar el 011 ", example = "02320484070, 1155150791, 01144697500", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^\\d{8,11}$", message = "El teléfono del tutor debe tener entre 8 y 11 dígitos numéricos")
    @NotBlank(message = "El teléfono del tutor es obligatorio")
    private String phoneNumber;

    @Past
    @Schema(description = "Fecha de nacimiento del paciente", example = "2019-07-20")
    private LocalDate birthDate;


    @Schema(description = "Indica el género del paciente ", example = "femenino/masculino/otro (no permite otras palabras)")
    @Pattern(regexp = "^(?i)(femenino|masculino|otro)$", message = "El género debe ser 'Femenino', 'Masculino' u 'Otro'")
    private String genre;

    @Schema(description = "Indica si el paciente tiene seguro médico", example = "true")
    private boolean hasInsurance;

    @Schema(description = "Nombre de la obra social (si tiene). Si no tiene, debe ser 'Particular'", example = "OSDE")
    private String insuranceName;

    @Schema(description = "Nombre del plan de la obra social del paciente", example = "210")
    private String insurancePlan;

    @Schema(description = "Número de afiliado en la obra social. Ej: OSDE (156150-06), Swiss Medical (000012345678), etc.", example = "451236-02")
    private String memberShipNumber;


    @Schema(description = "Dirección del paciente", example = "Av. Libertador 1925, CABA")
    @Size(min = 5, max = 30, message = "La dirección del paciente debe tener entre 5 y 30 caracteres.")
    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @Schema(description = "Nombre del tutor paciente", example = "Juan Lopez")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre del tutor solo debe contener letras")
    @Size(min = 7, max = 25, message = "El nombre del tutor a cargo del paciente debe tener entre 7 y 25 caracteres")
    @NotBlank(message = "El nombre del tutor es obligatorio")
    private String tutorName;

    @Schema(description = "Vínculo del tutor con el paciente. Las únicas opciones válidas son: madre, padre o tutor.", example = "Madre, padre o tutor. Solo se permite una opción.")
    @Pattern(regexp = "^(?i)(madre|padre|tutor)$", message = "El vínculo debe ser 'Madre', 'Padre' o 'Tutor'")
    @NotBlank(message = "La relación  del tutor con el paciente es obligatoria")
    private String relationToPatient;

    @Schema(description = "Los IDs de los Profesionales asignados al paciente", example = "[1, 2, 3]")
    private List<Long> professionalIds;

    @Schema(description = "ID de la escuela. Este campo no es obligatorio considerando que los pacientes tienen entre 3 y 13 años inclusive por lo cual, " +
            "si el menor tiene 3 años, podría no estar escolarizado. En caso de estar escolarizado, primero debe crearse la escuela " +
            "y luego añadir acá el Id de la escuela", example = "1")
    private Long schoolId;

}