package com.clinica.aura.modules.follow_up_entries.model;

import com.clinica.aura.modules.medical_records.model.MedicalRecordsModel;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad que representa una entrada de seguimiento asociada a una historia clínica.
 * Contiene observaciones, intervenciones realizadas e instrucciones para la próxima sesión.
 *
 * Cada entrada está vinculada a un profesional que la creó y otro que la actualizó,
 * así como a una historia clínica específica.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "follow_up_entries")
public class FollowUpEntriesModel {

    /**
     * Identificador único de la entrada de seguimiento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Historia clínica asociada a esta entrada de seguimiento.
     */
    @ManyToOne(targetEntity = MedicalRecordsModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name="medical_record_id", nullable = false)
    private MedicalRecordsModel medicals;

    /**
     * Profesional que creó esta entrada.
     */
    @ManyToOne(targetEntity = ProfessionalModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_professional_id", nullable = false)
    private ProfessionalModel createdBy;

    /**
     * Profesional que realizó la última actualización de esta entrada.
     */
    @ManyToOne(targetEntity = ProfessionalModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_professional_id", nullable = false)
    private ProfessionalModel updatedBy;

    /**
     * Observaciones registradas durante la sesión.
     */
    private String observations;

    /**
     * Intervenciones realizadas al paciente en esta sesión.
     */
    private String interventions;

    /**
     * Instrucciones para la próxima sesión del paciente.
     */
    private String nextSessionInstructions;

    /**
     * Fecha y hora de creación del registro (automática).
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización del registro (automática).
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
