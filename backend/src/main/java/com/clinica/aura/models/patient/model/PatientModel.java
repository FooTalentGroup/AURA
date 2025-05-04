package com.clinica.aura.models.patient.model;

import com.clinica.aura.models.person.model.PersonModel;
import com.clinica.aura.models.professional.model.ProfessionalModel;
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

    private LocalDate birthDate; //se agrega la fecha aca en paciente, a pedidos de los mismos 02/05/2025

    //private String paymentType; //campo quitado a pedido de Axel, ux (Tomas) y analista Funcional dieron el ok

    private String address;

    private String tutorName;

    private String relationToPatient;

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
