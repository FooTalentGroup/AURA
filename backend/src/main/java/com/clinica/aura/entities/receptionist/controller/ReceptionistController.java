package com.clinica.aura.entities.receptionist.controller;

import com.clinica.aura.entities.receptionist.dtoRequest.ReceptionistRequestUpdateDto;
import com.clinica.aura.entities.receptionist.dtoResponse.ReceptionistResponseDto;
import com.clinica.aura.entities.receptionist.service.ReceptionistService;
import com.clinica.aura.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receptionist")
@RequiredArgsConstructor
@Tag(name = "Receptionist", description = "Controlador de recepcionistas")
public class ReceptionistController {

    private final ReceptionistService receptionistService;


    @PatchMapping("/{id}")
    public ResponseEntity<ReceptionistResponseDto> updateReceptionist(@PathVariable  long id, @RequestBody @Valid ReceptionistRequestUpdateDto receptionistRequestUpdateDto) {
        return ResponseEntity.ok(receptionistService.updateReceptionist(id, receptionistRequestUpdateDto));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<ReceptionistResponseDto>> getAllReceptionists(@RequestParam(defaultValue = "0") int page,
                                                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(receptionistService.getAllReceptionists(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceptionistResponseDto> getReceptionistById(@PathVariable long id) {
        return ResponseEntity.ok(receptionistService.getReceptionistById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReceptionist(@PathVariable long id) {
        receptionistService.deleteReceptionist(id);
        return ResponseEntity.ok("Recepcionista eliminado exitosamente.");
    }
}
