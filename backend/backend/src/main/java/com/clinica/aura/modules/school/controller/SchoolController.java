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

    /**
     * Crea una nueva escuela.
     * Este endpoint recibe los datos de una escuela y la registra en el sistema.
     * Importante: aquí no se asocian pacientes a la escuela. La relación se establece luego,
     * al crear un nuevo paciente y asociarlo al ID de una escuela previamente registrada.
     * @param request Objeto {@link SchoolRequestDto} con los datos necesarios para crear la escuela.
     * @return Una respuesta con código 201 (CREATED) y un {@link SchoolResponseDto} con los datos de la escuela creada.
     */
    @PostMapping
    @Operation(summary = "Crear una nueva escuela", description = "Crea una nueva escuela con todos sus datos de contacto." +
            " En este paso no se agregan pacientes a la escuela. Cuando se crea un nuevo paciente, puede asociarse el ID" +
            " de la escuela a la que asiste. La escuela debe crearse primero, y luego su ID puede utilizarse al registrar" +
            " un nuevo paciente")
    public ResponseEntity<SchoolResponseDto> create(@Valid @RequestBody SchoolRequestDto request) {
        SchoolResponseDto response = schoolService.createSchool(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Lista todas las escuelas registradas de forma paginada.
     * Este endpoint permite obtener una lista de escuelas, paginada según los parámetros especificados.
     * @param page Número de página a consultar (por defecto 0).
     * @param size Cantidad de resultados por página (por defecto 10).
     * @return Un {@link PaginatedResponse} con la lista de escuelas y los datos de paginación.
     */
    @GetMapping("/schools")
    @Operation(summary = "Listar todas las escuelas", description = "Obtiene una lista paginada de todas las escuelas registradas")
    public PaginatedResponse<SchoolResponseDto> listSchools(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return schoolService.getAllSchools(page, size);
    }


    /**
     * Actualiza los datos de una escuela existente.
     * Busca una escuela por su ID y actualiza sus datos con los valores proporcionados.
     * @param id ID de la escuela a actualizar.
     * @param request Objeto {@link SchoolRequestDtoUpdate} con los nuevos datos de la escuela.
     * @return Una respuesta con código 200 (OK) y un {@link SchoolResponseDto} con los datos actualizados.
     */
    @PutMapping("/schools/{id}")
    @Operation(summary = "Editar una escuela", description = "Actualiza los datos de una escuela existente utilizando su ID.")
    public ResponseEntity<SchoolResponseDto> updateSchool(@PathVariable Long id, @Valid @RequestBody SchoolRequestDtoUpdate request) {
        SchoolResponseDto updatedSchool = schoolService.updateSchool(id, request);
        return ResponseEntity.ok(updatedSchool);
    }

    /**
     * Actualiza los datos de una escuela existente.
     * Busca una escuela por su ID y actualiza sus datos con los valores proporcionados.
     * @param id ID de la escuela a actualizar.
     * @return Una respuesta con código 200 (OK) y un {@link SchoolResponseDto} con los datos actualizados.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una escuela por ID", description = "Elimina una escuela existente. Devuelve mensaje de confirmación.")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        schoolService.deleteSchoolById(id);
        return ResponseEntity.ok("Escuela borrada correctamente");
    }
}