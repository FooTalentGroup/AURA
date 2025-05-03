package com.clinica.aura.models.medical_records.repository;

import com.clinica.aura.models.medical_records.model.MedicalRecordsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicalRecordsRepository extends JpaRepository<MedicalRecordsModel,Long> {
    @Modifying
    @Query("DELETE FROM MedicalRecordsModel m WHERE m.patients.id = :patientId")
    void deleteByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT COUNT(m) > 0 FROM MedicalRecordsModel m WHERE m.patients.id = :patientId")
    boolean existsByPatientId(Long patientId);
}
