package com.clinica.aura.modules.receptionist.repository;

import com.clinica.aura.modules.receptionist.model.ReceptionistModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionistRepository extends JpaRepository <ReceptionistModel, Long> {
}
