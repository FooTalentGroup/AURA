package com.clinica.aura.entities.patient.repository;

import com.clinica.aura.entities.patient.model.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientModel, Long> {

}
