package com.clinica.aura.entities.user_account.controller;

import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.entities.patient.dtoRequest.PatientRequestDto;
import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.entities.patient.model.PatientModel;
import com.clinica.aura.entities.patient.service.PatientService;
import com.clinica.aura.entities.professional.dtoRequest.ProfessionalRequestDto;
import com.clinica.aura.entities.professional.service.ProfessionalService;
import com.clinica.aura.entities.receptionist.dtoRequest.ReceptionistRequestDto;
import com.clinica.aura.entities.receptionist.service.ReceptionistService;
import com.clinica.aura.entities.user_account.dtoRequest.AuthLoginRequestDto;
import com.clinica.aura.entities.user_account.dtoRequest.SuspendRequestDto;
import com.clinica.aura.entities.user_account.dtoResponse.AuthResponseDto;
import com.clinica.aura.entities.user_account.dtoResponse.AuthResponseRegisterDto;
import com.clinica.aura.entities.user_account.service.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final ProfessionalService professionalService;
    private final ReceptionistService receptionistService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Iniciar sesión", description = "Inicia sesión y obtiene un token de autenticación." +
            " Por defecto ya se encuentra registrado un ADMIN con credenciales de login: " +
            "email: admin@example.com, password: admin123")
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa")
    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody @Valid AuthLoginRequestDto authDto,
            HttpServletResponse servletResponse) {

        AuthResponseDto response = this.userDetailsService.loginUser(authDto);

        // Configurar cookie
        Cookie jwtCookie = new Cookie("jwt_token", response.getToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(jwtUtils.getExpirationTime());

        // Añadir política SameSite
        String cookieHeader = String.format("jwt_token=%s; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=%d",
                response.getToken(),
                jwtUtils.getExpirationTime());
        servletResponse.addHeader("Set-Cookie", cookieHeader);

        return ResponseEntity.ok()
                .header("user-id", response.getId().toString())
                .body(response);
    }

    @Operation(summary = "Registrar nuevo profesional", description = """
            Registra un nuevo profesional y obtiene un token de autenticación.
            """)
    @PostMapping(value = "/professional/register")
    public ResponseEntity<AuthResponseRegisterDto> registerProfessional(
            @RequestBody @Valid ProfessionalRequestDto authCreateUserDto,
            HttpServletResponse servletResponse) {

        AuthResponseRegisterDto response = professionalService.createUser(authCreateUserDto);

        // 3. Configurar cookie con el token
        Cookie jwtCookie = new Cookie("jwt_token", response.getToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(jwtUtils.getExpirationTime());

        // 4. Añadir política SameSite
        String cookieHeader = String.format(
                "jwt_token=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
                response.getToken(),
                jwtUtils.getExpirationTime()
        );
        servletResponse.addHeader("Set-Cookie", cookieHeader);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Registrar nuevo recepcionista", description = """
            Registra un nuevo recepcionista y obtiene un token de autenticación.
            """)
    @PostMapping(value = "/receptionist/register")
    public ResponseEntity<AuthResponseRegisterDto> registerReceptionist(
            @RequestBody @Valid ReceptionistRequestDto authCreateUserDto,
            HttpServletResponse servletResponse) {

        AuthResponseRegisterDto response = receptionistService.createUser(authCreateUserDto);

        // 3. Configurar cookie con el token
        Cookie jwtCookie = new Cookie("jwt_token", response.getToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(jwtUtils.getExpirationTime());

        // 4. Añadir política SameSite
        String cookieHeader = String.format(
                "jwt_token=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
                response.getToken(),
                jwtUtils.getExpirationTime()
        );
        servletResponse.addHeader("Set-Cookie", cookieHeader);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Suspender un usuario", description = """
            Suspende a un usuario por una duración y unidad de tiempo especificadas. Solo el ADMIN puede suspender un usuario. Ten en cuenta que la unidad 
            de tiempo puede ser "HOURS", "DAYS", "WEEKS" o "MONTHS".
             En ese lazo de tiempo el usuario no puede iniciar sesión.
            """)

    @PostMapping("/{userId}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    /**
     * Suspende a un usuario por una duración y unidad de tiempo especificadas.
     *
     * @param userId el ID del usuario a suspender
     * @param request la solicitud de suspensión que contiene la duración y la unidad de tiempo
     * @return un ResponseEntity que contiene la hora de finalización de la suspensión y el tiempo restante
     */
    public ResponseEntity<?> suspendUser(@PathVariable Long userId,
                                         @RequestBody SuspendRequestDto request) {
        userDetailsService.suspendUser(userId, request.getDuration(), request.getUnit());

        LocalDateTime suspensionEndTime = LocalDateTime.now().plus(request.getDuration(), mapToChronoUnit(request.getUnit()));
        long remainingTime = ChronoUnit.SECONDS.between(LocalDateTime.now(), suspensionEndTime);

        return ResponseEntity.ok().body("Usuario suspendido hasta: " +
                suspensionEndTime + " (Tiempo restante: " + remainingTime + " segundos)");
    }

    @Operation(summary = "Reactivar un usuario", description = """
            Reactiva a un usuario suspendido.
            """)
    @PostMapping("/{userId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        userDetailsService.activateUser(userId);
        return ResponseEntity.ok().body("Usuario reactivado exitosamente");
    }

    private ChronoUnit mapToChronoUnit(SuspendRequestDto.TimeUnit unit) {
        return switch (unit) {
            case HOURS -> ChronoUnit.HOURS;
            case DAYS -> ChronoUnit.DAYS;
            case WEEKS -> ChronoUnit.WEEKS;
            case MONTHS -> ChronoUnit.MONTHS;
        };
    }


}
