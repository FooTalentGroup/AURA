package com.clinica.aura.models.diagnoses.repository;

import com.clinica.aura.models.diagnoses.model.DiagnosesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosesRepository extends JpaRepository<DiagnosesModel, Long> {
}
