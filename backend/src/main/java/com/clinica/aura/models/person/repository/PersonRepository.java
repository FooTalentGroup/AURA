package com.clinica.aura.models.person.repository;

import com.clinica.aura.models.person.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonModel, Long> {
}
