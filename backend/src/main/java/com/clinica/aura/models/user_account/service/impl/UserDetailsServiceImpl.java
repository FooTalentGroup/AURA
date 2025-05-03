package com.clinica.aura.models.user_account.service.impl;

import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.models.professional.repository.ProfessionalRepository;
import com.clinica.aura.models.user_account.dtoRequest.AuthLoginRequestDto;
import com.clinica.aura.models.user_account.dtoRequest.SuspendRequestDto;
import com.clinica.aura.models.user_account.dtoResponse.AuthResponseDto;
import com.clinica.aura.models.user_account.dtoResponse.UserMeResponseDto;
import com.clinica.aura.models.user_account.dtoResponse.UserResponseDto;
import com.clinica.aura.models.user_account.models.UserModel;
import com.clinica.aura.models.user_account.repository.RoleRepository;
import com.clinica.aura.models.user_account.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final ProfessionalRepository professionalRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                "El usuario con el email " + email + "no existe"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userEntity.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getEnumRole().name())));
        });

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                true,
                true,
                true,
                authorities);
    }

    public AuthResponseDto loginUser(@Valid AuthLoginRequestDto authDto) {
        String email = authDto.getEmail();
        String password = authDto.getPassword();

        Long id = userRepository.findByEmail(email)
                .map(UserModel::getId)
                .orElseThrow(() -> new UsernameNotFoundException("El Id del usuario con el correo " + email + " no existe"));


        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtUtils.generateJwtToken(authentication);
        return new AuthResponseDto(id, email, "Usuario logeado exitosamente", token, true);


    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);

        UserModel userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!userEntity.isEnabled()) {
            throw new DisabledException("Usuario suspendido hasta: " + userEntity.getSuspensionEnd());
        }

//        if (!userDetails.isEnabled()) {
//            throw new DisabledException("Usuario suspendido hasta: " +
//                    ((UserModel) userDetails).getSuspensionEnd());
//        }
//        if (!userDetails.isAccountNonLocked()) {
//            throw new LockedException("Cuenta bloqueada");
//        }

//        if (userDetails == null) {
//            throw new BadCredentialsException("Usuario no encontrado");
//        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

    }

    public UserResponseDto getUserById(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("El Id del usuario " + userId + " no existe"));
        return new UserResponseDto(user.getPerson().getName(), user.getPerson().getLastName());
    }

    public UserMeResponseDto getCurrentUser(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con el email " + email + " no encontrado"));

        return UserMeResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> role.getEnumRole().name())
                        .toList())
                .build();
    }


    @Transactional
    public void suspendUser(Long userId, int duration, SuspendRequestDto.TimeUnit unit) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        LocalDateTime now = LocalDateTime.now();
        user.setSuspensionEnd(calculateSuspensionEnd(now, duration, unit));
        userRepository.save(user);
    }

    private LocalDateTime calculateSuspensionEnd(LocalDateTime start, int duration, SuspendRequestDto.TimeUnit unit) {
        return switch (unit) {
            case HOURS -> start.plusHours(duration);
            case DAYS -> start.plusDays(duration);
            case WEEKS -> start.plusWeeks(duration);
            case MONTHS -> start.plusMonths(duration);
        };
    }

    @Transactional
    public void activateUser(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        user.setSuspensionEnd(null);
        userRepository.save(user);
    }
}
