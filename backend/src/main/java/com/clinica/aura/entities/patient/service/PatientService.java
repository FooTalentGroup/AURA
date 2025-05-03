package com.clinica.aura.entities.patient.service;

import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.entities.medical_records.repository.MedicalRecordsRepository;
import com.clinica.aura.entities.patient.dtoRequest.PatientRequestDto;
import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.entities.patient.model.PatientModel;
import com.clinica.aura.entities.patient.repository.PatientRepository;
import com.clinica.aura.entities.person.model.PersonModel;
import com.clinica.aura.entities.person.repository.PersonRepository;
import com.clinica.aura.entities.professional.model.ProfessionalModel;
import com.clinica.aura.entities.professional.repository.ProfessionalRepository;
import com.clinica.aura.entities.receptionist.dtoResponse.ReceptionistResponseDto;
import com.clinica.aura.entities.receptionist.model.ReceptionistModel;
import com.clinica.aura.entities.user_account.Enum.EnumRole;
import com.clinica.aura.entities.user_account.dtoResponse.AuthResponseRegisterDto;
import com.clinica.aura.entities.user_account.models.RoleModel;
import com.clinica.aura.entities.user_account.models.UserModel;
import com.clinica.aura.entities.user_account.repository.RoleRepository;
import com.clinica.aura.entities.user_account.repository.UserRepository;
import com.clinica.aura.util.PaginatedResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    public PatientResponseDto createUser(@Valid PatientRequestDto authCreateUserDto) {

        String email = authCreateUserDto.getEmail();
        String username = authCreateUserDto.getName();
        String lastName = authCreateUserDto.getLastName();
        String dni = authCreateUserDto.getDni();
        String phoneNumber = authCreateUserDto.getPhoneNumber();
        // String country = authCreateUserDto.getCountry(); //02/05/2025 campo que se pide eliminar
        //  String photoUrl = authCreateUserDto.getPhotoUrl(); //02/05/2025 campo que se pide eliminar
        LocalDate birthDate = authCreateUserDto.getBirthDate();

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
//                .country(country)  //02/05/2025 campo que se pide eliminar
//                .birthDate(birthDate) //02/05/2025 campo que se pide eliminar
//                .photoUrl(photoUrl) //02/05/2025 campo que se pide eliminar
                .build();

        PatientModel patientModel = PatientModel.builder()
                .person(personEntity)
                .hasInsurance(authCreateUserDto.isHasInsurance())
                .insuranceName(authCreateUserDto.getInsuranceName())
                .school(authCreateUserDto.getSchool())
                .birthDate(authCreateUserDto.getBirthDate())
                //  .paymentType(authCreateUserDto.getPaymentType()) 02/05/2025
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
        }else {
            patientModel.setProfessionals(new ArrayList<>());
        }

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
                //    .country(personEntity.getCountry()) //02/05/2025  pide quitar este campo pm, ux y analista dan okey
                //    .photoUrl(personEntity.getPhotoUrl())  //02/05/2025  pide quitar este campo
                .birthDate(patientModel.getBirthDate()) //02/05/25 se pide que fecha solo este en paciente
                .dni(personEntity.getDni())
                .hasInsurance(patientModel.getInsuranceName() != null && !patientModel.getInsuranceName().isBlank())
                .insuranceName(patientModel.getInsuranceName())
                .school(patientModel.getSchool())
                //     .paymentType(patientModel.getPaymentType())
                .professionalIds(
                        patientModel.getProfessionals().stream()
                                .map(ProfessionalModel::getId)
                                .toList()
                )
                .build();
    }

//    //listado de pacientes //esto es viejo se puede borrar si se requiere
//    public List<PatientResponseDto> getPatientsByRange(int from, int to) {
//        List<PatientModel> patients = patientRepository.findAll();
//
//        // Validamos el rango
//        if (from < 0) from = 0;
//        if (to >= patients.size()) to = patients.size() - 1;
//        if (from > to) return List.of();
//
//        return patients.subList(from, to + 1).stream().map(patient -> {
//            var person = patient.getPerson();
//            Optional<UserModel> userOptional = userRepository.findByPerson(person);
//
//            return PatientResponseDto.builder()
//                    .id(patient.getId())
//                    .name(person.getName())
//                    .lastName(person.getLastName())
//                    .phoneNumber(person.getPhoneNumber())
//                    .country(person.getCountry())
//                    .photoUrl(person.getPhotoUrl())
//                    .birthDate(person.getBirthDate())
//                    .dni(person.getDni())
//                    .email(userOptional.map(UserModel::getEmail).orElse(null))
//                    .insuranceName(patient.getInsuranceName())
//                    .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
//                    .school(patient.getSchool())
//                    .paymentType(patient.getPaymentType())
//                    .build();
//        }).toList();
//    }

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
                            //  .country(person.getCountry()) // 02/05/2025 pide los mencionados anteriormente que se quite
                            //  .photoUrl(person.getPhotoUrl()) // 02/05/2025 pide los mencionados anteriormente que se quite
                            .birthDate(patient.getBirthDate()) //02/05/2025 a pedido de los mencionados
                            .dni(person.getDni())
                            .email(userOptional.map(UserModel::getEmail).orElse(null))
                            .hasInsurance(patient.isHasInsurance())
                            .insuranceName(patient.getInsuranceName())
                            .school(patient.getSchool());
                    //  .paymentType(patient.getPaymentType());

                    if (patient.getProfessionals() != null) {
                        List<Long> professionalIds = patient.getProfessionals().stream()
                                .map(prof -> prof.getId())
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

        // Buscamos el UserModel a partir del person
        var user = userRepository.findByPerson(person).orElse(null);

        return PatientResponseDto.builder()
                .id(person.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                //  .country(person.getCountry())
                //  .photoUrl(person.getPhotoUrl())
                  .birthDate(patient.getBirthDate())
                .dni(person.getDni())
                .email(user != null ? user.getEmail() : null)
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .school(patient.getSchool())
              //  .paymentType(patient.getPaymentType()) //02/05/2025 campo que se pide eliminar
                .build();
    }

    //editar paciente
    public PatientResponseDto updatePatient(Long id, PatientRequestDto requestDto) {
        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        var person = patient.getPerson();

        // actualizar datos de la persona
        person.setName(requestDto.getName());
        person.setLastName(requestDto.getLastName());
        person.setPhoneNumber(requestDto.getPhoneNumber());
        //  person.setCountry(requestDto.getCountry()); //02/05/2025 campo que se pide eliminar
        //  person.setPhotoUrl(requestDto.getPhotoUrl()); //02/05/2025 campo que se pide eliminar
        person.setDni(requestDto.getDni());

        // Actualizar datos del paciente
        patient.setInsuranceName(requestDto.getInsuranceName());
        patient.setHasInsurance(requestDto.isHasInsurance());
        patient.setSchool(requestDto.getSchool());
        patient.setBirthDate(requestDto.getBirthDate());
        // patient.setPaymentType(requestDto.getPaymentType()); //02/05/2025 campo que se pide eliminar

        // Actualizar solo el email en la tabla usuario
        var user = userRepository.findByPerson(person)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para el paciente con ID: " + id));

        user.setEmail(requestDto.getEmail());

        // Guardar cambios
        patientRepository.save(patient);
        userRepository.save(user); // importante guardar el user también

        return PatientResponseDto.builder()
                .id(person.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                // .country(person.getCountry())
                // .photoUrl(person.getPhotoUrl())
                 .birthDate(patient.getBirthDate())
                .dni(person.getDni())
                .email(user.getEmail())
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .school(patient.getSchool())
               // .paymentType(patient.getPaymentType()) // campo que se pide eliminar 02/05/2025
                .build();
    }

    @Transactional
    public void deletePatientById(Long patientId) {
        PatientModel patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        Long personId = patient.getId();

        // Elimina relaciones con profesionales
        patientRepository.deletePatientProfessionalRelation(patientId);

        // Elimina registros médicos asociados (si corresponde)
        medicalRecordsRepository.deleteByPatientId(personId);

        // Elimina usuario y sus roles si existen
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

        return PatientResponseDto.builder()
                .id(patient.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
              //  .country(person.getCountry()) // campo que se pide eliminar 02/05/2025
              //  .photoUrl(person.getPhotoUrl()) // campo que se pide eliminar 02/05/2025
                .birthDate(patient.getBirthDate()) //cambio sugerido que solo este en paciente 02/05/2025
                .dni(person.getDni())
                .email(user != null ? user.getEmail() : null)
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .school(patient.getSchool())
              //  .paymentType(patient.getPaymentType())
                .build();
    }

    //buscar paciente por nombre
    public List<PatientResponseDto> getPatientsByName(String name) {
        List<PatientModel> patients = patientRepository.findByPerson_NameContainingIgnoreCase(name);

        if (patients.isEmpty()) {
            throw new RuntimeException("No se encontraron pacientes con el nombre: " + name);
        }

        return patients.stream().map(patient -> {
            PersonModel person = patient.getPerson();
            var user = userRepository.findByPerson(person).orElse(null);

            return PatientResponseDto.builder()
                    .id(patient.getId())
                    .name(person.getName())
                    .lastName(person.getLastName())
                    .phoneNumber(person.getPhoneNumber())
                    //   .country(person.getCountry()) // campo que se pide eliminar 02/05/2025
                    //   .photoUrl(person.getPhotoUrl()) // campo que se pide eliminar 02/05/2025
                       .birthDate(patient.getBirthDate())
                    .dni(person.getDni())
                    .email(user != null ? user.getEmail() : null)
                    .insuranceName(patient.getInsuranceName())
                    .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                    .school(patient.getSchool())
                    //   .paymentType(patient.getPaymentType())
                    .build();
        }).toList();
    }

}





