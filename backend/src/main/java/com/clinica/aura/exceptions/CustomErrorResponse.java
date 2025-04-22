package com.clinica.aura.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CustomErrorResponse {
    private String errorCode;
    private String message;
    private List<String> details;
    private LocalDateTime timestamp;
    private String path;
}
