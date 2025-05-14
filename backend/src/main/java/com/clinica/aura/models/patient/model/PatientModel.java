package com.clinica.aura.models.patient.model;


import com.clinica.aura.models.person.model.PersonModel;
import com.clinica.aura.models.professional.model.ProfessionalModel;
import com.clinica.aura.models.school.model.SchoolModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)//cascade elimina paciente y persona
    @MapsId
    @JoinColumn(name = "id")
    private PersonModel person;

    private boolean hasInsurance; //estaba en false

    private String insuranceName;

     private String address;

    private String tutorName;

    private String relationToPatient;

    private String genre;

    private String insurancePlan;

    private String memberShipNumber;

    @Transient
    private int age;

    @ManyToMany(targetEntity = ProfessionalModel.class, fetch = FetchType.LAZY)
    @JsonManagedReference
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

    @ManyToOne(targetEntity = SchoolModel.class, fetch = FetchType.LAZY)
    private SchoolModel  schoolModel;

}
