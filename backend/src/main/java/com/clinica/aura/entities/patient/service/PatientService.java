package com.clinica.aura.entities.patient.service;

import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.entities.patient.dtoRequest.PatientRequestDto;
import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.entities.patient.model.PatientModel;
import com.clinica.aura.entities.patient.repository.PatientRepository;
import com.clinica.aura.entities.person.model.PersonModel;
import com.clinica.aura.entities.user_account.Enum.EnumRole;
import com.clinica.aura.entities.user_account.dtoResponse.AuthResponseRegisterDto;
import com.clinica.aura.entities.user_account.models.RoleModel;
import com.clinica.aura.entities.user_account.models.UserModel;
import com.clinica.aura.entities.user_account.repository.RoleRepository;
import com.clinica.aura.entities.user_account.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public PatientResponseDto createUser(@Valid PatientRequestDto authCreateUserDto) {

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

        patientRepository.save(patientModel);

        UserModel userEntity = UserModel.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roleEntities)
                .person(personEntity)
                .build();

        userRepository.save(userEntity);

        return PatientResponseDto.builder()
                .name(personEntity.getName())
                .lastName(personEntity.getLastName())
                .phoneNumber(personEntity.getPhoneNumber())
                .country(personEntity.getCountry())
                .photoUrl(personEntity.getPhotoUrl())
                .birthDate(personEntity.getBirthDate())
                .dni(personEntity.getDni())
                .insuranceName(patientModel.getInsuranceName())
                .school(patientModel.getSchool())
                .paymentType(patientModel.getPaymentType())
                .build();
    }
    //agregado nadia
    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll().stream().map(patient -> {
            var person = patient.getPerson();
            return PatientResponseDto.builder()
                    .name(person.getName())
                    .lastName(person.getLastName())
                    .phoneNumber(person.getPhoneNumber())
                    .country(person.getCountry())
                    .photoUrl(person.getPhotoUrl())
                    .birthDate(person.getBirthDate())
                    .dni(person.getDni())
                    .insuranceName(patient.getInsuranceName())
                    .school(patient.getSchool())
                    .paymentType(patient.getPaymentType())
                    .build();
        }).toList();
    }


    public PatientResponseDto getPatientById(Long id) {
        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        var person = patient.getPerson();

        return PatientResponseDto.builder()
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .country(person.getCountry())
                .photoUrl(person.getPhotoUrl())
                .birthDate(person.getBirthDate())
                .dni(person.getDni())
                .insuranceName(patient.getInsuranceName())
                .school(patient.getSchool())
                .paymentType(patient.getPaymentType())
                .build();
    }

}
