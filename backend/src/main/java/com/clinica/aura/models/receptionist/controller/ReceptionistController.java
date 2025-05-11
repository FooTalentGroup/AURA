package com.clinica.aura.models.receptionist.controller;

import com.clinica.aura.models.receptionist.dtoRequest.ReceptionistRequestUpdateDto;
import com.clinica.aura.models.receptionist.dtoResponse.ReceptionistResponseDto;
import com.clinica.aura.models.receptionist.service.ReceptionistService;
import com.clinica.aura.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receptionist")
@RequiredArgsConstructor
@Tag(name = "Receptionist", description = "Controlador de recepcionistas")
@PreAuthorize("hasRole('ADMIN') or hasRole('RECEPTIONIST')")
public class ReceptionistController {

    private final ReceptionistService receptionistService;


    @Operation(summary = "Actualiza los datos de un recepcionista", description = "Actualiza los datos de un recepcionista específico.")
    @PatchMapping("/{id}")
    public ResponseEntity<ReceptionistResponseDto> updateReceptionist(@PathVariable  long id, @RequestBody @Valid ReceptionistRequestUpdateDto receptionistRequestUpdateDto) {
        return ResponseEntity.ok(receptionistService.updateReceptionist(id, receptionistRequestUpdateDto));
    }

    @Operation(summary = "Obtener todos los recepcionistas", description = "Obtiene todos los recepcionistas.")
    @GetMapping
    public ResponseEntity<PaginatedResponse<ReceptionistResponseDto>> getAllReceptionists(@RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(receptionistService.getAllReceptionists(page, size));
    }

    @Operation(summary = "Obtener un recepcionista por ID", description = "Obtiene un recepcionista por su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ReceptionistResponseDto> getReceptionistById(@PathVariable long id) {
        return ResponseEntity.ok(receptionistService.getReceptionistById(id));
    }

    @Operation(summary = "Eliminar un recepcionista", description = "Elimina un recepcionista del sistema.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReceptionist(@PathVariable long id) {
        receptionistService.deleteReceptionist(id);
        return ResponseEntity.ok("Recepcionista eliminado exitosamente.");
    }
}
