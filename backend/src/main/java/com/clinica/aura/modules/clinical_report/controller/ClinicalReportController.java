package com.clinica.aura.modules.clinical_report.controller;




import com.clinica.aura.modules.clinical_report.dto.ClinicalReportRequestDto;
import com.clinica.aura.modules.clinical_report.dto.ClinicalReportResponseDto;
import com.clinica.aura.modules.clinical_report.service.ClinicalReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar informes clínicos.
 * Proporciona endpoints para crear, obtener, actualizar y eliminar informes clínicos.
 */
@Tag(name = "Clinical Report", description = "Endpoints para la gestión de informes clínicos")
@RestController
@RequestMapping("/clinical-reports")
public class ClinicalReportController {

    private final ClinicalReportService clinicalReportService;

    public ClinicalReportController(ClinicalReportService clinicalReportService) {
        this.clinicalReportService = clinicalReportService;
    }

    /**
     * Crea un nuevo informe clínico.
     *
     * @param requestDto Datos del informe clínico a crear.
     * @return El informe clínico creado.
     */
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

    /**
     * Obtiene todos los informes clínicos.
     *
     * @return Lista de todos los informes clínicos registrados.
     */
    @Operation(
            summary = "Obtener todos los informes clínicos",
            description = "Recupera todos los informes clínicos. No requiere autorización."
    )
    @GetMapping
    public ResponseEntity<List<ClinicalReportResponseDto>> getAllReports() {
        List<ClinicalReportResponseDto> reports = clinicalReportService.findAll();
        return ResponseEntity.ok(reports);
    }

    /**
     * Obtiene un informe clínico por su ID.
     *
     * @param id ID del informe clínico.
     * @return El informe clínico correspondiente al ID proporcionado.
     */
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

    /**
     * Elimina un informe clínico por su ID.
     *
     * @param id ID del informe clínico a eliminar.
     * @return Respuesta sin contenido si la eliminación fue exitosa.
     */
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

    /**
     * Actualiza un informe clínico por su ID.
     *
     * @param id         ID del informe clínico a actualizar.
     * @param requestDto Datos actualizados del informe clínico.
     * @return El informe clínico actualizado.
     */
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
