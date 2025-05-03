package com.clinica.aura.entities.professional.service;
import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.entities.patient.model.PatientModel;
import com.clinica.aura.entities.user_account.service.impl.UserDetailsServiceImpl;
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
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    public AuthResponseRegisterDto createUser(@Valid ProfessionalRequestDto authCreateUserDto) {

        String email = authCreateUserDto.getEmail();
        String password = authCreateUserDto.getPassword();
        String username = authCreateUserDto.getName();
        String lastName = authCreateUserDto.getLastName();
        String dni = authCreateUserDto.getDni();
        String phoneNumber = authCreateUserDto.getPhoneNumber();
        //String country = authCreateUserDto.getCountry(); // campo que se pide eliminar 02/05/2025
       // String photoUrl = authCreateUserDto.getPhotoUrl(); // campo que se pide eliminar 02/05/2025
       // LocalDate birthDate = authCreateUserDto.getBirthDate(); // campo que se pide eliminar 02/05/2025 y que solo este solo en paciente

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
               // .country(country) // campo que se pide eliminar 02/05/2025
               // .birthDate(birthDate) // campo que se pide eliminar 02/05/2025 y que este solo en paciente
               // .photoUrl(photoUrl) // campo que se pide eliminar 02/05/2025
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

        UserDetails userDetails = userDetailsService.loadUserByUsername(userCreated.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userCreated.getPassword(),
                authoritiesList
        );
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
               // person != null ? person.getCountry() : null, // campo que se pide eliminar 02/05/2025
               // person != null ? person.getPhotoUrl() : null, // campo que se pide eliminar 02/05/2025
               // person != null ? person.getBirthDate() : null, // campo que se pide eliminar, y que solo este en paciente 02/05/2025
                professional.getLicenseNumber(),
                professional.getSpecialty(),
                patientIds // ahora solo devuelvo los IDs de los pacientes
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
        //existing.getPerson().setCountry(dto.getCountry());   //cambio sugerido a eliminar  02/05/2025
       // existing.getPerson().setBirthDate(dto.getBirthDate()); /cambio sugerido que solo este en paciente 02/05/2025
      //  existing.getPerson().setPhotoUrl(dto.getPhotoUrl()); //cambio sugerido a eliminar 02/05/2025

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
                    .email(null) // lo dejás en null porque no tenés el usuario
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
