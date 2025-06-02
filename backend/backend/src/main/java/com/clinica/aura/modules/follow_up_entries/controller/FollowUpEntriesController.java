package com.clinica.aura.modules.follow_up_entries.controller;

import com.clinica.aura.modules.follow_up_entries.dtoRequest.FollowUpEntriesDtoRequest;
import com.clinica.aura.modules.follow_up_entries.dtoRequest.FollowUpEntriesDtoRequestUpdate;
import com.clinica.aura.modules.follow_up_entries.dtoResponse.FollowUpEntriesDtoResponse;
import com.clinica.aura.modules.follow_up_entries.service.FollowUpEntriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/follow-up-entries")
@RequiredArgsConstructor
@Tag(name = "Follow Up Entries", description = "Entradas de seguimiento")
@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONAL')")
public class FollowUpEntriesController {

    public final FollowUpEntriesService followUpEntriesService;

    /**
     * Endpoint para crear un nuevo seguimiento médico.
     * <p>
     * Registra un seguimiento asociado a una historia clínica existente usando la información
     * proporcionada en el DTO de solicitud.
     * </p>
     *
     * @param dto el objeto {@link FollowUpEntriesDtoRequest} que contiene los datos para crear el seguimiento
     * @return una respuesta con código 200 (OK) y el DTO con los datos del seguimiento creado
     */
    @Operation(
            summary = "Crear un nuevo seguimiento",
            description = "Registra un nuevo seguimiento médico asociado a una historia clínica existente."
    )
    @PostMapping("/create")
    public ResponseEntity<FollowUpEntriesDtoResponse> create(@RequestBody FollowUpEntriesDtoRequest dto){
        FollowUpEntriesDtoResponse response = followUpEntriesService.create(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obtener todos los seguimientos paginados.
     * <p>
     * Devuelve una lista paginada de todos los registros de seguimiento existentes en el sistema.
     * </p>
     *
     * @param page el número de la página a obtener (por defecto 0)
     * @param size el número de elementos por página (por defecto 10)
     * @return una respuesta con código 200 (OK) y la lista paginada de seguimientos
     */

    @Operation(
            summary = "Obtener todos los seguimientos paginados",
            description = "Devuelve una lista paginada de todos los registros de seguimiento existentes en el sistema."
    )
    @GetMapping()
    public ResponseEntity<Page<FollowUpEntriesDtoResponse>> getFollowUpEntriesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(followUpEntriesService.getFollowUpEntriesPage(page, size));
    }

    /**
     * Endpoint para obtener todos los seguimientos de una historia clínica.
     * <p>
     * Devuelve una lista de todos los registros de seguimiento asociados a una historia clínica existente.
     * </p>
     *
     * @param id el ID de la historia clínica
     * @return una respuesta con código 200 (OK) y la lista de seguimientos asociados a la historia clínica
     */
    @Operation(
            summary = "Buscar seguimiento por ID",
            description = "Obtiene la información de un seguimiento específico a partir de su ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id ){
        return ResponseEntity.ok(followUpEntriesService.findById(id));
    }

    /**
     * Endpoint para actualizar un seguimiento existente.
     * <p>
     * Modifica los datos de un seguimiento existente identificado por su ID.
     * </p>
     *
     * @param id el ID del seguimiento a actualizar
     * @param dto el objeto {@link FollowUpEntriesDtoRequestUpdate} que contiene los nuevos datos para el seguimiento
     * @return una respuesta con código 200 (OK) y el DTO con los datos del seguimiento actualizado
     */

    @Operation(
            summary = "Actualizar un seguimiento existente",
            description = "Modifica los datos de un seguimiento existente identificado por su ID."
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FollowUpEntriesDtoRequestUpdate dto){
        return ResponseEntity.ok(followUpEntriesService.update(id, dto));
    }


}
