package com.clinica.aura.entities.user_account.controller;

import com.clinica.aura.entities.patient.dtoRequest.PatientRequestDto;
import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.entities.patient.model.PatientModel;
import com.clinica.aura.entities.patient.service.PatientService;
import com.clinica.aura.entities.professional.dtoRequest.ProfessionalRequestDto;
import com.clinica.aura.entities.professional.service.ProfessionalService;
import com.clinica.aura.entities.user_account.dtoRequest.AuthLoginRequestDto;
import com.clinica.aura.entities.user_account.dtoResponse.AuthResponseDto;
import com.clinica.aura.entities.user_account.dtoResponse.AuthResponseRegisterDto;
import com.clinica.aura.entities.user_account.service.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final ProfessionalService professionalService;
    private final UserDetailsServiceImpl userDetailsService;
    private final PatientService patientService;

    @Operation(summary = "Iniciar sesión", description = "Inicia sesión y obtiene un token de autenticación." +
            " Por defecto ya se encuentra registrado un ADMIN con credenciales de login: " +
            "email: admin@example.com, password: admin123")
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa")
    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid AuthLoginRequestDto authDto) {
        AuthResponseDto response = this.userDetailsService.loginUser(authDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken())
                .header("user-id", response.getId().toString())
                .body(response);
    }

    @Operation(summary = "Registrar nuevo profesional", description = """
            Registra un nuevo profesional y obtiene un token de autenticación.
            """)
    @PostMapping(value = "/professional/register")
    public ResponseEntity<AuthResponseRegisterDto> registerProfessional(@RequestBody @Valid ProfessionalRequestDto authCreateUserDto) {
        AuthResponseRegisterDto response = professionalService.createUser(authCreateUserDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
