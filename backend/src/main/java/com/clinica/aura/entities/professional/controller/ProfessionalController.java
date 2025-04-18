package com.clinica.aura.entities.professional.controller;

import com.clinica.aura.entities.professional.dtoResponse.ProfessionalResponseDto;
import com.clinica.aura.entities.professional.model.ProfessionalModel;
import com.clinica.aura.entities.professional.service.ProfessionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professionals")
@RequiredArgsConstructor
@Tag(name = "Professional", description = "Endpoints para la gestión de profesionales")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    @Operation(summary = "Obtener todos los profesionales")
    @ApiResponse(responseCode = "200", description = "Listado de profesionales obtenido exitosamente")
    @GetMapping
    public ResponseEntity<List<ProfessionalResponseDto>> getAllProfessionals() {
        return ResponseEntity.ok(professionalService.getAllProfessionals());
    }

    @Operation(summary = "Buscar profesional por ID")
    @ApiResponse(responseCode = "200", description = "Profesional encontrado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalResponseDto> getProfessionalById(@PathVariable Long id) {
        return ResponseEntity.ok(professionalService.getProfessionalById(id));
    }
}
