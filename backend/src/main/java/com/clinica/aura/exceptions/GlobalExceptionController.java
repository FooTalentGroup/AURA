package com.clinica.aura.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        // 1. Sanitizar nombres de campos sensibles y manejar mensajes nulos
        List<String> sanitizedDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    String fieldName = sanitizeFieldName(error.getField());
                    String message = Optional.ofNullable(error.getDefaultMessage())
                            .orElse("Validation constraint violated");
                    return String.format("%s: %s", fieldName, message);
                })
                .sorted(Comparator.comparing(detail -> detail.split(":")[0])) // Orden consistente
                .toList();

        // 2. Construir respuesta estándar con metadata
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("VALIDATION-001")
                .message("Errores de validación en la solicitud")
                .details(!sanitizedDetails.isEmpty() ? sanitizedDetails : List.of("Error de validación no especificado"))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        // 3. Logging estructurado seguro
        log.warn("Validation Error - Path: {} | IP: {} | Errors: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                sanitizedDetails.stream()
                        .map(detail -> detail.replaceAll("(\r\n|\n|\r)", ""))
                        .collect(Collectors.joining("; ")));

        // 4. Respuesta con headers de seguridad
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Validation-Error", "true")
                .body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            WebRequest request) {

        // 1. Parseo inteligente del mensaje de error
        String rootCauseMessage = extractRootCauseMessage(ex);

        // 2. Construcción de respuesta segura y estandarizada
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("PARSE-001")
                .message("Cuerpo de solicitud inválido")
                .details(List.of(
                        sanitizeJsonErrorMessage(rootCauseMessage),
                        "Revise la sintaxis JSON y los tipos de datos"
                ))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        // 3. Logging estructurado con contexto
        log.warn("JSON Parse Error - Path: {} | IP: {} | ErrorType: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                classifyJsonError(rootCauseMessage));

        // 4. Respuesta con headers de seguridad
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .header("Accept", "application/json")
                .body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(
            MissingServletRequestParameterException ex,
            WebRequest request) {

        // 1. Construcción del mensaje amigable y detalle técnico
        String paramName = sanitizeParamName(ex.getParameterName());
        String userMessage = "Falta un parámetro requerido en la solicitud";
        String detailMessage = String.format("Parámetro faltante: %s (%s)", paramName, ex.getParameterType());

        // 2. Construcción del error
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("PARAM-001")
                .message(userMessage)
                .details(List.of(detailMessage))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        // 3. Logging estructurado
        log.warn("Missing Param - Path: {} | Param: {} | Type: {}",
                errorResponse.getPath(),
                paramName,
                ex.getParameterType());

        // 4. Respuesta con headers de seguridad
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {

        // 1. Procesar y sanitizar violaciones
        List<String> sanitizedDetails = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String path = sanitizePropertyPath(violation.getPropertyPath().toString());
                    String message = Optional.ofNullable(violation.getMessage())
                            .orElse("Violación de restricción");
                    return String.format("%s: %s", path, message);
                })
                .sorted(Comparator.comparing(detail -> detail.split(":")[0])) // Ordenar por propiedad
                .toList();

        // 2. Construcción de respuesta estándar
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("VALIDATION-002")
                .message("Violaciones de restricción en los parámetros")
                .details(!sanitizedDetails.isEmpty() ? sanitizedDetails : List.of("Violaciones no especificadas"))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        // 3. Logging estructurado
        log.warn("ConstraintViolation - Path: {} | IP: {} | Violations: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                String.join("; ", sanitizedDetails));

        // 4. Respuesta con headers seguros
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Validation-Error", "true")
                .body(errorResponse);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("HTTP-405")
                .message("Método HTTP no permitido")
                .details(List.of("Método no soportado: " + sanitizeErrorMessage(ex.getMethod())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Método no permitido - Path: {} | IP: {} | Método: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMethod());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("RESOURCE-404")
                .message("El recurso solicitado no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Entity Not Found - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            WebRequest request) {

        // 1. Manejo seguro de mensajes nulos o vacíos
        String errorDetail = Optional.ofNullable(ex.getMessage())
                .filter(msg -> !msg.isBlank())
                .orElse("Credenciales inválidas (sin detalles adicionales)"); // Mensaje por defecto

        // 2. Evitar exponer información sensible
        String sanitizedMessage = sanitizeErrorMessage(errorDetail);

        // 3. Logging seguro y estructurado
        log.warn("Intento de autenticación fallido - IP: {}, User-Agent: {} | Razón: {}",
                request.getHeader("X-Forwarded-For"),
                request.getHeader("User-Agent"),
                sanitizedMessage);

        // 4. Respuesta normalizada
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-001")
                .message("Credenciales incorrectas")
                .details(List.of(sanitizedMessage))
                .timestamp(Instant.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(
            UsernameNotFoundException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-404")
                .message("El usuario no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Usuario no encontrado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Auth-Error", "true")
                .body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(
            UnauthorizedAccessException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-403")
                .message("Acceso no autorizado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Acceso no autorizado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Auth-Error", "true")
                .body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {

        // 1. Analizar la excepción para determinar el tipo de violación
        String violationDetail = extractViolationDetail(ex);
        String sanitizedDetail = sanitizeConstraintViolation(violationDetail);

        // 2. Construir respuesta estándar
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("CONFLICT-001")
                .message("Conflicto con los datos proporcionados")
                .details(List.of(!sanitizedDetail.isEmpty() ?
                        sanitizedDetail : "El recurso ya existe o viola restricciones de unicidad"))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        // 3. Logging estructurado seguro (sin exponer datos sensibles)
        log.warn("Data Integrity Violation - Path: {} | IP: {} | Violation: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                sanitizedDetail.replaceAll("(\r\n|\n|\r)", ""));

        // 4. Respuesta con headers de seguridad
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Conflict-Error", "true")
                .body(errorResponse);
    }

    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProfessionalNotFoundException(ProfessionalNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("RESOURCE-404")
                .message("El profesional solicitado no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Profesional no encontrado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }
    @ExceptionHandler(ReceptionistNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReceptionistNotFoundException(ReceptionistNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("RESOURCE-404")
                .message("El recepcionista solicitado no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Recepcionista no encontrado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("RESOURCE-404")
                .message("El usuario solicitado no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Usuario no se encuentra - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-403")
                .message("El usuario se encuentra deshabilitado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("El usuario se encuentra deshabilitado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(LockedException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("AUTH-403")
                .message("El usuario se encuentra bloqueado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("El usuario se encuentra bloqueado - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("RESOURCE-409")
                .message("El correo electrónica ya existe")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("El correo electrónica ya existe - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePatientNotFoundException(PatientNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("RESOURCE-404")
                .message("El paciente solicitado no fue encontrado")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Paciente no se encuentra - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    @ExceptionHandler(ConflictWithExistingRecord.class)
    public ResponseEntity<ErrorResponse> handleConflictWithExistingRecord(ConflictWithExistingRecord ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("RESOURCE-409")
                .message("El registro ya existe")
                .details(List.of(sanitizeErrorMessage(ex.getMessage())))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("El registro ya existe - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    @ExceptionHandler(SchoolNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSchoolNotFoundException(SchoolNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("RESOURCE-404")
                .message(ex.getMessage())
                .details(List.of("La escuela con el ID especificado no fue encontrada"))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        log.warn("Escuela no encontrada - Path: {} | IP: {} | Mensaje: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        // 1. Obtener mensaje raíz o fallback
        String rootMessage = Optional.ofNullable(ex.getMessage()).orElse("Error inesperado");

        // 2. Construcción de respuesta
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("SERVER-001")
                .message("Ha ocurrido un error inesperado")
                .details(List.of(sanitizeErrorMessage(rootMessage)))
                .timestamp(Instant.now())
                .path(getSanitizedPath(request))
                .build();

        // 3. Logging estructurado
        log.error("Unhandled Exception - Path: {} | IP: {} | Exception: {}",
                errorResponse.getPath(),
                request.getHeader("X-Forwarded-For"),
                ex.toString());

        // 4. Encabezados de seguridad
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("X-Content-Type-Options", "nosniff")
                .body(errorResponse);
    }

    // --- Métodos auxiliares ---

    private String extractViolationDetail(DataIntegrityViolationException ex) {
        // 1. Obtener la causa raíz de forma segura
        Throwable rootCause = ex.getRootCause();
        if (rootCause == null) {
            return "Error de integridad de datos no especificado";
        }

        // 2. Manejar los casos más comunes de violación de unicidad
        String errorMessage = rootCause.getMessage();
        if (errorMessage == null) {
            return "Violación de restricción de base de datos";
        }

        // 3. Extraer información relevante según el motor de base de datos
        try {
            // Caso MySQL/MariaDB
            if (errorMessage.contains("Duplicate entry") && errorMessage.contains("for key")) {
                int start = errorMessage.indexOf("for key '") + 9;
                int end = errorMessage.indexOf("'", start);
                return end > start ? "El valor ya existe para: " + errorMessage.substring(start, end) : errorMessage;
            }

            // Caso PostgreSQL
            if (errorMessage.contains("violates unique constraint")) {
                int start = errorMessage.indexOf("\"") + 1;
                int end = errorMessage.indexOf("\"", start);
                return end > start ? "Restricción única violada: " + errorMessage.substring(start, end) : errorMessage;
            }

            // Caso H2
            if (errorMessage.contains("unique constraint violated")) {
                int start = errorMessage.indexOf(": ") + 2;
                int end = errorMessage.indexOf(" ", start);
                return end > start ? "Restricción única: " + errorMessage.substring(start, end) : errorMessage;
            }

            // Caso genérico para otros motores
            return errorMessage.length() > 200 ? errorMessage.substring(0, 200) + "..." : errorMessage;

        } catch (Exception e) {
            log.warn("Error al parsear mensaje de violación de constraint", e);
            return "Error de duplicado (no se pudo determinar el campo)";
        }
    }

    private String sanitizeConstraintViolation(String detail) {
        // Sanitizar detalles técnicos para el cliente
        return detail.replaceAll("(Duplicate entry ')(.*?)(' for key)", "$1[REDACTED]$3")
                .replaceAll("(constraint \\[)(\\w+)(\\])", "$1[REDACTED]$3");
    }



    // Método auxiliar para sanitizar mensajes
    private String sanitizeErrorMessage(String rawMessage) {
        return rawMessage.replaceAll("(\r\n|\n|\r)", "") // Elimina saltos de línea
                .replaceAll("password|token|secret", "[REDACTED]"); // Ofusca datos sensibles
    }

    // Métodos auxiliares
    private String sanitizeFieldName(String fieldName) {
        // Ofusca campos sensibles en los mensajes de error
        return fieldName.toLowerCase().contains("password") ? "credential" : fieldName;
    }

    private String getSanitizedPath(WebRequest request) {
        return request.getDescription(false)
                .replace("uri=", "")
                .replaceAll("[\\n\\r]", "");
    }

    private String extractRootCauseMessage(Throwable ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        return rootCause != null ?
                rootCause.getMessage() :
                "Error de sintaxis no especificado";
    }

    private String sanitizeJsonErrorMessage(String rawMessage) {
        return rawMessage.replaceAll("(\"|'|`|\\{|\\}|\\[|\\])", "")
                .replaceAll("password|token|secret", "[REDACTED]");
    }

    private String classifyJsonError(String message) {
        if (message.contains("com.fasterxml.jackson")) {
            return message.contains("UnrecognizedPropertyException") ?
                    "UNKNOWN_FIELD" :
                    "TYPE_MISMATCH";
        }
        return message.contains("JSON parse error") ?
                "SYNTAX_ERROR" :
                "MALFORMED_REQUEST";
    }

    private String sanitizeParamName(String name) {
        return name.toLowerCase().contains("token") || name.toLowerCase().contains("password")
                ? "[REDACTED]"
                : name;
    }
    private String sanitizePropertyPath(String path) {
        return path.toLowerCase().contains("password") ? "credential" : path.replaceAll("[\\n\\r]", "");
    }


}
