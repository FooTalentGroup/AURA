package com.clinica.aura.config;

import com.clinica.aura.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * Maneja las excepciones de acceso denegado (HTTP 403).
     *
     * @param request la solicitud HTTP que generó el error
     * @param response la respuesta HTTP para enviar al cliente
     * @param accessDeniedException la excepción de acceso denegado
     *
     * La respuesta incluye:
     * 1. Código de error: "AUTH-403"
     * 2. Mensaje: "No tienes permiso para acceder"
     * 3. Detalles: "Este recurso requiere más privilegios"
     * 4. Timestamp: Fecha y hora actual
     * 5. Path: URL que generó el error
     *
     * La respuesta se envía en formato JSON con charset UTF-8 y código HTTP 403 (FORBIDDEN)
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponse error = ErrorResponse.builder()
                .errorCode("AUTH-403")
                .message("No tienes permiso para acceder")
                .details(List.of("Este recurso requiere más privilegios"))
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
