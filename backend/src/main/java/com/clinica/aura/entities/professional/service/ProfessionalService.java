package com.clinica.aura.entities.professional.service;

import com.clinica.aura.exceptions.ResourceNotFoundException;
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

        Optional<RoleModel> professionalRole = roleRepository.findByEnumRole(EnumRole.PROFESSIONAL);
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

        ProfessionalModel professionalEntity = ProfessionalModel.builder()
                .person(personEntity)
                .licenseNumber(authCreateUserDto.getLicenseNumber())
                .specialty(authCreateUserDto.getSpecialty())
                .build();

        professionalRepository.save(professionalEntity);

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

    //buscar profesional por id
    public ProfessionalResponseDto getProfessionalById(Long id) {
        ProfessionalModel professional = professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado con ID: " + id));
        return mapToDto(professional);
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


    public ProfessionalResponseDto updateProfessional(Long id, ProfessionalModel professionalModel) {
        ProfessionalModel existing = professionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado"));

        existing.getPerson().setName(professionalModel.getPerson().getName());
        existing.getPerson().setLastName(professionalModel.getPerson().getLastName());
        existing.getPerson().setDni(professionalModel.getPerson().getDni());
        existing.getPerson().setPhoneNumber(professionalModel.getPerson().getPhoneNumber());
        existing.getPerson().setCountry(professionalModel.getPerson().getCountry());
        existing.getPerson().setBirthDate(professionalModel.getPerson().getBirthDate());
        existing.getPerson().setPhotoUrl(professionalModel.getPerson().getPhotoUrl());

        existing.setLicenseNumber(professionalModel.getLicenseNumber());
        existing.setSpecialty(professionalModel.getSpecialty());

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
        ProfessionalModel professional = professionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado"));

        professional.setDeleted(true); // Marcamos como eliminado
        professionalRepository.save(professional); // Guardamos el cambio
    }

}
