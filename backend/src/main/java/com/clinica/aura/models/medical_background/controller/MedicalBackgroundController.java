package com.clinica.aura.models.medical_background.controller;


import com.clinica.aura.models.medical_background.dto.MedicalBackgroundRequestDto;
import com.clinica.aura.models.medical_background.dto.MedicalBackgroundResponseDto;

import com.clinica.aura.models.medical_background.service.MedicalBackgroundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Medical Background", description = "Operaciones relacionadas con los antecedentes médicos")
@RestController
@RequestMapping("/medical-backgrounds")
public class MedicalBackgroundController {

    private final MedicalBackgroundService medicalBackgroundService;

    public MedicalBackgroundController(MedicalBackgroundService medicalBackgroundService) {
        this.medicalBackgroundService = medicalBackgroundService;
    }

    // Crear antecedentes médicos
    @Operation(
            summary = "Crea un nuevo antecedente médico",
            description = "Solo un profesional autenticado puede crear antecedentes médicos."
    )
    @PostMapping
    public ResponseEntity<MedicalBackgroundResponseDto> create(@RequestBody @Valid MedicalBackgroundRequestDto dto) {
        MedicalBackgroundResponseDto createdBackground = medicalBackgroundService.create(dto);
        return new ResponseEntity<>(createdBackground, HttpStatus.CREATED);
    }

    // Obtener antecedentes médicos por ID
    @Operation(summary = "Obtener todos los antecedentes médicos por ID")
    @GetMapping("/{id}")
    public ResponseEntity<MedicalBackgroundResponseDto> getById(@PathVariable Long id) {
        MedicalBackgroundResponseDto background = medicalBackgroundService.findById(id);
        return new ResponseEntity<>(background, HttpStatus.OK);
    }

    // Obtener antecedentes médicos por ID de paciente
    @Operation(summary = "Obtener todos los antecedentes médicos por ID de paciente")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<MedicalBackgroundResponseDto> getByPatientId(@PathVariable Long patientId) {
        MedicalBackgroundResponseDto background = medicalBackgroundService.findByPatientId(patientId);
        return new ResponseEntity<>(background, HttpStatus.OK);
    }

    // Obtener todos los antecedentes médicos
    @Operation(summary = "Obtener todos los antecedentes médicos")
    @GetMapping
    public ResponseEntity<List<MedicalBackgroundResponseDto>> getAll() {
        List<MedicalBackgroundResponseDto> backgrounds = medicalBackgroundService.findAll();
        return new ResponseEntity<>(backgrounds, HttpStatus.OK);
    }

    // Actualizar antecedentes médicos
    @Operation(
            summary = "Actualizar un antecedente médico por ID",
            description = "Solo un profesional autenticado puede actualizar antecedentes médicos."
    )
    @PutMapping("/{id}")
    public ResponseEntity<MedicalBackgroundResponseDto> update(@PathVariable Long id,
                                                               @RequestBody @Valid MedicalBackgroundRequestDto dto) {
        MedicalBackgroundResponseDto updatedBackground = medicalBackgroundService.update(id, dto);
        return new ResponseEntity<>(updatedBackground, HttpStatus.OK);
    }

    // Eliminar antecedentes médicos
    @Operation(
            summary = "Eliminar un antecedente médico por ID",
            description = "Solo un profesional autenticado puede eliminar antecedentes médicos."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicalBackgroundService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
