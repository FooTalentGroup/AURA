package com.clinica.aura.modules.professional.model;

import com.clinica.aura.modules.patient.model.PatientModel;
import com.clinica.aura.modules.person.model.PersonModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Where(clause = "deleted = false")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "professional")
public class ProfessionalModel {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId
    @JoinColumn(name = "id")
    private PersonModel person;

    @Column(nullable = false)
    private String licenseNumber;

    private String specialty;

    @ManyToMany(targetEntity = PatientModel.class, mappedBy = "professionals", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<PatientModel> patients;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
