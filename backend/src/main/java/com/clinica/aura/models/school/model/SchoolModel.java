package com.clinica.aura.models.school.model;

import com.clinica.aura.models.patient.model.PatientModel;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "schools")
public class SchoolModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name =" school_name")
    private String schoolName;

    @Column(name =" school_Representative")
    private String schoolRepresentative;

    @Column(name =" email_school")
    private String emailSchool;

    @Column(name =" phone_school")
    private String phoneSchool;


}
