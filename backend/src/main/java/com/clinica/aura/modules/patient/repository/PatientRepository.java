package com.clinica.aura.modules.patient.repository;

import com.clinica.aura.modules.patient.model.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientModel, Long> {

    // se usa en el metodo buscar por dni
    Optional<PatientModel> findByPersonDni(String dni);

    // se usa para obtener pacientes por el ID de la escuela
    List<PatientModel> findBySchoolModelId(Long schoolId);

    // se usa en el metodo para obtener buscar por nombre
    @Query("""
    SELECT p FROM PatientModel p
    WHERE (
        
        (:kw1 IS NULL OR :kw1 = '' OR
         LOWER(p.person.name) LIKE LOWER(CONCAT('%', :kw1, '%')) OR
         LOWER(p.person.lastName) LIKE LOWER(CONCAT('%', :kw1, '%')) OR
         LOWER(CONCAT(p.person.name, ' ', p.person.lastName)) LIKE LOWER(CONCAT('%', :kw1, '%')) OR
         LOWER(CONCAT(p.person.lastName, ' ', p.person.name)) LIKE LOWER(CONCAT('%', :kw1, '%'))
        )
    )
    AND
    (
       
        (:kw2 IS NULL OR :kw2 = '' OR
         LOWER(p.person.lastName) LIKE LOWER(CONCAT('%', :kw2, '%')) OR
         LOWER(p.person.name) LIKE LOWER(CONCAT('%', :kw2, '%')) OR
         LOWER(CONCAT(p.person.lastName, ' ', p.person.name)) LIKE LOWER(CONCAT('%', :kw2, '%')) OR
         LOWER(CONCAT(p.person.name, ' ', p.person.lastName)) LIKE LOWER(CONCAT('%', :kw2, '%'))
        )
    )
    OR
    (
       
        (:kw1 IS NOT NULL AND :kw1 != '' AND :kw2 IS NOT NULL AND :kw2 != '' AND (
            LOWER(CONCAT(p.person.name, ' ', p.person.lastName)) = LOWER(CONCAT(:kw1, ' ', :kw2)) OR
            LOWER(CONCAT(p.person.lastName, ' ', p.person.name)) = LOWER(CONCAT(:kw2, ' ', :kw1))
        ))
    )
""")
    List<PatientModel> searchByFullName(@Param("kw1") String kw1, @Param("kw2") String kw2);



}
