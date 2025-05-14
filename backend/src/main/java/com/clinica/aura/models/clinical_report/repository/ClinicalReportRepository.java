package com.clinica.aura.models.clinical_report.repository;

import com.clinica.aura.models.clinical_report.model.ClinicalReportModel;
import com.clinica.aura.models.medical_background.model.MedicalBackgroundModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicalReportRepository extends JpaRepository<ClinicalReportModel, Long> {

}