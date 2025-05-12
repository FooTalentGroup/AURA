package com.clinica.aura.models.medical_notes_files.dto;

import java.time.LocalDate;

public class MedicalNotesFilesResponseDTO {
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
        String getDetails(); // Nuevo campo para los detalles del diagnóstico
        String getDiagnostico(); //Nuevo campo de diagnóstico
    }
}
