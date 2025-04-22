package com.clinica.aura.entities.professional.service;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    //listar todos los profesionales
    public List<ProfessionalResponseDto> getAllProfessionals() {
        return professionalRepository.findByDeletedFalse()
                .stream()
                .map(this::mapToDto)
                .toList();
    }


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


    //mapeo a Dto
    private ProfessionalResponseDto mapToDto(ProfessionalModel professional) {
        return new ProfessionalResponseDto(
                professional.getId(),

                professional.getPerson().getDni(),
                professional.getPerson().getName(),
                professional.getPerson().getLastName(),
                professional.getPerson().getPhoneNumber(),
                professional.getPerson().getCountry(),
                professional.getPerson().getPhotoUrl(),
                professional.getPerson().getBirthDate(),
                professional.getLicenseNumber(),
                professional.getSpecialty()
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



}
