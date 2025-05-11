package com.clinica.aura.user.controller;

import com.clinica.aura.config.jwt.JwtUtils;
import com.clinica.aura.models.user_account.controller.AuthController;
import com.clinica.aura.models.user_account.dtoRequest.AuthLoginRequestDto;
import com.clinica.aura.models.user_account.dtoResponse.AuthResponseDto;
import com.clinica.aura.models.user_account.service.impl.UserDetailsServiceImpl;
import com.clinica.aura.exceptions.GlobalExceptionController;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.startsWith;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionController())
                .build();
    }

    @Nested
    @DisplayName("Login Success Cases")
    class SuccessScenarios {

        @Test
        @DisplayName("Debería retornar JWT válido con credenciales correctas")
        void validCredentials() throws Exception {
            var request = new AuthLoginRequestDto("admin@example.com", "admin123");
            var response = new AuthResponseDto(1L, "admin@example.com",
                    "Usuario logeado exitosamente", "jwt.token", true);

            // Simular las dependencias
            when(userDetailsService.loginUser(any(AuthLoginRequestDto.class))).thenReturn(response);
            when(jwtUtils.getExpirationTime()).thenReturn(3600); // Tiempo de expiración controlado

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isOk(),
                            // Verificar la cookie
                            cookie().value("jwt_token", "jwt.token"),
                            cookie().httpOnly("jwt_token", true),
                            cookie().secure("jwt_token", true),
                            cookie().maxAge("jwt_token", 3600),
                            cookie().path("jwt_token", "/"),
                            // Verificar SameSite en el encabezado
                            header().string("Set-Cookie", containsString("SameSite=Strict")),
                            // Verificar encabezado user-id
                            header().string("user-id", "1"),
                            // Verificar cuerpo de la respuesta
                            jsonPath("$.id").value(1L),
                            jsonPath("$.token").value("jwt.token"),
                            jsonPath("$.success").value(true)
                    );
            verify(userDetailsService).loginUser(any(AuthLoginRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Login Failure Cases")
    class FailureScenarios {

        @ParameterizedTest
        @MethodSource("invalidCredentialsProvider")
        @DisplayName("Debería fallar con credenciales inválidas")
        void invalidCredentials(String email, String password) throws Exception {
            var request = new AuthLoginRequestDto(email, password);

            when(userDetailsService.loginUser(any()))
                    .thenThrow(BadCredentialsException.class);

            mockMvc.perform(post("/auth/login")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isUnauthorized(),
                            jsonPath("$.errorCode").value("AUTH-001")
                    );
            verify(userDetailsService).loginUser(any());
        }

        static Stream<Arguments> invalidCredentialsProvider() {
            return Stream.of(
                    Arguments.of("wrong@email.com", "admin123"), // Email incorrecto
                    Arguments.of("admin@example.com", "wrongpass"), // Password incorrecto
                    Arguments.of("invalid-email", "admin123") // Formato email inválido
            );
        }

        @Test
        @DisplayName("Debería fallar con request body inválido")
        void invalidRequestBody() throws Exception {
            String invalidJson = """
            {
                "email": "not-an-email",
                "password": ""
            }
            """;

            mockMvc.perform(post("/auth/login")
                            .content(invalidJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details[0]").value(startsWith("credential")))
                    .andExpect(jsonPath("$.details[1]").value(startsWith("email"))
                    );
        }


    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

}
