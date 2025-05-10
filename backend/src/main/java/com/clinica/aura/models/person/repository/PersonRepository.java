package com.clinica.aura.models.person.repository;

import com.clinica.aura.models.person.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PersonRepository extends JpaRepository<PersonModel, Long> {
    //se usa para recuperar la fecha de nacimiento en base al id
    @Query("SELECT p.birthDate FROM PersonModel p WHERE p.id = :id")
    LocalDate findBirthDateById(@Param("id") Long id);

    //se usa para traer la fecha de nacimiento en base al documento
    @Query("SELECT p.birthDate FROM PersonModel p WHERE p.dni = :dni")
    LocalDate findBirthDateByDni(@Param("dni") String dni);

}
