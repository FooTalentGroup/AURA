package com.clinica.aura.entities.professional.model;

import com.clinica.aura.entities.person.model.PersonModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "professional")
public class ProfessionalModel {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private PersonModel person;

    @Column(nullable = false)
    private String licenseNumber;

    private String specialty;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
