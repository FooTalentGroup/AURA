package com.clinica.aura.models.medical_notes_files.dto;

import java.time.LocalDate;
/**
 * DTO de respuesta utilizado para representar información detallada sobre notas médicas
 * vinculadas a un paciente, incluyendo datos del profesional responsable de los seguimientos.
 * Esta clase encapsula una interfaz anidada {@link MedicalFilesDTO}, la cual define una proyección
 * para mapear resultados personalizados directamente desde consultas en la capa de persistencia.
 */
public class MedicalNotesFilesResponseDTO {
    /**
     * Proyección de datos utilizada para representar una combinación de información proveniente
     * de distintas entidades: paciente, tutor, profesional y registros médicos.
     * Esta interfaz permite obtener solo los campos necesarios desde la base de datos, sin necesidad de mapear
     * entidades completas.
     */
    public interface MedicalFilesDTO {
        Long getPersonId();
        String getLastName();
        String getName();
        String getDni();
        String getPhoneNumber();
        LocalDate getBirthDate();
        String getRelationToPatient();
        String getTutorName();
        String getEmail();
        Long getProfessionalId();
        String getFollowUpProfessionalName();
        String getFollowUpProfessionalLastName();
        String getSpecialty();
        Long getMedicalRecordId();
        String getInterventions();
        String getNextSessionInstructions();
        String getObservations();
        LocalDate getCreateDate();
        String getDetails();
        String getDiagnostico();
    }
}
