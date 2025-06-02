package com.clinica.aura.modules.person.repository;

import com.clinica.aura.modules.person.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonModel, Long> {
    @Query("SELECT p.birthDate FROM PersonModel p WHERE p.id = :id")
    LocalDate findBirthDateById(@Param("id") Long id);

    @Query("SELECT p.birthDate FROM PersonModel p WHERE p.dni = :dni")
    LocalDate findBirthDateByDni(@Param("dni") String dni);

    Optional<PersonModel> findByDni(String dni);

}
