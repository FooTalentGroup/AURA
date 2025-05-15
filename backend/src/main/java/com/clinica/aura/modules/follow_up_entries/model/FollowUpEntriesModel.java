package com.clinica.aura.modules.follow_up_entries.model;

import com.clinica.aura.modules.medical_records.model.MedicalRecordsModel;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "follow_up_entries")
public class FollowUpEntriesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = MedicalRecordsModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name="medical_record_id", nullable = false)
    private MedicalRecordsModel medicals;


    @ManyToOne(targetEntity = ProfessionalModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_professional_id", nullable = false)
    private ProfessionalModel createdBy;

    @ManyToOne(targetEntity = ProfessionalModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_professional_id", nullable = false)
    private ProfessionalModel updatedBy;

    private String observations;

    private String interventions;

    private String nextSessionInstructions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
