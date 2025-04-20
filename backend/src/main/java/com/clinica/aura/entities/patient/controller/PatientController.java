package com.clinica.aura.entities.patient.controller;



import com.clinica.aura.entities.patient.dtoRequest.PatientRequestDto;
import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.entities.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
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

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente específico. No olvidar de cambiar los campos de example sino quedaran asi en la bd al ejecutar la prueba")
    public ResponseEntity<Void> updatePatient(@PathVariable Long id, @RequestBody PatientRequestDto request) {
        patientService.updatePatient(id, request);
        return ResponseEntity.noContent().build();
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
            e.printStackTrace(); // 👈 Esto nos muestra el error en consola
            return ResponseEntity.status(500).body("Error al eliminar el paciente.");
        }
    }

}
