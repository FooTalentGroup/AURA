package com.clinica.aura.user.controller;

import com.clinica.aura.exceptions.GlobalExceptionController;
import com.clinica.aura.entities.user_account.controller.AuthController;
import com.clinica.aura.entities.user_account.dtoRequest.AuthLoginRequestDto;
import com.clinica.aura.entities.user_account.dtoResponse.AuthResponseDto;
import com.clinica.aura.entities.user_account.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class AuthControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserDetailsServiceImpl userDetailsService;

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
                    "Usuario logeado exitosamente", "jwt.token.here", true);

            when(userDetailsService.loginUser(any(AuthLoginRequestDto.class))).thenReturn(response);

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isOk(),
                            header().string(HttpHeaders.AUTHORIZATION, "Bearer jwt.token.here"),
                            jsonPath("$.id").value(1L),
                            jsonPath("$.token").exists(),
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
                            jsonPath("$.code").value("BAD_CREDENTIALS")
                    );
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
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.details").isArray(),
                            jsonPath("$.details[?(@ =~ /email/)i]").exists(),
                            jsonPath("$.details[?(@ =~ /password/)i]").exists()
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
