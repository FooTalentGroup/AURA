package com.clinica.aura.modules.professional.service;
import com.clinica.aura.modules.patient.dto.PatientResponseDto;
import com.clinica.aura.modules.patient.model.PatientModel;
import com.clinica.aura.modules.patient.repository.PatientRepository;
import com.clinica.aura.modules.person.repository.PersonRepository;
import com.clinica.aura.modules.professional.dtoRequest.ProfessionalRequestUpdateDto;
import com.clinica.aura.modules.user_account.service.impl.UserDetailsServiceImpl;
import com.clinica.aura.exceptions.*;
import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.modules.person.model.PersonModel;
import com.clinica.aura.modules.professional.dtoRequest.ProfessionalRequestDto;
import com.clinica.aura.modules.professional.dtoResponse.ProfessionalResponseDto;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import com.clinica.aura.modules.professional.repository.ProfessionalRepository;
import com.clinica.aura.modules.user_account.Enum.EnumRole;
import com.clinica.aura.modules.user_account.dtoResponse.AuthResponseRegisterDto;
import com.clinica.aura.modules.user_account.models.RoleModel;
import com.clinica.aura.modules.user_account.models.UserModel;
import com.clinica.aura.modules.user_account.repository.RoleRepository;
import com.clinica.aura.modules.user_account.repository.UserRepository;
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
import com.clinica.aura.exceptions.DniAlreadyExistsException;

/**
 * Servicio que gestiona operaciones relacionadas con profesionales de la salud.
 */
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
    private final PersonRepository personRepository;

    /**
     * Crea un nuevo usuario profesional, asociando una persona, profesional y usuario en la base de datos.
     * Valida que el email y DNI no estén registrados previamente.
     *
     * @param authCreateUserDto DTO con la información del profesional a registrar.
     * @return DTO de respuesta con información del registro y token JWT.
     * @throws EmailAlreadyExistsException si el correo ya está registrado.
     * @throws DniAlreadyExistsException si el DNI ya existe en la base de datos.
     */
    @Transactional
    public AuthResponseRegisterDto createUser(@Valid ProfessionalRequestDto authCreateUserDto) {

        String email = authCreateUserDto.getEmail();
        String password = authCreateUserDto.getPassword();
        String dni = authCreateUserDto.getDni();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("El correo " + email + " ya existe en la base de datos.");
        }

        //valida que el dni no este en la base
        if (personRepository.findByDni(dni).isPresent()) {
            throw new DniAlreadyExistsException("El DNI " + dni + " ya está registrado en la base de datos.");
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



    /**
     * Obtiene la información de un profesional a partir de su ID.
     *
     * @param id ID del profesional.
     * @return DTO con los datos del profesional.
     * @throws ProfessionalNotFoundException si no se encuentra el profesional.
     */
    public ProfessionalResponseDto getProfessionalById(Long id) {
        Optional<ProfessionalModel> professional = professionalRepository.findById(id);
        if (professional.isEmpty()) {
            // Lanza la excepción ResourceNotFoundException si no se encuentra el profesional
            throw new ProfessionalNotFoundException("Profesional con ID " + id + " no encontrado.");
        }

        // Llamamos a mapToDto para convertir la entidad en DTO
        return mapToDto(professional.get());
    }

    /**
     * Devuelve una lista paginada de profesionales.
     *
     * @param page Número de página a obtener (comienza en 0).
     * @param size Cantidad de elementos por página.
     * @return Página con profesionales mapeados a DTO.
     */
    public Page<ProfessionalResponseDto> getProfessionalsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return professionalRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    /**
     * Busca profesionales por apellido o especialidad utilizando una palabra clave.
     *
     * @param keyword Término de búsqueda (apellido o especialidad).
     * @return Lista de profesionales coincidentes en formato DTO.
     */
    public List<ProfessionalResponseDto> searchProfessionals(String keyword) {
        return professionalRepository.searchByLastNameOrSpecialty(keyword)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Mapea un modelo de profesional a su representación como DTO.
     *
     * @param professional Entidad ProfessionalModel.
     * @return DTO correspondiente.
     */
    private ProfessionalResponseDto mapToDto(ProfessionalModel professional) {
        PersonModel person = professional.getPerson();

        List<Long> patientIds = professional.getPatients() != null
                ? professional.getPatients().stream()
                .map(PatientModel::getId)
                .toList()
                : Collections.emptyList();

        return ProfessionalResponseDto.builder()
                .id(professional.getId())
                .email(userRepository.findByPersonId(person.getId()).get().getEmail())
                .dni(person.getDni())
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .address(person.getAddress())
                .birthDate(person.getBirthDate())
                .locality(person.getLocality())
                .cuil(person.getCuil())
                .licenseNumber(professional.getLicenseNumber())
                .specialty(professional.getSpecialty())
                .patientIds(patientIds)
                .build();
    }



    /**
     * Actualiza la información de un profesional existente, incluyendo sus datos personales y pacientes asociados.
     *
     * @param id ID del profesional a actualizar.
     * @param dto DTO con los datos actualizados.
     * @return DTO actualizado del profesional.
     * @throws ProfessionalNotFoundException si no se encuentra el profesional.
     */
    public ProfessionalResponseDto updateProfessional(Long id, @Valid ProfessionalRequestUpdateDto dto) {
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




    /**
     * Elimina lógicamente a un profesional (soft delete) y elimina su persona asociada.
     *
     * @param id ID del profesional a eliminar.
     * @throws ProfessionalNotFoundException si no se encuentra el profesional.
     */
    public void deleteProfessional(Long id) {
        ProfessionalModel professional = professionalRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProfessionalNotFoundException("Profesional no encontrado con ID: " + id));
        personRepository.deleteById(professional.getPerson().getId());
        professional.setDeleted(true);
        professionalRepository.save(professional);
    }
    /**
     * Obtiene todos los pacientes asociados a un profesional específico.
     *
     * @param professionalId ID del profesional.
     * @return Lista de pacientes asociados al profesional.
     */
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
                        .birthDate(person != null ? person.getBirthDate() : null)
                        .email(null) // lo dejás en null porque no tenés el usuario
                        .hasInsurance(patient.isHasInsurance())
                        .insuranceName(patient.getInsuranceName())
                        .address(patient.getAddress())
                        .tutorName(patient.getTutorName())
                        .relationToPatient(patient.getRelationToPatient())
                        .genre(patient.getGenre())
                        .memberShipNumber(patient.getMemberShipNumber())
                        .insurancePlan(patient.getInsurancePlan())
                        .schoolId(patient.getSchoolModel() != null ? patient.getSchoolModel().getId() : null)
                        .professionalIds(patient.getProfessionals() != null
                                ? patient.getProfessionals().stream()
                                .map(ProfessionalModel::getId)
                                .toList()
                                : Collections.emptyList())
                        .build();
            }).toList();
        }
}
