package com.clinica.aura.models.follow_up_entries.model;

import com.clinica.aura.models.medical_records.model.MedicalRecordsModel;
import com.clinica.aura.models.professional.model.ProfessionalModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
