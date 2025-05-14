package com.clinica.aura.models.professional.controller;

import com.clinica.aura.models.patient.dto.PatientResponseDto;
import com.clinica.aura.models.professional.dtoResponse.ProfessionalResponseDto;
import com.clinica.aura.models.professional.service.ProfessionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import com.clinica.aura.models.professional.dtoRequest.ProfessionalRequestDto;
import jakarta.validation.Valid;

import java.util.List;
/**
 * Controlador REST para gestionar operaciones relacionadas con profesionales.
 */
@RestController
@RequestMapping("/professionals")
@RequiredArgsConstructor
@Tag(name = "Professional", description = "Endpoints para la gestión de profesionales")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    /**
     * Busca un profesional por su ID.
     *
     * @param id el ID del profesional a buscar
     * @return un ResponseEntity que contiene el profesional encontrado o un error si no se encuentra
     */
    @Operation(summary = "Buscar profesional por ID")
    @ApiResponse(responseCode = "200", description = "Profesional encontrado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONAL')")
    public ResponseEntity<ProfessionalResponseDto> getProfessionalById(@PathVariable Long id) {
        ProfessionalResponseDto professionalResponse = professionalService.getProfessionalById(id);
        return ResponseEntity.ok(professionalResponse);
    }

    /**
     * Obtiene una lista paginada de profesionales.
     *
     * @param page número de página (por defecto 0)
     * @param size cantidad de elementos por página (por defecto 10)
     * @return un ResponseEntity con la lista paginada de profesionales
     */
    @Operation(
            summary = "Obtener profesionales con paginación",
            description = "Usá los parámetros 'page' y 'size' en la URL para controlar la paginación. Ejemplo: /professionals/page?page=0&size=5"
    )
    @ApiResponse(responseCode = "200", description = "Listado paginado de profesionales obtenido exitosamente")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONAL')")
    public ResponseEntity<Page<ProfessionalResponseDto>> getProfessionalsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(professionalService.getProfessionalsPaginated(page, size));
    }

    /**
     * Busca profesionales por apellido o especialidad.
     *
     * @param keyword palabra clave para buscar (puede ser parte del apellido o la especialidad)
     * @return un ResponseEntity con la lista de profesionales que coinciden con la búsqueda
     */
    @Operation(
            summary = "Buscar profesionales por apellido o especialidad",
            description = "Busca profesionales por apellido o especialidad. Ejemplo: `/professionals/search?keyword=pediatria`"
    )
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONAL')")
    public ResponseEntity<List<ProfessionalResponseDto>> searchProfessionals(@RequestParam String keyword) {
        return ResponseEntity.ok(professionalService.searchProfessionals(keyword));
    }




    /**
     * Actualiza los datos de un profesional existente por su ID.
     *
     * @param id ID del profesional a actualizar
     * @param requestDto objeto con los nuevos datos del profesional
     * @return un ResponseEntity con el profesional actualizado
     */
    @Operation(summary = "Actualizar un profesional por ID")
    @ApiResponse(responseCode = "200", description = "Profesional actualizado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONAL')")
    public ResponseEntity<ProfessionalResponseDto> updateProfessional(
            @PathVariable Long id,
            @Valid @RequestBody ProfessionalRequestDto requestDto) {
        return ResponseEntity.ok(professionalService.updateProfessional(id, requestDto));
    }

    /**
     * Elimina lógicamente un profesional por su ID.
     *
     * @param id ID del profesional a eliminar
     * @return un ResponseEntity sin contenido si la eliminación fue exitosa
     */
    @Operation(summary = "Eliminar un profesional por ID",
            description = "Elimina lógicamente al profesional (no se borra de la base de datos)")
    @ApiResponse(responseCode = "204", description = "Profesional eliminado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProfessional(@PathVariable Long id) {
        professionalService.deleteProfessional(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene la lista de pacientes asignados a un profesional específico.
     *
     * @param id ID del profesional
     * @return un ResponseEntity con la lista de pacientes asociados al profesional
     */
    @Operation(summary = "Listar pacientes de un profesional",
            description = "Devuelve una lista de los pacientes asociados al profesional especificado por su ID")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @GetMapping("/{id}/patients")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONAL')")
    public ResponseEntity<List<PatientResponseDto>> getPatientsByProfessionalId(@PathVariable Long id) {
        List<PatientResponseDto> patients = professionalService.getPatientsByProfessionalId(id);
        return ResponseEntity.ok(patients);
    }



}
