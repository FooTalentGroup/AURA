package com.clinica.aura.entities.professional.repository;

import com.clinica.aura.entities.professional.model.ProfessionalModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalRepository extends JpaRepository<ProfessionalModel, Long> {
}
