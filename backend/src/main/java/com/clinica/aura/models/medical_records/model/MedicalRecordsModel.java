package com.clinica.aura.models.medical_records.model;

import com.clinica.aura.models.patient.model.PatientModel;
import com.clinica.aura.models.professional.model.ProfessionalModel;
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
@Table(name = "medical_records")
public class MedicalRecordsModel {
    //historia clinicas
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = PatientModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientModel patients;

    @ManyToOne(targetEntity = ProfessionalModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_professional_id", nullable = false)
    private ProfessionalModel createdBy;

    @ManyToOne(targetEntity = ProfessionalModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_professional_id", nullable = false)
    private ProfessionalModel updatedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
