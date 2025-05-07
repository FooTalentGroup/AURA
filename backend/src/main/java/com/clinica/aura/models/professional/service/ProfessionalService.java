package com.clinica.aura.models.professional.service;
import com.clinica.aura.models.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.models.patient.model.PatientModel;
import com.clinica.aura.models.patient.repository.PatientRepository;
import com.clinica.aura.models.user_account.service.impl.UserDetailsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import com.clinica.aura.exceptions.*;
import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.models.person.model.PersonModel;
import com.clinica.aura.models.professional.dtoRequest.ProfessionalRequestDto;
import com.clinica.aura.models.professional.dtoResponse.ProfessionalResponseDto;
import com.clinica.aura.models.professional.model.ProfessionalModel;
import com.clinica.aura.models.professional.repository.ProfessionalRepository;
import com.clinica.aura.models.user_account.Enum.EnumRole;
import com.clinica.aura.models.user_account.dtoResponse.AuthResponseRegisterDto;
import com.clinica.aura.models.user_account.models.RoleModel;
import com.clinica.aura.models.user_account.models.UserModel;
import com.clinica.aura.models.user_account.repository.RoleRepository;
import com.clinica.aura.models.user_account.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final UserDetailsServiceImpl userDetailsService;
    private final PatientRepository patientRepository;

    @Transactional
    public AuthResponseRegisterDto createUser(@Valid ProfessionalRequestDto authCreateUserDto) {

        String email = authCreateUserDto.getEmail();
        String password = authCreateUserDto.getPassword();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("El correo " + email + " ya existe en la base de datos.");
        }

        RoleModel professionalRole = roleRepository.findByEnumRole(EnumRole.PROFESSIONAL)
                .orElseThrow(() -> new IllegalArgumentException("El rol especificado no está configurado en la base de datos."));
        Set<RoleModel> roleEntities = Set.of(professionalRole);

        // Crea la persona con todos los campos nuevos
        PersonModel personEntity = PersonModel.builder()
                .dni(authCreateUserDto.getDni())
                .name(authCreateUserDto.getName())
                .lastName(authCreateUserDto.getLastName())
                .phoneNumber(authCreateUserDto.getPhoneNumber())
                .address(authCreateUserDto.getAddress())
                .birthDate(authCreateUserDto.getBirthDate())
                .locality(authCreateUserDto.getLocality())
                .cuil(authCreateUserDto.getCuil())
                .build();

        // Crea el profesional
        ProfessionalModel professionalEntity = ProfessionalModel.builder()
                .person(personEntity)
                .licenseNumber(authCreateUserDto.getLicenseNumber())
                .specialty(authCreateUserDto.getSpecialty())
                .deleted(false)
                .build();

        // Guarda el profesional (y con cascade, también guarda la persona)
        professionalRepository.save(professionalEntity);

        // Crea el usuario vinculado a la persona
        UserModel userEntity = UserModel.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roleEntities)
                .person(personEntity)
                .build();

        UserModel userCreated = userRepository.save(userEntity);

        // Autoridades para JWT
        List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();
        userCreated.getRoles().forEach(role -> {
            authoritiesList.add(new SimpleGrantedAuthority("ROLE_" + role.getEnumRole().name()));
            role.getPermissions().forEach(permission ->
                    authoritiesList.add(new SimpleGrantedAuthority(permission.getName())));
        });

        UserDetails userDetails = userDetailsService.loadUserByUsername(userCreated.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userCreated.getPassword(), authoritiesList);
        String accessToken = jwtUtils.generateJwtToken(authentication);

        return new AuthResponseRegisterDto(
                userCreated.getId(),
                authCreateUserDto.getName(),
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
        PersonModel person = professional.getPerson();

        List<Long> patientIds = professional.getPatients() != null
                ? professional.getPatients().stream()
                .map(PatientModel::getId)
                .toList()
                : Collections.emptyList();

        return new ProfessionalResponseDto(
                professional.getId(),

                person != null ? person.getDni() : null,
                person != null ? person.getName() : null,
                person != null ? person.getLastName() : null,
                person != null ? person.getPhoneNumber() : null,
                person != null ? person.getAddress() : null,
                person != null ? person.getBirthDate() : null,
                person != null ? person.getLocality() : null,
                person != null ? person.getCuil() : null,
                professional.getLicenseNumber(),
                professional.getSpecialty(),
                patientIds
        );
    }



    //metodo para hacer update a profesional
    public ProfessionalResponseDto updateProfessional(Long id, @Valid ProfessionalRequestDto dto) {
        ProfessionalModel existing = professionalRepository.findById(id)
                .orElseThrow(() -> new ProfessionalNotFoundException("Profesional no encontrado con ID: " + id));

        PersonModel person = existing.getPerson();


        person.setDni(dto.getDni());
        person.setName(dto.getName());
        person.setLastName(dto.getLastName());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setAddress(dto.getAddress());
        person.setBirthDate(dto.getBirthDate());
        person.setLocality(dto.getLocality());
        person.setCuil(dto.getCuil());

        existing.setLicenseNumber(dto.getLicenseNumber());
        existing.setSpecialty(dto.getSpecialty());

        // Setear pacientes
        List<PatientModel> patients = dto.getPatientIds() != null
                ? patientRepository.findAllById(dto.getPatientIds())
                : Collections.emptyList();

        existing.setPatients(patients);

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
        List<PatientModel> patients = professionalRepository.findPatientsByProfessionalId(professionalId);

        return patients.stream().map(patient -> {
            PersonModel person = patient.getPerson(); // extraigo la persona

            return PatientResponseDto.builder()
                    .id(patient.getId())
                    .name(person != null ? person.getName() : null)
                    .lastName(person != null ? person.getLastName() : null)
                    .dni(person != null ? person.getDni() : null)
                    .phoneNumber(person != null ? person.getPhoneNumber() : null)
                   // .country(person != null ? person.getCountry() : null) //cambio sugerido a eliminar  02/05/2025
                  //  .photoUrl(person != null ? person.getPhotoUrl() : null) //cambio sugerido a eliminar  02/05/2025
                  //  .birthDate(person != null ? person.getBirthDate() : null) //cambio sugerido a eliminar, que solo este en paciente  02/05/2025
                    .email(null) //
                    .hasInsurance(patient.isHasInsurance())
                    .insuranceName(patient.getInsuranceName())
                    .school(patient.getSchool())
                   // .paymentType(patient.getPaymentType()) //cambio sugerido a eliminar  02/05/2025
                    .professionalIds(patient.getProfessionals() != null
                            ? patient.getProfessionals().stream()
                            .map(ProfessionalModel::getId)
                            .toList()
                            : Collections.emptyList())
                    .build();
        }).toList();
    }
}
