package com.clinica.aura.models.school.repository;


import com.clinica.aura.models.patient.model.PatientModel;
import com.clinica.aura.models.school.dto.SchoolResponseDto;
import com.clinica.aura.models.school.model.SchoolModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SchoolRepository extends JpaRepository<SchoolModel, Long>{
   /*se usa en el metodo crear escuela, se valida que no exista este mail en la base de datos
    antes de insertarlo en la nueva escuela que se quiere crear*/
    Optional<SchoolModel> findByEmailSchool(String emailSchool);

}
