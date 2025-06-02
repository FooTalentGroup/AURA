package com.clinica.aura.modules.school.repository;


import com.clinica.aura.modules.school.model.SchoolModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<SchoolModel, Long>{
    /**
     * Busca una escuela por su correo electrónico.
     * Este método se utiliza al momento de crear una nueva escuela,
     * para validar que no exista ya una registrada con el mismo correo.
     * @param emailSchool Correo electrónico de la escuela a buscar.
     * @return Un {@link Optional} con la escuela encontrada, si existe.
     */
    Optional<SchoolModel> findByEmailSchool(String emailSchool);

}
