package com.clinica.aura.models.professional.model;

import com.clinica.aura.models.patient.model.PatientModel;
import com.clinica.aura.models.person.model.PersonModel;
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

    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId
    @JoinColumn(name = "id")
    private PersonModel person;

    @Column(nullable = false)
    private String licenseNumber;

    private String specialty;

    @ManyToMany(targetEntity = PatientModel.class, mappedBy = "professionals", fetch = FetchType.LAZY)
    private List<PatientModel> patients;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
