package com.clinica.aura.entities.patient.repository;

import com.clinica.aura.entities.patient.model.PatientModel;
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

    //se usa en el metodo buscar por nombre
    List<PatientModel> findByPerson_NameContainingIgnoreCase(String name);



}
