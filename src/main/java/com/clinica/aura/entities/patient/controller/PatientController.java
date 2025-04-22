package com.clinica.aura.entities.patient.controller;



import com.clinica.aura.entities.patient.dtoRequest.PatientRequestDto;
import com.clinica.aura.entities.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.entities.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

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
