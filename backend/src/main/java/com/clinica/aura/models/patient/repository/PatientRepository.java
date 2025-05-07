package com.clinica.aura.models.patient.repository;

import com.clinica.aura.models.patient.model.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientModel, Long> {

    @Modifying
    @Transactional //esta query se usa en el metodo delete paciente/professional_patient
    @Query(value = "DELETE FROM professional_patient WHERE patient_id = :patientId", nativeQuery = true)
    void deletePatientProfessionalRelation(Long patientId);


    @Modifying
    @Transactional     //esta query se usa en  el metodo delete paciente/ users_roles
    @Query(value = "DELETE FROM users_roles WHERE user_id = :userId", nativeQuery = true)
    void deleteUserRolesByUserId(Long userId);

    @Modifying
    @Transactional   //esta query se usa en el metodo delete paciente/users
    @Query(value = "DELETE FROM users WHERE id = :userId", nativeQuery = true)
    void deleteUserById(Long userId);

    @Modifying
    @Transactional   //esta query se usa en el metodo delete paciente/patients
    @Query(value = "DELETE FROM patients WHERE id = :id", nativeQuery = true)
    void deletePatientByIdNative(Long id);

    @Modifying
    @Transactional   //esta query se usa en el metodo delete paciente/person
    @Query(value = "DELETE FROM person WHERE id = :id", nativeQuery = true)
    void deletePersonById(Long id);

    // se usa en el metodo buscar por dni
    Optional<PatientModel> findByPersonDni(String dni);

    // Método para obtener pacientes por el ID de la escuela
    List<PatientModel> findBySchoolModelId(Long schoolId);

    //name o apellido o ambos
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
