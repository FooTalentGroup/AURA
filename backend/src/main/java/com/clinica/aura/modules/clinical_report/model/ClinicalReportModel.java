package com.clinica.aura.modules.clinical_report.model;

import com.clinica.aura.modules.medical_background.model.MedicalBackgroundModel;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "clinical_report")
public class ClinicalReportModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate date;

    // Relación con el profesional que hizo el informe
    @ManyToOne
    @JoinColumn(name = "professional_id")
    private ProfessionalModel professional;

    // Relación con antecedentes médicos
    @ManyToOne
    @JoinColumn(name = "medical_background_id")
    private MedicalBackgroundModel medicalBackground;


}