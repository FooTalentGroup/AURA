package com.clinica.aura.modules.school.model;

import com.clinica.aura.modules.patient.model.PatientModel;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una institución educativa asociada a uno o más pacientes.
 * Esta entidad se utiliza para registrar y gestionar información básica sobre escuelas.
 * Su uso es referencial en otras entidades como {@link PatientModel}, que puede vincularse a una escuela específica.
 *
 * Atributos principales:
 * - schoolName: nombre de la escuela.
 * - emailSchool: correo electrónico institucional, utilizado para validaciones únicas.
 * - phoneSchool: número de contacto telefónico.
 * La entidad se encuentra mapeada a la tabla "schools" y su ID es generado automáticamente.
 */
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

    @Column(name =" email_school")
    private String emailSchool;

    @Column(name =" phone_school")
    private String phoneSchool;

}
