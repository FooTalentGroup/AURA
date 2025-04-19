package com.clinica.aura.entities.medical_records.model;

import com.clinica.aura.entities.patient.model.PatientModel;
import com.clinica.aura.entities.person.model.PersonModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "medical_records")
public class MedicalRecordsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private PatientModel person;

    private String notes;

    private String allergies;

    private String previousConditions;

    @UpdateTimestamp
    private LocalDate createdAt;

    @CreationTimestamp
    private LocalDate updatedAt;
}
