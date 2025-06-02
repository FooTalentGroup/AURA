package com.clinica.aura.modules.follow_up_entries.dtoResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para representar la respuesta de una entrada de seguimiento.
 * Contiene los datos necesarios que se devuelven al cliente tras crear, consultar o actualizar una entrada.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowUpEntriesDtoResponse {

    /**
     * Identificador único de la entrada de seguimiento.
     */
    private Long id;

    /**
     * Observaciones registradas en la entrada de seguimiento.
     */
    private String observations;

    /**
     * Intervenciones realizadas durante la sesión.
     */
    private String interventions;

    /**
     * Instrucciones para la próxima sesión del paciente.
     */
    private String nextSessionInstructions;

    /**
     * Fecha y hora de creación de la entrada, con formato yyyy-MM-dd HH:mm:ss.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización de la entrada, con formato yyyy-MM-dd HH:mm:ss.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * Identificador del profesional que creó o actualizó la entrada.
     */
    private Long professionalId;

    /**
     * Identificador de la historia clínica asociada a esta entrada.
     */
    private Long medicalRecordId;
}
