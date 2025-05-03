package com.clinica.aura.entities.patient.model;

import com.clinica.aura.entities.person.model.PersonModel;
import com.clinica.aura.entities.professional.model.ProfessionalModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "patients")
public class PatientModel {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)//cascade elimina paciente y persona
    @MapsId
    @JoinColumn(name = "id")
    private PersonModel person;

    private boolean hasInsurance = false;

    private String insuranceName;

    private String school;

    private String paymentType;

    @ManyToMany(targetEntity = ProfessionalModel.class, fetch = FetchType.LAZY)
    @JoinTable(
            name = "professional_patient",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "professional_id")
    )
    private List<ProfessionalModel> professionals;


    @UpdateTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updatedAt;

}
