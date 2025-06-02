package com.clinica.aura.modules.receptionist.controller;

import com.clinica.aura.modules.receptionist.dtoRequest.ReceptionistRequestUpdateDto;
import com.clinica.aura.modules.receptionist.dtoResponse.ReceptionistResponseDto;
import com.clinica.aura.modules.receptionist.service.ReceptionistService;
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
@Tag(name = "Receptionist", description = "Controlador de recepcionistas - Versión futura esta por fuera del MVP")
@PreAuthorize("hasRole('ADMIN') or hasRole('RECEPTIONIST')")
public class ReceptionistController {

    private final ReceptionistService receptionistService;


    /**
     * Endpoint para actualizar los datos de un recepcionista específico.
     * <p>
     * Actualiza la información del recepcionista identificado por el ID proporcionado,
     * utilizando los datos validados del DTO de actualización.
     * </p>
     *
     * @param id                            el identificador único del recepcionista a actualizar
     * @param receptionistRequestUpdateDto  el objeto {@link ReceptionistRequestUpdateDto} con los datos para la actualización
     * @return una respuesta con código 200 (OK) y el DTO con los datos actualizados del recepcionista
     */

    @Operation(summary = "Actualiza los datos de un recepcionista", description = "Actualiza los datos de un recepcionista específico.")
    @PatchMapping("/{id}")
    public ResponseEntity<ReceptionistResponseDto> updateReceptionist(@PathVariable  long id, @RequestBody @Valid ReceptionistRequestUpdateDto receptionistRequestUpdateDto) {
        return ResponseEntity.ok(receptionistService.updateReceptionist(id, receptionistRequestUpdateDto));
    }

    /**
     * Endpoint para obtener todos los recepcionistas con paginación.
     * <p>
     * Permite obtener una lista paginada de recepcionistas.
     * Los parámetros 'page' y 'size' controlan la paginación de los resultados.
     * </p>
     *
     * @param page número de página a obtener (por defecto 0)
     * @param size cantidad de recepcionistas por página (por defecto 10)
     * @return una respuesta con código 200 (OK) y una página de {@link ReceptionistResponseDto}
     */

    @Operation(summary = "Obtener todos los recepcionistas", description = "Obtiene todos los recepcionistas.")
    @GetMapping
    public ResponseEntity<PaginatedResponse<ReceptionistResponseDto>> getAllReceptionists(@RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(receptionistService.getAllReceptionists(page, size));
    }

    /**
     * Endpoint para obtener un recepcionista por ID.
     * <p>
     * Permite obtener un recepcionista identificado por su ID.
     * </p>
     *
     * @param id el identificador único del recepcionista a obtener
     * @return una respuesta con código 200 (OK) y el DTO con los datos del recepcionista
     */

    @Operation(summary = "Obtener un recepcionista por ID", description = "Obtiene un recepcionista por su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ReceptionistResponseDto> getReceptionistById(@PathVariable long id) {
        return ResponseEntity.ok(receptionistService.getReceptionistById(id));
    }

    /**
     * Endpoint para eliminar un recepcionista.
     * <p>
     * Elimina un recepcionista identificado por su ID.
     * </p>
     *
     * @param id el identificador único del recepcionista a eliminar
     * @return una respuesta con código 200 (OK) y un mensaje de confirmación
     */

    @Operation(summary = "Eliminar un recepcionista", description = "Elimina un recepcionista del sistema.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReceptionist(@PathVariable long id) {
        receptionistService.deleteReceptionist(id);
        return ResponseEntity.ok("Recepcionista eliminado exitosamente.");
    }
}
