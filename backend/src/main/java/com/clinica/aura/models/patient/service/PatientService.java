package com.clinica.aura.models.patient.service;

import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.exceptions.EmailAlreadyExistsException;
import com.clinica.aura.models.medical_records.repository.MedicalRecordsRepository;
import com.clinica.aura.models.patient.dto.PatientRequestDto;
import com.clinica.aura.models.patient.dto.PatientResponseDto;
import com.clinica.aura.models.patient.model.PatientModel;
import com.clinica.aura.models.patient.repository.PatientRepository;
import com.clinica.aura.models.person.model.PersonModel;
import com.clinica.aura.models.person.repository.PersonRepository;
import com.clinica.aura.models.professional.model.ProfessionalModel;
import com.clinica.aura.models.professional.repository.ProfessionalRepository;
import com.clinica.aura.models.school.model.SchoolModel;
import com.clinica.aura.models.school.repository.SchoolRepository;
import com.clinica.aura.models.user_account.Enum.EnumRole;
import com.clinica.aura.models.user_account.models.RoleModel;
import com.clinica.aura.models.user_account.models.UserModel;
import com.clinica.aura.models.user_account.repository.RoleRepository;
import com.clinica.aura.models.user_account.repository.UserRepository;
import com.clinica.aura.util.PaginatedResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;

    private final PersonRepository personRepository;//n
    private final MedicalRecordsRepository medicalRecordsRepository;
    private final ProfessionalRepository professionalRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SchoolRepository schoolRepository;


    @Transactional
    public PatientResponseDto createUser(@Valid PatientRequestDto authCreateUserDto) {

        String email = authCreateUserDto.getEmail();
        String username = authCreateUserDto.getName();
        String lastName = authCreateUserDto.getLastName();
        String dni = authCreateUserDto.getDni();
        String phoneNumber = authCreateUserDto.getPhoneNumber();
        LocalDate birthDate = authCreateUserDto.getBirthDate();
        String address = authCreateUserDto.getAddress();
        String tutorName = authCreateUserDto.getTutorName();
        String relationToPatient = authCreateUserDto.getRelationToPatient();
        String level = authCreateUserDto.getLevel();
        String shift = authCreateUserDto.getShift();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("El correo " + email + " ya existe en la base de datos.");
        }

        Optional<RoleModel> professionalRole = roleRepository.findByEnumRole(EnumRole.PATIENT);
        if (professionalRole.isEmpty()) {
            throw new IllegalArgumentException("El rol especificado no está configurado en la base de datos.");
        }
        Set<RoleModel> roleEntities = Set.of(professionalRole.get());

        PersonModel personEntity = PersonModel.builder()
                .dni(dni)
                .name(username)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .build();

        PatientModel patientModel = PatientModel.builder()
                .person(personEntity)
                .hasInsurance(authCreateUserDto.isHasInsurance())
                .insuranceName(authCreateUserDto.getInsuranceName())
                .address(address)
                .tutorName(tutorName)
                .relationToPatient(relationToPatient)
                .level(level)
                .shift(shift)
                .build();

        List<Long> profIds = authCreateUserDto.getProfessionalIds();
        if (profIds != null && !profIds.isEmpty()) {
            List<ProfessionalModel> professionals = professionalRepository.findAllById(profIds);
            List<Long> existingProfIds = professionals.stream().map(ProfessionalModel::getId).toList();
            List<Long> nonExistingProfIds = new ArrayList<>(profIds);
            nonExistingProfIds.removeAll(existingProfIds);
            if (!nonExistingProfIds.isEmpty()) {
                throw new EntityNotFoundException("Los siguientes profesionales no fueron encontrados: " + nonExistingProfIds);
            }
            patientModel.setProfessionals(professionals);
        } else {
            patientModel.setProfessionals(new ArrayList<>());
        }

        Long schoolId = authCreateUserDto.getSchoolId();
        if (schoolId == null) {
            throw new IllegalArgumentException("El ID de la escuela es obligatorio.");
        }

        SchoolModel school = entityManager.find(SchoolModel.class, schoolId);
        if (school == null) {
            throw new EntityNotFoundException("La escuela con ID " + schoolId + " no fue encontrada.");
        }
        patientModel.setSchoolModel(school);

        patientRepository.save(patientModel);

        UserModel userEntity = UserModel.builder()
                .email(email)
                .password("")
                .roles(roleEntities)
                .person(personEntity)
                .build();

        userRepository.save(userEntity);

        return PatientResponseDto.builder()
                .id(personEntity.getId())
                .email(userEntity.getEmail())
                .name(personEntity.getName())
                .lastName(personEntity.getLastName())
                .phoneNumber(personEntity.getPhoneNumber())
                .birthDate(personEntity.getBirthDate())
                .dni(personEntity.getDni())
                .hasInsurance(patientModel.getInsuranceName() != null && !patientModel.getInsuranceName().isBlank())
                .insuranceName(patientModel.getInsuranceName())
                .address(patientModel.getAddress())
                .tutorName(patientModel.getTutorName())
                .relationToPatient(patientModel.getRelationToPatient())
                .level(patientModel.getLevel())
                .shift(patientModel.getShift())
                .professionalIds(
                        patientModel.getProfessionals() != null
                                ? patientModel.getProfessionals().stream().map(ProfessionalModel::getId).toList()
                                : Collections.emptyList()
                )
                .schoolId(school.getId())
                .build();
    }



    //LISTADO DE PACIENTES NUEVO
    public PaginatedResponse<PatientResponseDto> getAllPatients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientModel> patientsPage = patientRepository.findAll(pageable);

        List<PatientResponseDto> patientResponseDtos = patientsPage.getContent().stream()
                .map(patient -> {
                    PersonModel person = patient.getPerson();
                    Optional<UserModel> userOptional = userRepository.findByPerson(person);

                    PatientResponseDto.PatientResponseDtoBuilder dtoBuilder = PatientResponseDto.builder()
                            .id(patient.getId())
                            .name(person.getName())
                            .lastName(person.getLastName())
                            .phoneNumber(person.getPhoneNumber())
                            .birthDate(person.getBirthDate())
                            .dni(person.getDni())
                            .email(userOptional.map(UserModel::getEmail).orElse(null))
                            .hasInsurance(patient.isHasInsurance())
                            .insuranceName(patient.getInsuranceName())
                            .address(patient.getAddress())
                            .tutorName(patient.getTutorName())
                            .relationToPatient(patient.getRelationToPatient())
                            .level(patient.getLevel())
                            .shift(patient.getShift());

                    if (patient.getSchoolModel() != null) {
                        dtoBuilder.schoolId(patient.getSchoolModel().getId());
                    }

                    if (patient.getProfessionals() != null) {
                        List<Long> professionalIds = patient.getProfessionals().stream()
                                .map(ProfessionalModel::getId)
                                .toList();
                        dtoBuilder.professionalIds(professionalIds);
                    }

                    return dtoBuilder.build();
                })
                .toList();

        return new PaginatedResponse<>(
                patientResponseDtos,
                patientsPage.getNumber(),
                patientsPage.getSize(),
                patientsPage.getTotalPages(),
                patientsPage.getTotalElements()
        );
    }


    //buscar pacientes por id
    public PatientResponseDto getPatientById(Long id) {
        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        var person = patient.getPerson();

        // busca el usuario a partir de la persona
        var user = userRepository.findByPerson(person).orElse(null);

        List<Long> professionalIds = null;
        if (patient.getProfessionals() != null) {
            professionalIds = patient.getProfessionals().stream()
                    .map(prof -> prof.getId())
                    .toList();
        }

        PatientResponseDto.PatientResponseDtoBuilder dtoBuilder = PatientResponseDto.builder()
                .id(person.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .birthDate(person.getBirthDate())
                .dni(person.getDni())
                .email(user != null ? user.getEmail() : null)
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .address(patient.getAddress())
                .tutorName(patient.getTutorName())
                .relationToPatient(patient.getRelationToPatient())
                .professionalIds(professionalIds)
                .level(patient.getLevel())
                .shift(patient.getShift());

        if (patient.getSchoolModel() != null) {
            dtoBuilder.schoolId(patient.getSchoolModel().getId());
        }

        return dtoBuilder.build();
    }


    //editar paciente por id
    public PatientResponseDto updatePatient(Long id, PatientRequestDto requestDto) {
        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        var person = patient.getPerson();

        // se actualizan datos de la persona
        person.setName(requestDto.getName());
        person.setLastName(requestDto.getLastName());
        person.setPhoneNumber(requestDto.getPhoneNumber());
        person.setDni(requestDto.getDni());
        person.setBirthDate(requestDto.getBirthDate());

        //se  actualizan datos del paciente
        patient.setInsuranceName(requestDto.getInsuranceName());
        patient.setHasInsurance(requestDto.isHasInsurance());
        patient.setAddress(requestDto.getAddress());
        patient.setTutorName(requestDto.getTutorName());
        patient.setRelationToPatient(requestDto.getRelationToPatient());
        patient.setLevel(requestDto.getLevel());
        patient.setShift(requestDto.getShift());

        if (requestDto.getSchoolId() == null) {
            throw new IllegalArgumentException("El ID de la escuela no puede ser null.");
        }
        //busca la escuela por ID y asignarla
        SchoolModel school = schoolRepository.findById(requestDto.getSchoolId())
                .orElseThrow(() -> new RuntimeException("Escuela no encontrada con ID: " + requestDto.getSchoolId()));
        patient.setSchoolModel(school);

        List<Long> profIds = requestDto.getProfessionalIds();
        if (profIds != null && !profIds.isEmpty()) {
            List<ProfessionalModel> professionals = professionalRepository.findAllById(profIds);
            List<Long> existingProfIds = professionals.stream().map(ProfessionalModel::getId).toList();
            List<Long> nonExistingProfIds = new ArrayList<>(profIds);
            nonExistingProfIds.removeAll(existingProfIds);
            if (!nonExistingProfIds.isEmpty()) {
                throw new EntityNotFoundException("Los siguientes profesionales no fueron encontrados: " + nonExistingProfIds);
            }
            patient.setProfessionals(professionals);
        } else {
            patient.setProfessionals(new ArrayList<>());
        }


        // Actualizar solo el email en la tabla usuario
        Optional<UserModel> optionalUser = userRepository.findByPerson(person);
        UserModel user = optionalUser.orElseThrow(() -> new RuntimeException("Usuario no encontrado para el paciente con ID: " + id));

        user.setEmail(requestDto.getEmail());

        // Guardar cambios
        patientRepository.save(patient);
        userRepository.save(user); // importante guardar el user también

        // traer los ids de los profesionales
        List<Long> professionalIds = null;
        if (patient.getProfessionals() != null) {
            professionalIds = patient.getProfessionals().stream()
                    .map(prof -> prof.getId())
                    .toList();
        }

        return PatientResponseDto.builder()
                .id(person.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .birthDate(person.getBirthDate())
                .dni(person.getDni())
                .email(user.getEmail())
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .address(patient.getAddress())
                .tutorName(patient.getTutorName())
                .relationToPatient(patient.getRelationToPatient())
                .professionalIds(professionalIds)
                .level(patient.getLevel())
                .shift(patient.getShift())
                .schoolId(patient.getSchoolModel().getId())
                .build();
    }

    //eliminar paciente por id
    @Transactional
    public void deletePatientById(Long patientId) {
        PatientModel patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        Long personId = patient.getId();

        // Elimina la relacion con profesionales
        patientRepository.deletePatientProfessionalRelation(patientId);

        // Elimina el usuario y sus roles
        Optional<UserModel> userOpt = userRepository.findByPersonId(personId);
        userOpt.ifPresent(user -> {
            patientRepository.deleteUserRolesByUserId(user.getId());
            patientRepository.deleteUserById(user.getId());
        });

        //Elimina paciente y persona
        patientRepository.deletePatientByIdNative(patientId);
        patientRepository.deletePersonById(personId);
    }

    //buscar paciente por dni exacto
    public PatientResponseDto getPatientByDni(String dni) {
        var patient = patientRepository.findByPersonDni(dni)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con DNI: " + dni));

        var person = patient.getPerson();
        var user = userRepository.findByPerson(person).orElse(null);

        List<Long> professionalIds = null;
        if (patient.getProfessionals() != null) {
            professionalIds = patient.getProfessionals().stream()
                    .map(prof -> prof.getId())
                    .toList();
        }

        Long schoolId = patient.getSchoolModel() != null ? patient.getSchoolModel().getId() : null;

        return PatientResponseDto.builder()
                .id(patient.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .birthDate(person.getBirthDate())
                .dni(person.getDni())
                .email(user != null ? user.getEmail() : null)
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .address(patient.getAddress())
                .tutorName(patient.getTutorName())
                .relationToPatient(patient.getRelationToPatient())
                .professionalIds(professionalIds)
                .level(patient.getLevel())
                .shift(patient.getShift())
                .schoolId(schoolId)
                .build();
    }


    //buscar paciente por nombre
    public List<PatientResponseDto> getPatientsByName(String name, String sureName) {
        List<PatientModel> patients = patientRepository.searchByFullName(name, sureName);

        if (patients.isEmpty()) {
            throw new RuntimeException("No se encontraron pacientes con el nombre: " + name);
        }

        return patients.stream().map(patient -> {
            PersonModel person = patient.getPerson();
            var user = userRepository.findByPerson(person).orElse(null);

            List<Long> professionalIds = null;
            if (patient.getProfessionals() != null) {
                professionalIds = patient.getProfessionals().stream()
                        .map(prof -> prof.getId())
                        .toList();
            }

            Long schoolId = patient.getSchoolModel() != null ? patient.getSchoolModel().getId() : null;

            return PatientResponseDto.builder()
                    .id(patient.getId())
                    .name(person.getName())
                    .lastName(person.getLastName())
                    .phoneNumber(person.getPhoneNumber())
                    .birthDate(person.getBirthDate())
                    .dni(person.getDni())
                    .email(user != null ? user.getEmail() : null)
                    .insuranceName(patient.getInsuranceName())
                    .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                    .address(patient.getAddress())
                    .tutorName(patient.getTutorName())
                    .relationToPatient(patient.getRelationToPatient())
                    .professionalIds(professionalIds)
                    .level(patient.getLevel())
                    .shift(patient.getShift())
                    .schoolId(schoolId)
                    .build();
        }).toList();
    }


}





