package com.clinica.aura.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<CustomErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    CustomErrorResponse error = new CustomErrorResponse(
            "NOT_FOUND",
            ex.getMessage(),
            List.of(ex.getMessage()),
            LocalDateTime.now(),
            null // luego le ponemos el path desde un filtro o controller, si querés
    );
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex) {
    CustomErrorResponse error = new CustomErrorResponse(
            "SERVER-001",
            "Ha ocurrido un error inesperado",
            List.of(ex.getMessage()),
            LocalDateTime.now(),
            null
    );
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
