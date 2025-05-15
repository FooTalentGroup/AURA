package com.clinica.aura.modules.clinical_report.repository;

import com.clinica.aura.modules.clinical_report.model.ClinicalReportModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicalReportRepository extends JpaRepository<ClinicalReportModel, Long> {

}