package com.clinica.aura.modules.medical_notes_files.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un archivo PDF generado con notas médicas de un paciente.
 * Esta clase se mapea a la tabla {@code medical_notes_files} en la base de datos y
 * almacena metadatos asociados al archivo, como el nombre del archivo, el nombre del paciente
 * y la fecha en que se subió el archivo.
 * Se utiliza principalmente para registrar y rastrear los reportes generados desde el sistema,
 * manteniendo un historial de generación de documentos médicos.
 */
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

