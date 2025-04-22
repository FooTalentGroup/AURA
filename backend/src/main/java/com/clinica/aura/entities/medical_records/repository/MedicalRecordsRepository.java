package com.clinica.aura.entities.medical_records.repository;

import com.clinica.aura.entities.medical_records.model.MedicalRecordsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordsRepository extends JpaRepository<MedicalRecordsModel,Long> {
}
