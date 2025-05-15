package com.clinica.aura.modules.school.controller;

import com.clinica.aura.modules.school.dto.SchoolRequestDto;
import com.clinica.aura.modules.school.dto.SchoolRequestDtoUpdate;
import com.clinica.aura.modules.school.dto.SchoolResponseDto;
import com.clinica.aura.modules.school.service.SchoolService;
import com.clinica.aura.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
@Tag(name = "School", description = "Endpoints para manejar las escuelas")
@PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONAL') or hasRole('RECEPTIONIST')")
public class SchoolController {

    private final SchoolService schoolService;

    @PostMapping
    @Operation(summary = "Crear una nueva escuela", description = "Se crea una nueva escuela con todos sus datos de contacto, " +
            "acá no se agregan pacientes a la escuela. Cuando se crea el nuevo paciente ahí puede agregarse el id" +
            " de la escuela a la que el menor asiste. La escuela se crea primero y luego puede ser usado su " +
            "usado su id cuando se cree un nuevo paciente")
    public ResponseEntity<SchoolResponseDto> create(@Valid @RequestBody SchoolRequestDto request) {
        SchoolResponseDto response = schoolService.createSchool(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/schools")
    @Operation(summary = "Listar todas las escuelas", description = "Obtiene una lista paginada de todas las escuelas registradas")
    public PaginatedResponse<SchoolResponseDto> listSchools(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return schoolService.getAllSchools(page, size);
    }

    @PutMapping("/schools/{id}")
    @Operation(summary = "Editar una escuela", description = "Actualiza los datos de una escuela existente por su ID.")
    public ResponseEntity<SchoolResponseDto> updateSchool(@PathVariable Long id, @Valid @RequestBody SchoolRequestDtoUpdate request) {
        SchoolResponseDto updatedSchool = schoolService.updateSchool(id, request);
        return ResponseEntity.ok(updatedSchool);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una escuela por ID", description = "Elimina una escuela existente. Devuelve mensaje de confirmación.")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        schoolService.deleteSchoolById(id);
        return ResponseEntity.ok("Escuela borrada correctamente");
    }
    

}