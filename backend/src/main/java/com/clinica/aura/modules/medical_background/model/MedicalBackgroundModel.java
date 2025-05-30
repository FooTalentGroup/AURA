package com.clinica.aura.modules.medical_background.model;



import com.clinica.aura.modules.professional.model.ProfessionalModel;
import jakarta.persistence.*;
import lombok.*;
import com.clinica.aura.modules.patient.model.PatientModel;
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

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientModel patient;

    @ElementCollection
    @CollectionTable(name = "medical_background_allergies", joinColumns = @JoinColumn(name = "medical_background_id"))
    @Column(name = "allergy")
    private List<String> allergies;

    @ElementCollection
    @CollectionTable(name = "medical_background_disabilities", joinColumns = @JoinColumn(name = "medical_background_id"))
    @Column(name = "disability")
    private List<String> disabilities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private ProfessionalModel createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private ProfessionalModel updatedBy;

}
