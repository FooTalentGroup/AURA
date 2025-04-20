package com.clinica.aura.entities.patient.service;

import com.clinica.aura.config.jwt.JwtUtils;
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

    @PersistenceContext
    private EntityManager entityManager;

    public AuthResponseRegisterDto createUser(@Valid PatientRequestDto authCreateUserDto) {

        String email = authCreateUserDto.getEmail();
        String password = authCreateUserDto.getPassword();
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

        // Persistimos la entidad Person primero para asegurarnos de que tenga un ID antes de asociarla al paciente
        personRepository.save(personEntity);
        patientRepository.save(patientModel);

        UserModel userEntity = UserModel.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roleEntities)
                .person(personEntity)
                .build();

        UserModel userCreated = userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();

        userCreated.getRoles().forEach(role ->
                authoritiesList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getEnumRole().name())))
        );

        userCreated.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authoritiesList.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getEmail(), userCreated.getPassword(), authoritiesList);
        String accessToken = jwtUtils.generateJwtToken(authentication);

        return new AuthResponseRegisterDto(
                userCreated.getId(),
                username,
                "Usuario registrado exitosamente",
                accessToken,
                true
        );
    }
    //listar todos los pacientes
    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll().stream().map(patient -> {
            var person = patient.getPerson();

            // Buscás el usuario a partir de la persona asociada al paciente
            Optional<UserModel> userOptional = userRepository.findByPerson(person);

            return PatientResponseDto.builder()
                    .name(person.getName())
                    .lastName(person.getLastName())
                    .phoneNumber(person.getPhoneNumber())
                    .country(person.getCountry())
                    .photoUrl(person.getPhotoUrl())
                    .birthDate(person.getBirthDate())
                    .dni(person.getDni())
                    .email(userOptional.map(UserModel::getEmail).orElse(null)) //recupera el email
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

        // Actualizar datos de la persona
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

    @Transactional
    public void deletePatientById(Long patientId) {
        // Verificamos que el paciente exista
        PatientModel patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        Long personId = patient.getId();

        // 1. Eliminar relaciones users_roles
       // Optional<UserModel> userOpt = userRepository.findByPersonId(personId);
       // userOpt.ifPresent(user -> {
       //     entityManager.createNativeQuery("DELETE FROM users_roles WHERE user_id = :userId")
        //            .setParameter("userId", user.getId())
        //            .executeUpdate();

       //     // 2. Eliminar el usuario
      //      entityManager.createNativeQuery("DELETE FROM users WHERE id = :userId")
      //              .setParameter("userId", user.getId())
       //             .executeUpdate();
      //  });

        // 3. Eliminar el paciente
        entityManager.createNativeQuery("DELETE FROM patients WHERE id = :id")
                .setParameter("id", personId)
                .executeUpdate();

        // 4. Eliminar la persona
        //entityManager.createNativeQuery("DELETE FROM person WHERE id = :id")
         //       .setParameter("id", personId)
        //        .executeUpdate();
    }






}

