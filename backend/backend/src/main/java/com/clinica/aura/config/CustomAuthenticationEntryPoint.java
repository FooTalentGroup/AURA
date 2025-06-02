package com.clinica.aura.config;

import com.clinica.aura.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;


    /**
     * Maneja las excepciones de autenticación (HTTP 401).
     *
     * @param request la solicitud HTTP que generó el error
     * @param response la respuesta HTTP para enviar al cliente
     * @param authException la excepción de autenticación
     *
     * La respuesta incluye:
     * 1. Código de error: "AUTH-401"
     * 2. Mensaje: "No estás autenticado"
     * 3. Detalles: "Debes iniciar sesión para acceder a este recurso"
     * 4. Timestamp: Fecha y hora actual
     * 5. Path: URL que generó el error
     *
     * La respuesta se envía en formato JSON con charset UTF-8 y código HTTP 401 (UNAUTHORIZED)
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ErrorResponse error = ErrorResponse.builder()
                .errorCode("AUTH-401")
                .message("No estás autenticado")
                .details(List.of("Debes iniciar sesión para acceder a este recurso"))
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
