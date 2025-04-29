package com.clinica.aura.entities.professional.service;
import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import jakarta.persistence.EntityNotFoundException;
import com.clinica.aura.exceptions.*;
import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.entities.person.model.PersonModel;
import com.clinica.aura.entities.professional.dtoRequest.ProfessionalRequestDto;
import com.clinica.aura.entities.professional.dtoResponse.ProfessionalResponseDto;
import com.clinica.aura.entities.professional.model.ProfessionalModel;
import com.clinica.aura.entities.professional.repository.ProfessionalRepository;
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
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfessionalService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final ProfessionalRepository professionalRepository;

    @Transactional
    public AuthResponseRegisterDto createUser(@Valid ProfessionalRequestDto authCreateUserDto) {

        String email = authCreateUserDto.getEmail();
        String password = authCreateUserDto.getPassword();
        String username = authCreateUserDto.getName();
        String lastName = authCreateUserDto.getLastName();
        String dni = authCreateUserDto.getDni();
        String phoneNumber = authCreateUserDto.getPhoneNumber();
        String country = authCreateUserDto.getCountry();
        String photoUrl = authCreateUserDto.getPhotoUrl();
        LocalDate birthDate = authCreateUserDto.getBirthDate();

        UserModel emailExists = userRepository.findByEmail(email).orElse(null);
        if (emailExists != null) {
            throw new EmailAlreadyExistsException("El correo " + email + " ya existe en la base de datos.");
        }

        Optional<RoleModel> professionalRole = roleRepository.findByEnumRole(EnumRole.PROFESSIONAL);
        if (professionalRole.isEmpty()) {
            throw new IllegalArgumentException("El rol especificado no está configurado en la base de datos.");
        }

        Set<RoleModel> roleEntities = Set.of(professionalRole.get());

        // Crea la persona (aún no se guarda explícitamente)
        PersonModel personEntity = PersonModel.builder()
                .dni(dni)
                .name(username)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .country(country)
                .birthDate(birthDate)
                .photoUrl(photoUrl)
                .build();

        // Crea el profesional con la persona
        ProfessionalModel professionalEntity = ProfessionalModel.builder()
                .person(personEntity)
                .licenseNumber(authCreateUserDto.getLicenseNumber())
                .specialty(authCreateUserDto.getSpecialty())
                .deleted(false) //  Asegura que no se guarde como null
                .build();

        // Guarda el profesional (esto también guarda la persona gracias a cascade = PERSIST)
        professionalRepository.save(professionalEntity);

        // Crea el usuario con la persona ya persistida
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

//    //listar todos los profesionales
//    public List<ProfessionalResponseDto> getAllProfessionals() {
//        return professionalRepository.findByDeletedFalse()
//                .stream()
//                .map(this::mapToDto)
//                .toList();
//    }


    // Buscar profesional por ID
    public ProfessionalResponseDto getProfessionalById(Long id) {
        Optional<ProfessionalModel> professional = professionalRepository.findById(id);
        if (professional.isEmpty()) {
            // Lanza la excepción ResourceNotFoundException si no se encuentra el profesional
            throw new ProfessionalNotFoundException("Profesional con ID " + id + " no encontrado.");
        }

        // Llamamos a mapToDto para convertir la entidad en DTO
        return mapToDto(professional.get());
    }

    //paginacion
    public Page<ProfessionalResponseDto> getProfessionalsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return professionalRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    //metodo q filtra por especialidad o apellido
    public List<ProfessionalResponseDto> searchProfessionals(String keyword) {
        return professionalRepository.searchByLastNameOrSpecialty(keyword)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    //mapeo a dto
    private ProfessionalResponseDto mapToDto(ProfessionalModel professional) {
        // Mapeo de los pacientes del profesional
        List<PatientResponseDto> patientsDto = professional.getPatients() != null
                ? professional.getPatients().stream()
                .map(patient -> PatientResponseDto.builder()
                        .id(patient.getId())
                        .name(patient.getPerson() != null ? patient.getPerson().getName() : null) // Control de null
                        .lastName(patient.getPerson() != null ? patient.getPerson().getLastName() : null)
                        .email(null) // Omitimos `getUser` porque no existe en `PersonModel`
                        .school(patient.getSchool())
                        .professionalIds(patient.getProfessionals() != null
                                ? patient.getProfessionals().stream()
                                .map(ProfessionalModel::getId)
                                .toList()
                                : Collections.emptyList()) // Validar `professionalIds`
                        .build()
                )
                .toList()
                : Collections.emptyList(); // Si no hay pacientes, devolver lista vacía.

        // Construimos el DTO utilizando los datos disponibles
        PersonModel person = professional.getPerson(); // Aseguramos validaciones de null
        return new ProfessionalResponseDto(
                professional.getId(),
                person != null ? person.getDni() : null, // Validamos si existe `PersonModel`
                person != null ? person.getName() : null,
                person != null ? person.getLastName() : null,
                person != null ? person.getPhoneNumber() : null,
                person != null ? person.getCountry() : null,
                person != null ? person.getPhotoUrl() : null,
                person != null ? person.getBirthDate() : null,
                professional.getLicenseNumber(),
                professional.getSpecialty(),
                patientsDto // Agregamos los pacientes mapeados
        );
    }


    //metodo para hacer update a profesional
    public ProfessionalResponseDto updateProfessional(Long id, @Valid ProfessionalRequestDto dto) {
        ProfessionalModel existing = professionalRepository.findById(id)
                .orElseThrow(() -> new ProfessionalNotFoundException("Profesional no encontrado con ID: " + id));

        existing.getPerson().setName(dto.getName());
        existing.getPerson().setLastName(dto.getLastName());
        existing.getPerson().setDni(dto.getDni());
        existing.getPerson().setPhoneNumber(dto.getPhoneNumber());
        existing.getPerson().setCountry(dto.getCountry());
        existing.getPerson().setBirthDate(dto.getBirthDate());
        existing.getPerson().setPhotoUrl(dto.getPhotoUrl());

        existing.setLicenseNumber(dto.getLicenseNumber());
        existing.setSpecialty(dto.getSpecialty());

        ProfessionalModel updated = professionalRepository.save(existing);
        return mapToDto(updated);
    }


    //método de eliminacion tradicional
    /*
    public void deleteProfessional(Long id) {
        if (!professionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profesional no encontrado");
        }
        professionalRepository.deleteById(id);
    }*/

    //método de eliminación logica
    public void deleteProfessional(Long id) {
        ProfessionalModel professional = professionalRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Profesional no encontrado con ID: " + id));
        professional.setDeleted(true);
        professionalRepository.save(professional);
    }

    //metodo para listar los pacientes de un profesional por su id
    @Transactional(readOnly = true)
    public List<PatientResponseDto> getPatientsByProfessionalId(Long professionalId) {
        // Obtener el profesional por su ID
        ProfessionalModel professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profesional no encontrado con ID: " + professionalId));

        // Mapeo de sus pacientes (si existen)
        return professional.getPatients() != null
                ? professional.getPatients().stream()
                .map(patient -> PatientResponseDto.builder()
                        .id(patient.getId())
                        .name(patient.getPerson() != null ? patient.getPerson().getName() : null) // Validación para posibles null
                        .lastName(patient.getPerson() != null ? patient.getPerson().getLastName() : null)
                        .email(null)
                        .school(patient.getSchool())
                        .professionalIds(patient.getProfessionals() != null
                                ? patient.getProfessionals().stream()
                                .map(ProfessionalModel::getId)
                                .toList()
                                : Collections.emptyList()) // Si no hay profesionales asociados, lista vacía
                        .build()
                )
                .toList()
                : Collections.emptyList(); // Si no tiene pacientes, devolvemos una lista vacía
    }








}
