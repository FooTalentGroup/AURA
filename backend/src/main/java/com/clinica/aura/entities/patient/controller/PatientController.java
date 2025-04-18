package com.clinica.aura.entities.patient.controller;



import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.entities.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @Operation(summary = "Obtener todos los pacientes", description = "Devuelve una lista de todos los pacientes registrados en el sistema.")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Devuelve los datos de un paciente específico según su ID.")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }
}
