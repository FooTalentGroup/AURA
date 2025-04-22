package com.clinica.aura.entities.professional.repository;

import com.clinica.aura.entities.professional.model.ProfessionalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<ProfessionalModel, Long> {

    List<ProfessionalModel> findByDeletedFalse();
    Optional<ProfessionalModel> findByIdAndDeletedFalse(Long id);
    // busca coincidencias en apellido o especialidad, sin distinguir mayúsculas/minúsculas.
    @Query("SELECT p FROM ProfessionalModel p " +
            "WHERE LOWER(p.person.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.specialty) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ProfessionalModel> searchByLastNameOrSpecialty(@Param("keyword") String keyword);

}
