package com.clinica.aura.models.receptionist.repository;

import com.clinica.aura.models.receptionist.model.ReceptionistModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionistRepository extends JpaRepository <ReceptionistModel, Long> {
}
