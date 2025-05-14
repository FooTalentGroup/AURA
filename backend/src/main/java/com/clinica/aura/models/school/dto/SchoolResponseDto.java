package com.clinica.aura.models.school.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) utilizado para enviar información detallada de una escuela
 * desde el backend hacia el cliente, generalmente en respuestas HTTP.
 * Contiene datos como el identificador de la escuela, su nombre, email y teléfono institucional.
 * Campos:
 * <ul>
 *     <li><b>id</b>: Identificador único de la escuela (generado automáticamente).</li>
 *     <li><b>schoolName</b>: Nombre de la institución educativa.</li>
 *     <li><b>emailSchool</b>: Correo electrónico institucional de la escuela.</li>
 *     <li><b>phoneSchool</b>: Número de teléfono de contacto de la escuela.</li>
 * </ul>
 */

@Getter
@Setter
@Builder
public class SchoolResponseDto {
    private Long id;
    private String schoolName;
    private String emailSchool;
    private String phoneSchool;

   }
