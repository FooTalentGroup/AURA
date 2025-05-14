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

/**
 * Entidad que representa a un paciente en el sistema.
 * Esta entidad extiende los datos personales básicos desde {@link PersonModel}
 * a través de una relación @OneToOne compartiendo el mismo ID.
 * Atributos destacados:
 * - hasInsurance: indica si tiene cobertura médica.
 * - insuranceName, insurancePlan, memberShipNumber: detalles del seguro médico.
 * - address: dirección del paciente.
 * - tutorName y relationToPatient: datos del adulto responsable.
 * - genre: género del paciente.
 * - age: edad calculada, no persistida( En la base se guarda la fecha de nacimiento, atributo que hereda de personModel).
 * - professionals: lista de profesionales asignados (relación @ManyToMany).
 * - schoolModel: escuela asociada (opcional, @ManyToOne).
 * - createdAt y updatedAt: timestamps de auditoría automática.
 */
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId
    @JoinColumn(name = "id")
    private PersonModel person;

    private boolean hasInsurance;
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
