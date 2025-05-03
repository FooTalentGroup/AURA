package com.clinica.aura.models.professional.controller;

import com.clinica.aura.models.patient.dtoRequest.PatientResponseDto;
import com.clinica.aura.models.professional.dtoResponse.ProfessionalResponseDto;
import com.clinica.aura.models.professional.service.ProfessionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import com.clinica.aura.models.professional.dtoRequest.ProfessionalRequestDto;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/professionals")
@RequiredArgsConstructor
@Tag(name = "Professional", description = "Endpoints para la gestión de profesionales")
public class ProfessionalController {

    private final ProfessionalService professionalService;

//        //método para obtener todos los professionals
//        @Operation(summary = "Obtener todos los profesionales")
//        @ApiResponse(responseCode = "200", description = "Listado de profesionales obtenido exitosamente")
//        @GetMapping
//        public ResponseEntity<List<ProfessionalResponseDto>> getAllProfessionals() {
//            return ResponseEntity.ok(professionalService.getAllProfessionals());
//        }

    // metodo para obtener professional por Id
    @Operation(summary = "Buscar profesional por ID")
    @ApiResponse(responseCode = "200", description = "Profesional encontrado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalResponseDto> getProfessionalById(@PathVariable Long id) {
        ProfessionalResponseDto professionalResponse = professionalService.getProfessionalById(id);
        return ResponseEntity.ok(professionalResponse);
    }

    //metodo para paginar
    @Operation(
            summary = "Obtener profesionales con paginación",
            description = "Usá los parámetros 'page' y 'size' en la URL para controlar la paginación. Ejemplo: /professionals/page?page=0&size=5"
    )
    @ApiResponse(responseCode = "200", description = "Listado paginado de profesionales obtenido exitosamente")
    @GetMapping()
    public ResponseEntity<Page<ProfessionalResponseDto>> getProfessionalsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(professionalService.getProfessionalsPaginated(page, size));
    }

    //filtra por apellido o especialidad
    @Operation(
            summary = "Buscar profesionales por apellido o especialidad",
            description = "Busca profesionales por apellido o especialidad. Ejemplo: `/professionals/search?keyword=pediatria`"
    )
    @GetMapping("/search")
    public ResponseEntity<List<ProfessionalResponseDto>> searchProfessionals(@RequestParam String keyword) {
        return ResponseEntity.ok(professionalService.searchProfessionals(keyword));
    }




// Actualizar un profesional por ID
@Operation(summary = "Actualizar un profesional por ID")
@ApiResponse(responseCode = "200", description = "Profesional actualizado")
@ApiResponse(responseCode = "404", description = "Profesional no encontrado")
@PutMapping("/{id}")
public ResponseEntity<ProfessionalResponseDto> updateProfessional(
        @PathVariable Long id,
        @Valid @RequestBody ProfessionalRequestDto requestDto) {
    return ResponseEntity.ok(professionalService.updateProfessional(id, requestDto));
}
    // Eliminar un profesional (eliminación lógica)
    @Operation(summary = "Eliminar un profesional por ID",
            description = "Elimina lógicamente al profesional (no se borra de la base de datos)")
    @ApiResponse(responseCode = "204", description = "Profesional eliminado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessional(@PathVariable Long id) {
        professionalService.deleteProfessional(id);
        return ResponseEntity.noContent().build();
    }

    //obtener pacientes de un profesional

    @Operation(summary = "Listar pacientes de un profesional",
            description = "Devuelve una lista de los pacientes asociados al profesional especificado por su ID")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @GetMapping("/{id}/patients")
    public ResponseEntity<List<PatientResponseDto>> getPatientsByProfessionalId(@PathVariable Long id) {
        List<PatientResponseDto> patients = professionalService.getPatientsByProfessionalId(id);
        return ResponseEntity.ok(patients);
    }



}
