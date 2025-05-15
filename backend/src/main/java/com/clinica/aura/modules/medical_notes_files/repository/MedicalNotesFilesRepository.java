package com.clinica.aura.modules.medical_notes_files.repository;

import com.clinica.aura.modules.medical_notes_files.dto.MedicalNotesFilesResponseDTO;
import com.clinica.aura.modules.medical_notes_files.model.MedicalNotesFilesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Recupera un conjunto de datos médicos completos del paciente identificado por su DNI.
 * Esta consulta utiliza SQL nativo para unir múltiples tablas relacionadas con pacientes,
 * usuarios, profesionales, seguimientos e historiales médicos, a fin de construir un reporte
 * detallado que será utilizado para la generación de archivos PDF.
 * Los datos recuperados incluyen:
 * - Datos personales del paciente (nombre, apellido, DNI, teléfono, fecha de nacimiento)
 * - Información del tutor (nombre y relación con el paciente)
 * - Correo electrónico del paciente (si existe)
 * - Datos del profesional responsable de la historia clínica
 * - Datos del profesional que realiza los seguimientos médicos
 * - Detalles de las intervenciones, observaciones e instrucciones para próximas sesiones
 * - Diagnósticos y sus detalles asociados a la historia clínica
 *
 *   se ingresa el DNI del paciente para el cual se desea generar el reporte.
 * @return Una lista de DTOs {@link
 */

@Repository
public interface MedicalNotesFilesRepository extends JpaRepository<MedicalNotesFilesModel, Long> {

    @Query(value = """
            SELECT
         
              p.id AS person_id,
              p.last_name,
              p.name,
              p.dni,
              p.phone_number,
              p.birth_date,
          
              pat.relation_to_patient,
              pat.tutor_name,
             
              u.email,
            
              prof.id AS professional_id,
              pp.name AS professional_name,
              pp.last_name AS professional_last_name,
              prof.specialty,
              
              mr.id AS medical_record_id,
            
              fue.interventions AS interventions,
              fue.next_session_instructions AS next_session_instructions,
              fue.observations as observations,
              fue.created_at as createDate,
            
              prof_fue.name AS follow_up_professional_name,
              prof_fue.last_name AS follow_up_professional_last_name,
             
              d.details AS details,  
              d.title AS diagnostico 
            
            FROM person p
            JOIN patients pat ON pat.id = p.id
            LEFT JOIN users u ON u.person_id = p.id
            JOIN medical_records mr ON mr.patient_id = p.id
            JOIN professional prof ON prof.id = mr.created_by_professional_id
            JOIN person pp ON pp.id = prof.id
            LEFT JOIN follow_up_entries fue ON fue.medical_record_id = mr.id
            LEFT JOIN professional prof_fue_table ON prof_fue_table.id = fue.created_by_professional_id
            LEFT JOIN person prof_fue ON prof_fue.id = prof_fue_table.id
            LEFT JOIN diagnoses d ON d.medical_record_id = mr.id -- JOIN con la tabla de diagnósticos
            
            WHERE p.dni = :dni
        """, nativeQuery = true)
    List<MedicalNotesFilesResponseDTO.MedicalFilesDTO> findReportByDni(@Param("dni") String dni);
}