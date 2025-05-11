package com.clinica.aura.models.medical_notes_files.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class MedicalRecordReporteDTO {
    public interface MedicalRecordReportDTO {
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
        String getProfessionalName();
        String getProfessionalLastName();
        String getSpecialty();
        Long getMedicalRecordId();
        LocalDate getFollowUpDate();
        String getFollowUpNotes();
    }
}
