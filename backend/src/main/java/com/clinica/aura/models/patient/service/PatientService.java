package com.clinica.aura.models.patient.service;

import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.exceptions.*;
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

import java.time.Period;
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
        String genre = authCreateUserDto.getGenre();
        String insurancePlan = authCreateUserDto.getInsurancePlan();
        String memberShipNumer = authCreateUserDto.getMemberShipNumber();

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
                .insurancePlan(authCreateUserDto.getInsurancePlan())
                .address(address)
                .tutorName(tutorName)
                .relationToPatient(relationToPatient)
                .genre(genre)
                .memberShipNumber(memberShipNumer)
                .build();



        List<Long> profIds = authCreateUserDto.getProfessionalIds();
        if (profIds != null && !profIds.isEmpty()) {
            List<ProfessionalModel> professionals = professionalRepository.findAllById(profIds);
            List<Long> existingProfIds = professionals.stream().map(ProfessionalModel::getId).toList();
            List<Long> nonExistingProfIds = new ArrayList<>(profIds);
            nonExistingProfIds.removeAll(existingProfIds);
            if (!nonExistingProfIds.isEmpty()) {
                throw new ProfessionalNotFoundException("Los siguientes profesionales no fueron encontrados: " + nonExistingProfIds);
            }
            patientModel.setProfessionals(professionals);
        } else {
            patientModel.setProfessionals(new ArrayList<>());
        }

        Long schoolId = authCreateUserDto.getSchoolId();
        SchoolModel school = null;

        if (schoolId != null) {
            school = entityManager.find(SchoolModel.class, schoolId);
            if (school == null) {
                throw new SchoolNotFoundException("La escuela con ID " + schoolId + " no fue encontrada.");
            }
        }
        patientModel.setSchoolModel(school);

        patientRepository.save(patientModel);

        int currentAge= calculatorAge(patientModel.getId(), birthDate);

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
                .insurancePlan(patientModel.getInsurancePlan())
                .memberShipNumber(patientModel.getMemberShipNumber())
                .address(patientModel.getAddress())
                .tutorName(patientModel.getTutorName())
                .relationToPatient(patientModel.getRelationToPatient())
                .genre(patientModel.getGenre())
                .professionalIds(
                        patientModel.getProfessionals() != null
                                ? patientModel.getProfessionals().stream().map(ProfessionalModel::getId).toList()
                                : Collections.emptyList()
                )
                .schoolId(patientModel.getSchoolModel() != null ? patientModel.getSchoolModel().getId() : null)
                .age(currentAge)
                .build();
    }

    //Asignar en ael atributo schoolId de paciente el id de la escuela
    public void assignSchoolToPatient(Long patientId, Long schoolId) {
        PatientModel patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + patientId));
        SchoolModel school = entityManager.find(SchoolModel.class, schoolId);
        if (school == null) {
            throw new SchoolNotFoundException("La escuela con ID " + schoolId + " no fue encontrada.");
        }
        patient.setSchoolModel(school);
        patientRepository.save(patient);
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
                            .insurancePlan(patient.getInsurancePlan())
                            .memberShipNumber(patient.getMemberShipNumber())
                            .address(patient.getAddress())
                            .tutorName(patient.getTutorName())
                            .relationToPatient(patient.getRelationToPatient())
                            .genre(patient.getGenre())
                            .age(calculateAgeFromBirthDate(person.getBirthDate()));



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
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + id));

        var person = patient.getPerson();

        //trae el id
        Long idPatient = patient.getId();
        //busca la fecha de nacimiento en base al id
        LocalDate birthDate = personRepository.findBirthDateById(id);

        int currentAge= calculatorAge(idPatient, birthDate);

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
                .insurancePlan(patient.getInsurancePlan())
                .memberShipNumber(patient.getMemberShipNumber())
                .address(patient.getAddress())
                .tutorName(patient.getTutorName())
                .relationToPatient(patient.getRelationToPatient())
                .professionalIds(professionalIds)
                .age(currentAge)
                .genre(patient.getGenre());


        if (patient.getSchoolModel() != null) {
            dtoBuilder.schoolId(patient.getSchoolModel().getId());
        }

        return dtoBuilder.build();
    }


    //editar paciente por id
    public PatientResponseDto updatePatient(Long id, PatientRequestDto requestDto) {
        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + id));

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
        patient.setInsurancePlan(requestDto.getInsurancePlan());
        patient.setMemberShipNumber(requestDto.getMemberShipNumber());
        patient.setAddress(requestDto.getAddress());
        patient.setTutorName(requestDto.getTutorName());
        patient.setRelationToPatient(requestDto.getRelationToPatient());
        patient.setGenre(requestDto.getGenre());

        if (requestDto.getSchoolId() != null) {
            SchoolModel school = schoolRepository.findById(requestDto.getSchoolId())
                    .orElseThrow(() -> new SchoolNotFoundException("Escuela no encontrada con ID: " + requestDto.getSchoolId()));
            patient.setSchoolModel(school);
        }else{
            patient.setSchoolModel(null);
        }
        //busca la escuela por ID y asignarla


        List<Long> profIds = requestDto.getProfessionalIds();
        if (profIds != null && !profIds.isEmpty()) {
            List<ProfessionalModel> professionals = professionalRepository.findAllById(profIds);
            List<Long> existingProfIds = professionals.stream().map(ProfessionalModel::getId).toList();
            List<Long> nonExistingProfIds = new ArrayList<>(profIds);
            nonExistingProfIds.removeAll(existingProfIds);
            if (!nonExistingProfIds.isEmpty()) {
                throw new ProfessionalNotFoundException("Los siguientes profesionales no fueron encontrados: " + nonExistingProfIds);
            }
            patient.setProfessionals(professionals);
        } else {
            patient.setProfessionals(new ArrayList<>());
        }


        // Actualizar solo el email en la tabla usuario
        Optional<UserModel> optionalUser = userRepository.findByPerson(person);
        UserModel user = optionalUser.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado para el paciente con ID: " + id));

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

        //trae el id del paciente
        Long idPatient = patient.getId();
        //busca la fecha de nacimiento en base al id
        LocalDate birthDate = personRepository.findBirthDateById(id);

        int currentAge= calculatorAge(idPatient, birthDate);

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
                .insurancePlan(patient.getInsurancePlan())
                .memberShipNumber(patient.getMemberShipNumber())
                .address(patient.getAddress())
                .tutorName(patient.getTutorName())
                .relationToPatient(patient.getRelationToPatient())
                .professionalIds(professionalIds)
                .schoolId(patient.getSchoolModel() != null ? patient.getSchoolModel().getId() : null)
                .genre(patient.getGenre())
                .age(currentAge)
                .build();
    }

    //eliminar paciente por id
    @Transactional
    public void deletePatientById(Long patientId) {
        PatientModel patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + patientId));

        Long personId = patient.getId();

        // Elimina la relacion con profesionales
        patientRepository.deletePatientProfessionalRelation(patientId);

       //Elimina la relación con medical
       // medicalRecordsRepository.deleteByPatientId(patientId);

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
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con DNI: " + dni));

        var person = patient.getPerson();
        var user = userRepository.findByPerson(person).orElse(null);

        List<Long> professionalIds = null;
        if (patient.getProfessionals() != null) {
            professionalIds = patient.getProfessionals().stream()
                    .map(prof -> prof.getId())
                    .toList();
        }

        Long schoolId = patient.getSchoolModel() != null ? patient.getSchoolModel().getId() : null;

        //trae el id
        Long idPatient = patient.getId();
        //busca la fecha de nacimiento en base al id
        LocalDate birthDate = personRepository.findBirthDateByDni(dni);

        int currentAge= calculatorAge(idPatient, birthDate);

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
                .insurancePlan(patient.getInsurancePlan())
                .memberShipNumber(patient.getMemberShipNumber())
                .address(patient.getAddress())
                .tutorName(patient.getTutorName())
                .relationToPatient(patient.getRelationToPatient())
                .professionalIds(professionalIds)
                .genre(patient.getGenre())
                .schoolId(schoolId)
                .age(currentAge)
                .build();
    }


    //buscar paciente por nombre
    public List<PatientResponseDto> getPatientsByName(String name, String sureName) {
        List<PatientModel> patients = patientRepository.searchByFullName(name, sureName);

        if (patients.isEmpty()) {
            throw new PatientNotFoundException("No se encontraron pacientes con el nombre: " + name);
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
                    .insurancePlan(patient.getInsurancePlan())
                    .memberShipNumber(patient.getMemberShipNumber())
                    .address(patient.getAddress())
                    .tutorName(patient.getTutorName())
                    .relationToPatient(patient.getRelationToPatient())
                    .professionalIds(professionalIds)
                    .genre(patient.getGenre())
                    .schoolId(schoolId)
                    .age(calculateAgeFromBirthDate(person.getBirthDate()))
                    .build();
        }).toList();
    }

    private int calculatorAge(Long id, LocalDate age){
        personRepository.findById(id).orElseThrow(()->
                new PatientNotFoundException("El paciente no se encuentra en la base de datos"));
        LocalDate currentDate = LocalDate.now();
        return Period.between(age, currentDate).getYears();
    }

    private int calculateAgeFromBirthDate(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}





