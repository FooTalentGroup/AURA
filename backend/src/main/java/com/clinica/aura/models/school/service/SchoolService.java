package com.clinica.aura.models.school.service;

import com.clinica.aura.exceptions.EmailAlreadyExistsException;
import com.clinica.aura.exceptions.SchoolNotFoundException;
import com.clinica.aura.models.patient.dto.PatientResponseDto;
import com.clinica.aura.models.patient.model.PatientModel;
import com.clinica.aura.models.patient.repository.PatientRepository;
import com.clinica.aura.models.person.model.PersonModel;
import com.clinica.aura.models.professional.model.ProfessionalModel;
import com.clinica.aura.models.school.dto.SchoolRequestDto;
import com.clinica.aura.models.school.dto.SchoolRequestDtoUpdate;
import com.clinica.aura.models.school.dto.SchoolResponseDto;
import com.clinica.aura.models.school.model.SchoolModel;
import com.clinica.aura.models.school.repository.SchoolRepository;
import com.clinica.aura.models.user_account.models.UserModel;
import com.clinica.aura.models.user_account.repository.UserRepository;
import com.clinica.aura.util.PaginatedResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolService {

    @Autowired
    PatientRepository patientRepository;



    private final SchoolRepository schoolRepository;

    // Crear una nueva escuela
    @Transactional
    public SchoolResponseDto createSchool(@Valid SchoolRequestDto request) {

        String emailSchool = request.getEmailSchool();

        // Valida si ya existe el email en la tabla de escuelas
        if (schoolRepository.findByEmailSchool(emailSchool).isPresent()) {
            throw new EmailAlreadyExistsException("El correo " + emailSchool + " ya existe en la base de datos de escuelas.");
        }

        SchoolModel school = SchoolModel.builder()
                .schoolName(request.getSchoolName())
                .emailSchool(emailSchool)
                .phoneSchool(request.getPhoneSchool())
                .build();

        SchoolModel savedSchool = schoolRepository.save(school);

        return SchoolResponseDto.builder()
                .id(savedSchool.getId())
                .schoolName(savedSchool.getSchoolName())
                .emailSchool(savedSchool.getEmailSchool())
                .phoneSchool(savedSchool.getPhoneSchool())
                .build();
    }


    // Listar escuelas con paginación
    public PaginatedResponse<SchoolResponseDto> getAllSchools(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SchoolModel> schoolsPage = schoolRepository.findAll(pageable);

        List<SchoolResponseDto> schoolResponseDtos = schoolsPage.getContent().stream()
                .map(school -> SchoolResponseDto.builder()
                        .id(school.getId())
                        .schoolName(school.getSchoolName())
                        .emailSchool(school.getEmailSchool())
                        .phoneSchool(school.getPhoneSchool())
                        .build())
                .toList();

        return new PaginatedResponse<>(
                schoolResponseDtos,
                schoolsPage.getNumber(),
                schoolsPage.getSize(),
                schoolsPage.getTotalPages(),
                schoolsPage.getTotalElements()
        );
    }

    // Actualizar escuela por id
    @Transactional
    public SchoolResponseDto updateSchool(Long id, SchoolRequestDtoUpdate request) {
        SchoolModel school = schoolRepository.findById(id)
                .orElseThrow(() -> new SchoolNotFoundException("Escuela no encontrada con ID: " + id));

        school.setSchoolName(request.getSchoolName());
        school.setEmailSchool(request.getEmailSchool());
        school.setPhoneSchool(request.getPhoneSchool());

        SchoolModel updatedSchool = schoolRepository.save(school);

        return SchoolResponseDto.builder()
                .id(updatedSchool.getId())
                .schoolName(updatedSchool.getSchoolName())
                .emailSchool(updatedSchool.getEmailSchool())
                .phoneSchool(updatedSchool.getPhoneSchool())
                .build();
    }

   //borrar escuela por id
   @Transactional
   public void deleteSchoolById(Long id) {
       if (!schoolRepository.existsById(id)) {
           throw new SchoolNotFoundException("No se encontró una escuela con el ID: " + id);
       }
       schoolRepository.deleteById(id);
   }


}

