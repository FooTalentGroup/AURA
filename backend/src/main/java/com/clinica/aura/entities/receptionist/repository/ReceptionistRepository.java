package com.clinica.aura.entities.receptionist.repository;

import com.clinica.aura.entities.receptionist.model.ReceptionistModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionistRepository extends JpaRepository <ReceptionistModel, Long> {
}
