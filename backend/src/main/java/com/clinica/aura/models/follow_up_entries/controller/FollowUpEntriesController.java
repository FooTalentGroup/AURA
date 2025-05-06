package com.clinica.aura.models.follow_up_entries.controller;

import com.clinica.aura.models.follow_up_entries.dtoRequest.FollowUpEntriesDtoRequest;
import com.clinica.aura.models.follow_up_entries.dtoRequest.FollowUpEntriesDtoRequestUpdate;
import com.clinica.aura.models.follow_up_entries.dtoResponse.FollowUpEntriesDtoResponse;
import com.clinica.aura.models.follow_up_entries.service.FollowUpEntriesService;
import com.clinica.aura.models.medical_records.dtoResponse.MedicalRecordsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/follow-up-entries")
@RequiredArgsConstructor
@Tag(name = "Follow Up Entries", description = "Entradas de seguimiento")
@RestController
public class FollowUpEntriesController {

    public final FollowUpEntriesService followUpEntriesService;

    @Operation(
            summary = "Crear un nuevo seguimiento",
            description = "Registra un nuevo seguimiento médico asociado a una historia clínica existente."
    )
    @PostMapping("/create")
    public ResponseEntity<FollowUpEntriesDtoResponse> create(@RequestBody FollowUpEntriesDtoRequest dto){
        FollowUpEntriesDtoResponse response = followUpEntriesService.create(dto);
        return ResponseEntity.ok(response);
    }

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

    @Operation(
            summary = "Buscar seguimiento por ID",
            description = "Obtiene la información de un seguimiento específico a partir de su ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id ){
        return ResponseEntity.ok(followUpEntriesService.findById(id));
    }

    @Operation(
            summary = "Actualizar un seguimiento existente",
            description = "Modifica los datos de un seguimiento existente identificado por su ID."
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FollowUpEntriesDtoRequestUpdate dto){
        return ResponseEntity.ok(followUpEntriesService.update(id, dto));
    }

    @Operation(
            summary = "Eliminar un seguimiento",
            description = "Elimina un seguimiento médico del sistema a partir de su ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        followUpEntriesService.delete(id);
        return ResponseEntity.ok( "Ha eliminado exitosamente seguimiento con id: "+id);
    }

}
