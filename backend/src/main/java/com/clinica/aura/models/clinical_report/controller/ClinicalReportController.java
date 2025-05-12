package com.clinica.aura.models.clinical_report.controller;




import com.clinica.aura.models.clinical_report.dto.ClinicalReportRequestDto;
import com.clinica.aura.models.clinical_report.dto.ClinicalReportResponseDto;
import com.clinica.aura.models.clinical_report.service.ClinicalReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Clinical Report", description = "Operaciones relacionadas con los informes clínicos")
@RestController
@RequestMapping("/clinical-reports")
public class ClinicalReportController {

    private final ClinicalReportService clinicalReportService;

    public ClinicalReportController(ClinicalReportService clinicalReportService) {
        this.clinicalReportService = clinicalReportService;
    }

    @Operation(
            summary = "Crear un nuevo informe clínico",
            description = "Crear un nuevo informe clínico. Solo un profesional autenticado puede crear un informe."
    )
    @PreAuthorize("hasRole('PROFESSIONAL')")
    @PostMapping
    public ResponseEntity<ClinicalReportResponseDto> createReport(
            @Valid @RequestBody ClinicalReportRequestDto requestDto) {
        ClinicalReportResponseDto response = clinicalReportService.create(requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener todos los informes clínicos",
            description = "Recupera todos los informes clínicos. No requiere autorización."
    )
    @GetMapping
    public ResponseEntity<List<ClinicalReportResponseDto>> getAllReports() {
        List<ClinicalReportResponseDto> reports = clinicalReportService.findAll();
        return ResponseEntity.ok(reports);
    }

    @Operation(
            summary = "Obtener un informe clínico por ID",
            description = "Recupera un informe clínico por ID. No requiere autorización."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClinicalReportResponseDto> getReportById(
            @Parameter(description = "ID del informe clínico") @PathVariable Long id) {
        ClinicalReportResponseDto report = clinicalReportService.findById(id);
        return ResponseEntity.ok(report);
    }

    @Operation(
            summary = "Eliminar un informe clínico por ID",
            description = "Elimina un informe clínico por ID. Solo un profesional autenticado puede eliminar un informe."
    )
    @PreAuthorize("hasRole('PROFESSIONAL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(
            @Parameter(description = "ID del informe clínico a eliminar") @PathVariable Long id) {
        clinicalReportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar un informe clínico por ID",
            description = "Actualiza un informe clínico por ID. Solo un profesional autenticado puede actualizar un informe."
    )
    @PreAuthorize("hasRole('PROFESSIONAL')")
    @PutMapping("/{id}")
    public ResponseEntity<ClinicalReportResponseDto> updateReport(
            @Parameter(description = "ID del informe clínico a actualizar") @PathVariable Long id,
            @Valid @RequestBody ClinicalReportRequestDto requestDto) {
        ClinicalReportResponseDto response = clinicalReportService.update(id, requestDto);
        return ResponseEntity.ok(response);
    }
}
