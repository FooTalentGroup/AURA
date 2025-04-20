package com.clinica.aura.entities.professional.controller;

import com.clinica.aura.entities.professional.dtoResponse.ProfessionalResponseDto;
import com.clinica.aura.entities.professional.model.ProfessionalModel;
import com.clinica.aura.entities.professional.service.ProfessionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@RestController
@RequestMapping("/professionals")
@RequiredArgsConstructor
@Tag(name = "Professional", description = "Endpoints para la gestión de profesionales")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    //método para obtener todos los professionals
    @Operation(summary = "Obtener todos los profesionales")
    @ApiResponse(responseCode = "200", description = "Listado de profesionales obtenido exitosamente")
    @GetMapping
    public ResponseEntity<List<ProfessionalResponseDto>> getAllProfessionals() {
        return ResponseEntity.ok(professionalService.getAllProfessionals());
    }

    // metodo para obtener professional por Id
    @Operation(summary = "Buscar profesional por ID")
    @ApiResponse(responseCode = "200", description = "Profesional encontrado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalResponseDto> getProfessionalById(@PathVariable Long id) {
        return ResponseEntity.ok(professionalService.getProfessionalById(id));
    }

    //metodo para paginar
    @Operation(
            summary = "Obtener profesionales con paginación",
            description = "Usá los parámetros 'page' y 'size' en la URL para controlar la paginación. Ejemplo: /professionals/page?page=0&size=5"
    )
    @ApiResponse(responseCode = "200", description = "Listado paginado de profesionales obtenido exitosamente")
    @GetMapping("/page")
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



    //actualizar un professional
    @Operation(summary = "Actualizar un profesional por ID")
    @ApiResponse(responseCode = "200", description = "Profesional actualizado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalResponseDto> updateProfessional(
            @PathVariable Long id,
            @RequestBody ProfessionalModel professionalModel) {
        return ResponseEntity.ok(professionalService.updateProfessional(id, professionalModel));
    }





    //eliminar un profesional
    @Operation(summary = "Eliminar un profesional por ID",
    description = "Realiza una eliminación lógica del profesional (no se borra físicamente de la base de datos)"
    )
    @ApiResponse(responseCode = "204", description = "Profesional eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessional(@PathVariable Long id) {
        professionalService.deleteProfessional(id);
        return ResponseEntity.noContent().build();
    }


}
