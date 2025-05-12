package com.clinica.aura.models.medical_records.repository;

import com.clinica.aura.models.medical_records.model.MedicalRecordsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordsRepository extends JpaRepository<MedicalRecordsModel,Long> {
    @Modifying
    @Query("DELETE FROM MedicalRecordsModel m WHERE m.patients.id = :patientId")
    void deleteByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT COUNT(m) > 0 FROM MedicalRecordsModel m WHERE m.patients.id = :patientId")
    boolean existsByPatientId(Long patientId);

    Optional<MedicalRecordsModel> findByPatientsId(Long patientId);

    List<MedicalRecordsModel> findAllByOrderByCreatedAtAsc();


    @Query("""
    SELECT mr FROM MedicalRecordsModel mr
    WHERE (:specialty IS NULL OR mr.createdBy.specialty = :specialty)
    AND (:date IS NULL OR FUNCTION('DATE', mr.createdAt) = :date)
    AND (:professionalName IS NULL OR 
         LOWER(CONCAT(mr.createdBy.person.name, ' ', mr.createdBy.person.lastName)) LIKE LOWER(:professionalName)
    )
    """)
    List<MedicalRecordsModel> filterClinicalHistory(
            @Param("specialty") String specialty,
            @Param("date") LocalDate date,
            @Param("professionalName") String professionalName
    );



}
