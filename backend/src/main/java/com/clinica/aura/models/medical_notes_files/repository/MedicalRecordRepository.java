package com.clinica.aura.models.medical_notes_files.repository;

import com.clinica.aura.models.medical_notes_files.dto.MedicalRecordReporteDTO;
import com.clinica.aura.models.medical_records.model.MedicalRecordsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecordsModel, Long> {

    @Query(value = """
        SELECT
          p.id AS personId,
          p.last_name AS lastName,
          p.name AS name,
          p.dni AS dni,
          p.phone_number AS phoneNumber,
          p.birth_date AS birthDate,
          pat.relation_to_patient AS relationToPatient,
          pat.tutor_name AS tutorName,
          u.email AS email,
          prof.id AS professionalId,
          pp.name AS professionalName,
          pp.last_name AS professionalLastName,
          prof.specialty AS specialty,
          mr.id AS medicalRecordId,
          fue.date AS followUpDate,
          fue.notes AS followUpNotes
        FROM person p
        JOIN patients pat ON pat.id = p.id
        LEFT JOIN users u ON u.person_id = p.id
        JOIN medical_records mr ON mr.patient_id = p.id
        JOIN professional prof ON prof.id = mr.created_by_professional_id
        JOIN person pp ON pp.id = prof.id
        LEFT JOIN follow_up_entries fue ON fue.medical_record_id = mr.id
        WHERE p.dni = :dni
        """, nativeQuery = true)
    List<MedicalRecordReporteDTO.MedicalRecordReportDTO> findReportByDni(@Param("dni") String dni);
}