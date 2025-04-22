package com.clinica.aura.entities.person.repository;

import com.clinica.aura.entities.person.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonModel, Long> {
}
