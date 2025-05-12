package com.clinica.aura.models.medical_background.model;


import com.clinica.aura.models.clinical_report.model.ClinicalReportModel;
import com.clinica.aura.models.professional.model.ProfessionalModel;
import jakarta.persistence.*;
import lombok.*;
import com.clinica.aura.models.patient.model.PatientModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "medical_background")
public class MedicalBackgroundModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación uno a uno con el paciente
    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientModel patient;

    // Relación uno a muchos con informes clínicos
    @OneToMany(mappedBy = "medicalBackground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicalReportModel> clinicalReports;

    // Otros campos
    @Column(columnDefinition = "TEXT")
    private String schoolReports;

    // Lista simple de alergias
    @ElementCollection
    @CollectionTable(name = "medical_background_allergies", joinColumns = @JoinColumn(name = "medical_background_id"))
    @Column(name = "allergy")
    private List<String> allergies;

    // Lista simple de discapacidades
    @ElementCollection
    @CollectionTable(name = "medical_background_disabilities", joinColumns = @JoinColumn(name = "medical_background_id"))
    @Column(name = "disability")
    private List<String> disabilities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


// Relaciones con ProfessionalModel (creado por / actualizado por)
@ManyToOne
@JoinColumn(name = "created_by")
private ProfessionalModel createdBy;

@ManyToOne
@JoinColumn(name = "updated_by")
private ProfessionalModel updatedBy;

}
