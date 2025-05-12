package com.clinica.aura.models.medical_notes_files.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "medical_notes_files")
public class MedicalNotesFilesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String file_name;
    private String patient_name;
    private String patient_last_name;
    private LocalDateTime uploaded_at;
}

