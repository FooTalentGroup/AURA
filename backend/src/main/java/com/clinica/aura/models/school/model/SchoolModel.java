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

   // private String level; //paso estos 2 atributos a paciente

   // private String shift; //turno

    @Column(name =" school_Representative") //nombre del representante de la escuela
    private String schoolRepresentative;   // puede ser un psicologo o director,
                                         //nunca docentes, me lo confirmo Mayra de ux
    @Column(name =" email_school")
    private String emailSchool;

    @Column(name =" phone_school")
    private String phoneSchool;

    //relacion 1 a n con pacientes
    //una escuela tiene n cantidad des pacientes
//    @OneToMany(targetEntity = PatientModel.class, fetch = FetchType.LAZY, mappedBy = "schoolModel")
//    @JsonManagedReference
//    private List<PatientModel> patients;

    //ME TRAE LOS PACIENTES EN EL EXAMPLE
    //Poniendo hiden en la relacion no aparecen a la hora de editar
}
