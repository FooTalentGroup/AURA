package com.clinica.aura.modules.diagnoses.repository;

import com.clinica.aura.modules.diagnoses.model.DiagnosesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosesRepository extends JpaRepository<DiagnosesModel, Long> {
}
