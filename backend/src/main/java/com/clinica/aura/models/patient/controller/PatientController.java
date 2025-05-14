package com.clinica.aura.models.patient.controller;



import com.clinica.aura.models.patient.dto.PatientRequestDto;
import com.clinica.aura.models.patient.dto.PatientResponseDto;
import com.clinica.aura.models.patient.service.PatientService;
import com.clinica.aura.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "Endpoints para manejar pacientes")
@PreAuthorize("hasRole('ADMIN') or hasRole('RECEPTIONIST') or hasRole('PROFESSIONAL')")
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Registrar nuevo paciente", description = """
            Registra un nuevo paciente, el campo professionalIds es opcional y se lo puede dejar en blanco, pero
            si se lo llena se debe asegurar de que sean ids de profesionales existentes en la base de datos.
                
            """)
    @PostMapping(value = "/register")
    @Tag(name = "Patient")
    public ResponseEntity<PatientResponseDto> registerPatient(@RequestBody @Valid PatientRequestDto authCreateUserDto) {
        PatientResponseDto response = patientService.createUser(authCreateUserDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Filtrar pacientes por paginación", description = "(Recordar que empieza desde 0) y size 1")
    public ResponseEntity<PaginatedResponse<PatientResponseDto>> getAllPatients
    (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(patientService.getAllPatients(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Devuelve los datos de un paciente específico según su ID.")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente específico. No olvidar de cambiar los campos de example sino quedaran asi en la base de datos al ejecutar la prueba")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable("id") Long id, @RequestBody @Valid PatientRequestDto request) {
        PatientResponseDto patientResponseDto =  patientService.updatePatient(id, request);
        return new ResponseEntity<>(patientResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/search/dni")
    @Operation(summary = "Buscar paciente por dni", description = "Se busca un paciente por dni deben ingresarse los" +
            " 8 caracteres exactos.")
    public ResponseEntity<PatientResponseDto> getPatientByDni(
            @RequestParam(name = "dni") String dni) {
        return ResponseEntity.ok(patientService.getPatientByDni(dni));
    }

    @GetMapping("/search/name")
    @Operation(summary = "Buscar paciente por nombre", description = "Se busca un paciente por nombre, apellido o ambos" +
            " (coincidencia parcial o total) Se debe llenar al menos uno de los 2 campos.")
    public ResponseEntity<List<PatientResponseDto>> getPatientsByName(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "sureName", required = false) String sureName) {

        name = (name == null || name.trim().isEmpty()) ? null : name.trim();
        sureName = (sureName == null || sureName.trim().isEmpty()) ? null : sureName.trim();

        if (name == null && sureName == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(patientService.getPatientsByName(name,sureName));
    }

    @PutMapping("/{patient_id}/{school_id}")
    @Operation(summary = "Asignar escuela a paciente", description = "Asigna una escuela a un paciente, se debe ingresar el ID del paciente " +
            "en el path y en el cuerpo del request el ID de la escuela")
    public ResponseEntity<String> assignSchoolToPatient(@Schema(description = "ID del paciente", example = "1") @PathVariable("patient_id") Long patientId,
                                                        @Schema(description = "ID de la escuela", example = "1", title = "ID de la escuela" ) @RequestBody Long schoolId) {
        patientService.assignSchoolToPatient(patientId, schoolId);
        return ResponseEntity.ok("Escuela asignada al paciente correctamente");
    }

}
