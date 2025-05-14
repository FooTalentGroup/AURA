package com.clinica.aura.models.medical_background.repository;

import com.clinica.aura.models.follow_up_entries.model.FollowUpEntriesModel;
import com.clinica.aura.models.medical_background.model.MedicalBackgroundModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalBackgroundRepository extends JpaRepository<MedicalBackgroundModel, Long>{
    boolean existsByPatientId(@NotNull(message = "El ID del paciente no puede ser nulo") Long patientId);

    Optional<MedicalBackgroundModel> findByPatientId(Long patientId);

}

