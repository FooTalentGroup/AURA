package com.clinica.aura.entities.patient.controller;



import com.clinica.aura.entities.patient.dtoRequest.PatientRequestDto;
import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.entities.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "Endpoints para manejar pacientes")
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Registrar nuevo paciente", description = """
            Registra un nuevo paciente, el campo professionalIds es opcional y se lo puede dejar en blanco, pero
            si se lo llena se debe asegurarse de que sean ids de profesionales existentes en la base de datos
            """)
    @PostMapping(value = "/register")
    @Tag(name = "Patient")
    public ResponseEntity<PatientResponseDto> registerPatient(@RequestBody @Valid PatientRequestDto authCreateUserDto) {
        PatientResponseDto response = patientService.createUser(authCreateUserDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Filtrar pacientes por rango", description = "Devuelve los pacientes desde el índice 'from' hasta 'to' (Recordar que empieza desde 0)")
    public ResponseEntity<List<PatientResponseDto>> getPatientsByRange(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "9") int to
    ) {
        return ResponseEntity.ok(patientService.getPatientsByRange(from, to));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Devuelve los datos de un paciente específico según su ID.")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente específico. No olvidar de cambiar los campos de example sino quedaran asi en la bd al ejecutar la prueba")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable Long id, @RequestBody PatientRequestDto request) {
        PatientResponseDto patientResponseDto =  patientService.updatePatient(id, request);
        return new ResponseEntity<>(patientResponseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar paciente", description = "Elimina un paciente del sistema junto con su usuario, roles y datos personales.")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatientById(id);
            return ResponseEntity.ok("Paciente eliminado exitosamente.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al eliminar el paciente.");
        }
    }

    @GetMapping("/buscar/dni")
    @Operation(summary = "Buscar paciente por dni", description = "Se busca un paciente por dni.")
    public ResponseEntity<PatientResponseDto> getPatientByDni(
            @RequestParam String dni) {
        return ResponseEntity.ok(patientService.getPatientByDni(dni));
    }


}
