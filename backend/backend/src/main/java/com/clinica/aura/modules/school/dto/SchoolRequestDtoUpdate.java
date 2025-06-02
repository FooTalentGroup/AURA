package com.clinica.aura.modules.school.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para actualizar los datos de una escuela en el sistema.
 * Este objeto se emplea en operaciones de actualización de registros escolares y contiene
 * la información básica que se puede modificar: nombre, email y teléfono de la institución.
 * Incluye validaciones para asegurar que los datos ingresados cumplan con el formato esperado:
 * <ul>
 *     <li><b>schoolName</b>: Nombre de la escuela. No puede estar en blanco.</li>
 *     <li><b>emailSchool</b>: Dirección de correo electrónico de la escuela. Debe tener un formato válido.</li>
 *     <li><b>phoneSchool</b>: Número telefónico de la escuela. Debe ser un valor numérico de entre 8 y 11 dígitos..</li>
 * </ul>
 *Este DTO es utilizado por el controlador o el servicio al recibir solicitudes HTTP PUT o PATCH
 * para modificar los datos de una escuela ya existente en la base de datos.
 * Ejemplo de uso:
 * <pre>
 * {
 *   "schoolName": "Escuela Técnica N°5",
 *   "emailSchool": "tecnica5@gmail.com",
 *   "phoneSchool": "1155667788"
 * }
 * @see com.clinica.aura.modules.school.service.SchoolService#updateSchool(Long, SchoolRequestDtoUpdate)
 */

@Getter
@Setter
public class SchoolRequestDtoUpdate {
    @Schema(description = "Nombre de la escuela a la que asiste el paciente", example = "Instituto Modelo", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre de la escuela es obligatorio")
    private String schoolName;


    @NotBlank(message = "El email de la escuela es obligatorio")
    @Email(message = "El email debe tener un formato válido. Ejemplo = rayuela@gmail.com")
    private String emailSchool;

    @Schema(description = "El teléfono está pensado en formato argentino; se permite un mínimo de 8 dígitos en" +
            " caso de no incluir el prefijo 011.", example = "02320484070, 1155150791, 01144697500", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^\\d{8,11}$", message = "El teléfono debe tener entre 8 y 11 dígitos numéricos")
    @NotBlank(message = "El teléfono de la escuela es obligatorio")
    private String phoneSchool;


}