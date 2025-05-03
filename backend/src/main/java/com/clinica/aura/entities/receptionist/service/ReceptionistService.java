package com.clinica.aura.entities.receptionist.service;

import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.entities.person.model.PersonModel;
import com.clinica.aura.entities.professional.dtoRequest.ProfessionalRequestDto;
import com.clinica.aura.entities.professional.model.ProfessionalModel;
import com.clinica.aura.entities.receptionist.dtoRequest.ReceptionistRequestDto;
import com.clinica.aura.entities.receptionist.dtoRequest.ReceptionistRequestUpdateDto;
import com.clinica.aura.entities.receptionist.dtoResponse.ReceptionistResponseDto;
import com.clinica.aura.entities.receptionist.mapper.ReceptionistMapper;
import com.clinica.aura.entities.receptionist.model.ReceptionistModel;
import com.clinica.aura.entities.receptionist.repository.ReceptionistRepository;
import com.clinica.aura.entities.user_account.Enum.EnumRole;
import com.clinica.aura.entities.user_account.dtoResponse.AuthResponseRegisterDto;
import com.clinica.aura.entities.user_account.models.RoleModel;
import com.clinica.aura.entities.user_account.models.UserModel;
import com.clinica.aura.entities.user_account.repository.RoleRepository;
import com.clinica.aura.entities.user_account.repository.UserRepository;
import com.clinica.aura.entities.user_account.service.impl.UserDetailsServiceImpl;
import com.clinica.aura.exceptions.EmailAlreadyExistsException;
import com.clinica.aura.exceptions.ReceptionistNotFoundException;
import com.clinica.aura.util.PaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
public class ReceptionistService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ReceptionistRepository receptionistRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReceptionistMapper receptionistMapper;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    public AuthResponseRegisterDto createUser(@Valid ReceptionistRequestDto authCreateUserDto) {

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

        Optional<RoleModel> professionalRole = roleRepository.findByEnumRole(EnumRole.RECEPTIONIST);
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


        ReceptionistModel receptionistModel = ReceptionistModel.builder()
                .person(personEntity)
                .build();


        receptionistRepository.save(receptionistModel);

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
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userCreated.getPassword(), authoritiesList);
        String accessToken = jwtUtils.generateJwtToken(authentication);

        return new AuthResponseRegisterDto(
                userCreated.getId(),
                username,
                "Usuario registrado exitosamente",
                accessToken,
                true
        );
    }

    @Transactional
    public ReceptionistResponseDto updateReceptionist(long id, ReceptionistRequestUpdateDto receptionistRequestUpdateDto) {
        ReceptionistModel receptionistModel = receptionistRepository.findById(id)
                .orElseThrow(() -> new ReceptionistNotFoundException("Recepcionista no encontrado con ID: " + id));

        receptionistMapper.updateReceptionist(receptionistRequestUpdateDto, receptionistModel);

        ReceptionistModel updatedReceptionist = receptionistRepository.save(receptionistModel);

        return receptionistMapper.toReceptionistResponseDto(updatedReceptionist);
    }

    public ReceptionistResponseDto getReceptionistById(long id) {
        ReceptionistModel receptionistModel = receptionistRepository.findById(id)
                .orElseThrow(() -> new ReceptionistNotFoundException("Recepcionista no encontrado con ID: " + id));

        return receptionistMapper.toReceptionistResponseDto(receptionistModel);
    }

    public PaginatedResponse<ReceptionistResponseDto> getAllReceptionists(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReceptionistModel> receptionistsPage = receptionistRepository.findAll(pageable);

        List<ReceptionistModel> receptionists = receptionistsPage.getContent();
        List<ReceptionistResponseDto> receptionistResponseDtos = receptionists.stream()
                .map(receptionistMapper::toReceptionistResponseDto)
                .toList();
        return new PaginatedResponse<>(
                receptionistResponseDtos,
                receptionistsPage.getNumber(),
                receptionistsPage.getSize(),
                receptionistsPage.getTotalPages(),
                receptionistsPage.getTotalElements()
        );
    }

    @Transactional
    public void deleteReceptionist(long id) {
        ReceptionistModel receptionistModel = receptionistRepository.findById(id)
                .orElseThrow(() -> new ReceptionistNotFoundException("Recepcionista no encontrado con ID: " + id));
        receptionistRepository.delete(receptionistModel);
    }




}
