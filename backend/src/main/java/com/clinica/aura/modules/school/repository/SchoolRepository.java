package com.clinica.aura.modules.school.repository;


import com.clinica.aura.modules.school.model.SchoolModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<SchoolModel, Long>{
   /*se usa en el metodo crear escuela, se valida que no exista este mail en la base de datos
    antes de insertarlo en la nueva escuela que se quiere crear*/
    Optional<SchoolModel> findByEmailSchool(String emailSchool);

}
