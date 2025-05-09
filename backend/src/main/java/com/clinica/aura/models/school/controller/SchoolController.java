package com.clinica.aura.models.school.controller;

import com.clinica.aura.models.school.dto.SchoolRequestDto;
import com.clinica.aura.models.school.dto.SchoolRequestDtoUpdate;
import com.clinica.aura.models.school.dto.SchoolResponseDto;
import com.clinica.aura.models.school.model.SchoolModel;
import com.clinica.aura.models.school.service.SchoolService;
import com.clinica.aura.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
@Tag(name = "School", description = "Endpoints para manejar las escuelas")
public class SchoolController {

    private final SchoolService schoolService;

    //crear escuela
    @PostMapping
    @Operation(summary = "Crear una nueva escuela", description = "Se crea una nueva escuela con todos sus datos de contacto, " +
            "acá no se agregan pacientes a la escuela. Cuando se crea el nuevo paciente ahí puede agregarse el id" +
            " de la escuela a la que el menor asiste. La escuela se crea primero y luego puede ser usado su " +
            "usado su id cuando se cree un nuevo paciente")
    public ResponseEntity<SchoolResponseDto> create(@Valid @RequestBody SchoolRequestDto request) {
        SchoolResponseDto response = schoolService.createSchool(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //listar escuelas
    @GetMapping("/schools")
    @Operation(summary = "Listar todas las escuelas", description = "Obtiene una lista paginada de todas las escuelas registradas")
    public PaginatedResponse<SchoolResponseDto> listSchools(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return schoolService.getAllSchools(page, size);
    }

    //actualizar escuela
    @PutMapping("/schools/{id}")
    @Operation(summary = "Editar una escuela", description = "Actualiza los datos de una escuela existente por su ID.")
    public ResponseEntity<SchoolResponseDto> updateSchool(@PathVariable Long id, @Valid @RequestBody SchoolRequestDtoUpdate request) {
        SchoolResponseDto updatedSchool = schoolService.updateSchool(id, request);
        return ResponseEntity.ok(updatedSchool);
    }

    //borrar escuela por id
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una escuela por ID", description = "Elimina una escuela existente. Devuelve mensaje de confirmación.")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        schoolService.deleteSchoolById(id);
        return ResponseEntity.ok("Escuela borrada correctamente");
    }




}