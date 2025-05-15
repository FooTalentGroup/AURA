package com.clinica.aura.modules.diagnoses.controller;

import com.clinica.aura.modules.diagnoses.dtoRequest.DiagnosesRequestDto;
import com.clinica.aura.modules.diagnoses.dtoRequest.DiagnosesRequestUpdateDto;
import com.clinica.aura.modules.diagnoses.dtoResponse.DiagnosesResponseDto;
import com.clinica.aura.modules.diagnoses.service.DiagnosesService;
import com.clinica.aura.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diagnoses")
@Tag(name = "Diagnoses", description = "Diagnoses")
@PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONAL')")
public class DiagnosesController {
    private final DiagnosesService diagnosesService;

    @Operation(summary = "Crear diagnostico", description = "Crea un diagnostico a partir de una historia clinica")
    @PostMapping("/create")
    public ResponseEntity<DiagnosesResponseDto> createDiagnoses(@RequestBody DiagnosesRequestDto diagnosesRequestDto) {
        DiagnosesResponseDto diagnosesResponseDto = diagnosesService.createDiagnoses(diagnosesRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(diagnosesResponseDto);
    }

    @Operation(summary = "Actualizar diagnostico", description = "Actualiza un diagnostico, verifica que el id del diagnostico exista")
    @PatchMapping("/update/{id}")
    public ResponseEntity<DiagnosesResponseDto> updateDiagnoses(@PathVariable Long id, @RequestBody DiagnosesRequestUpdateDto diagnosesRequestDto) {
        DiagnosesResponseDto diagnosesResponseDto = diagnosesService.updateDiagnoses(id, diagnosesRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(diagnosesResponseDto);
    }

    @Operation(summary = "Obtener todos los diagnosticos", description = "Obtiene todos los diagnosticos")
    @GetMapping
    public ResponseEntity<PaginatedResponse<DiagnosesResponseDto>> getAllDiagnoses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(diagnosesService.getAllDiagnoses(page, size));
    }

    @Operation(summary = "Obtener diagnostico por id", description = "Obtiene un diagnostico por id")
    @GetMapping("/{id}")
    public ResponseEntity<DiagnosesResponseDto> getDiagnosesById(@PathVariable Long id) {
        DiagnosesResponseDto diagnosesResponseDto = diagnosesService.getDiagnosesById(id);
        return ResponseEntity.status(HttpStatus.OK).body(diagnosesResponseDto);
    }

    @Operation(summary = "Eliminar diagnostico por id", description = "Elimina un diagnostico por id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiagnosesById(@PathVariable Long id) {
        diagnosesService.deleteDiagnoses(id);
        return ResponseEntity.status(HttpStatus.OK).body("Diagnostico eliminado correctamente");
    }


}
