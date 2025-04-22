package com.clinica.aura.entities.patient.service;

import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.entities.medical_records.repository.MedicalRecordsRepository;
import com.clinica.aura.entities.patient.dtoRequest.PatientRequestDto;
import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.entities.patient.model.PatientModel;
import com.clinica.aura.entities.patient.repository.PatientRepository;
import com.clinica.aura.entities.person.model.PersonModel;
import com.clinica.aura.entities.person.repository.PersonRepository;
import com.clinica.aura.entities.user_account.Enum.EnumRole;
import com.clinica.aura.entities.user_account.dtoResponse.AuthResponseRegisterDto;
import com.clinica.aura.entities.user_account.models.RoleModel;
import com.clinica.aura.entities.user_account.models.UserModel;
import com.clinica.aura.entities.user_account.repository.RoleRepository;
import com.clinica.aura.entities.user_account.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public PatientResponseDto createUser(@Valid PatientRequestDto authCreateUserDto) {

        String email = authCreateUserDto.getEmail();
        String username = authCreateUserDto.getName();
        String lastName = authCreateUserDto.getLastName();
        String dni = authCreateUserDto.getDni();
        String phoneNumber = authCreateUserDto.getPhoneNumber();
        String country = authCreateUserDto.getCountry();
        String photoUrl = authCreateUserDto.getPhotoUrl();
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
                .country(country)
                .birthDate(birthDate)
                .photoUrl(photoUrl)
                .build();

        PatientModel patientModel = PatientModel.builder()
                .person(personEntity)
                .hasInsurance(authCreateUserDto.isHasInsurance())
                .insuranceName(authCreateUserDto.getInsuranceName())
                .school(authCreateUserDto.getSchool())
                .paymentType(authCreateUserDto.getPaymentType())
                .build();

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
                .country(personEntity.getCountry())
                .photoUrl(personEntity.getPhotoUrl())
                .birthDate(personEntity.getBirthDate())
                .dni(personEntity.getDni())
                .hasInsurance(patientModel.getInsuranceName() != null && !patientModel.getInsuranceName().isBlank())
                .insuranceName(patientModel.getInsuranceName())
                .school(patientModel.getSchool())
                .paymentType(patientModel.getPaymentType())
                .build();
    }

    //listado de pacientes
    public List<PatientResponseDto> getPatientsByRange(int from, int to) {
        List<PatientModel> patients = patientRepository.findAll();

        // Validamos el rango
        if (from < 0) from = 0;
        if (to >= patients.size()) to = patients.size() - 1;
        if (from > to) return List.of();

        return patients.subList(from, to + 1).stream().map(patient -> {
            var person = patient.getPerson();
            Optional<UserModel> userOptional = userRepository.findByPerson(person);

            return PatientResponseDto.builder()
                    .id(patient.getId())
                    .name(person.getName())
                    .lastName(person.getLastName())
                    .phoneNumber(person.getPhoneNumber())
                    .country(person.getCountry())
                    .photoUrl(person.getPhotoUrl())
                    .birthDate(person.getBirthDate())
                    .dni(person.getDni())
                    .email(userOptional.map(UserModel::getEmail).orElse(null))
                    .insuranceName(patient.getInsuranceName())
                    .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                    .school(patient.getSchool())
                    .paymentType(patient.getPaymentType())
                    .build();
        }).toList();
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
                .country(person.getCountry())
                .photoUrl(person.getPhotoUrl())
                .birthDate(person.getBirthDate())
                .dni(person.getDni())
                .email(user != null ? user.getEmail() : null)
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .school(patient.getSchool())
                .paymentType(patient.getPaymentType())
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
        person.setCountry(requestDto.getCountry());
        person.setPhotoUrl(requestDto.getPhotoUrl());
        person.setBirthDate(requestDto.getBirthDate());
        person.setDni(requestDto.getDni());

        // Actualizar datos del paciente
        patient.setInsuranceName(requestDto.getInsuranceName());
        patient.setHasInsurance(requestDto.isHasInsurance());
        patient.setSchool(requestDto.getSchool());
        patient.setPaymentType(requestDto.getPaymentType());

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
                .country(person.getCountry())
                .photoUrl(person.getPhotoUrl())
                .birthDate(person.getBirthDate())
                .dni(person.getDni())
                .email(user.getEmail())
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .school(patient.getSchool())
                .paymentType(patient.getPaymentType())
                .build();
    }

    @Transactional //Borrar paciente por id
    public void deletePatientById(Long patientId) {
        PatientModel patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        Long personId = patient.getId();

        // 1. Eliminar registros médicos asociados
        medicalRecordsRepository.deleteByPatientId(personId);

        Optional<UserModel> userOpt = userRepository.findByPersonId(personId);
        userOpt.ifPresent(user -> {
            patientRepository.deleteUserRolesByUserId(user.getId());
            patientRepository.deleteUserById(user.getId());
        });

        patientRepository.deletePatientByIdNative(personId);
        patientRepository.deletePersonById(personId);
    }

    //buscar paciente por dni
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
                .country(person.getCountry())
                .photoUrl(person.getPhotoUrl())
                .birthDate(person.getBirthDate())
                .dni(person.getDni())
                .email(user != null ? user.getEmail() : null)
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .school(patient.getSchool())
                .paymentType(patient.getPaymentType())
                .build();
    }

}



