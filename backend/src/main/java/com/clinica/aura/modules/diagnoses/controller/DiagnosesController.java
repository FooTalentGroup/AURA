package com.clinica.aura.modules.diagnoses.controller;

import com.clinica.aura.modules.diagnoses.dtoRequest.DiagnosesRequestDto;
import com.clinica.aura.modules.diagnoses.dtoRequest.DiagnosesRequestUpdateDto;
import com.clinica.aura.modules.diagnoses.dtoResponse.DiagnosesResponseDto;
import com.clinica.aura.modules.diagnoses.service.DiagnosesService;
import com.clinica.aura.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diagnoses")
@Tag(name = "Diagnoses", description = "Diagnoses")
@PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONAL')")
public class DiagnosesController {
    private final DiagnosesService diagnosesService;

    /**
     * Endpoint para crear un diagnóstico a partir de una historia clínica.
     * <p>
     * Este método recibe un objeto {@link DiagnosesRequestDto} en el cuerpo de la solicitud
     * y retorna un {@link DiagnosesResponseDto} con la información del diagnóstico creado.
     * </p>
     *
     * @param diagnosesRequestDto el DTO que contiene los datos necesarios para crear un diagnóstico
     * @return una respuesta con código 201 (Created) y el DTO del diagnóstico creado en el cuerpo
     */
    @Operation(summary = "Crear diagnostico", description = "Crea un diagnostico a partir de una historia clinica")
    @PostMapping("/create")
    public ResponseEntity<DiagnosesResponseDto> createDiagnoses(@RequestBody DiagnosesRequestDto diagnosesRequestDto) {
        DiagnosesResponseDto diagnosesResponseDto = diagnosesService.createDiagnoses(diagnosesRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(diagnosesResponseDto);
    }



    /**
     * Endpoint para actualizar un diagnóstico existente.
     * <p>
     * Verifica que el diagnóstico con el ID proporcionado exista y luego actualiza sus datos
     * con la información recibida en el cuerpo de la solicitud.
     * </p>
     *
     * @param id                  el identificador único del diagnóstico a actualizar
     * @param diagnosesRequestDto el DTO que contiene los datos para actualizar el diagnóstico
     * @return una respuesta con código 200 (OK) y el DTO con la información actualizada del diagnóstico
     */
    @Operation(summary = "Actualizar diagnostico", description = "Actualiza un diagnostico, verifica que el id del diagnostico exista")
    @PatchMapping("/update/{id}")
    public ResponseEntity<DiagnosesResponseDto> updateDiagnoses(@PathVariable Long id, @RequestBody DiagnosesRequestUpdateDto diagnosesRequestDto) {
        DiagnosesResponseDto diagnosesResponseDto = diagnosesService.updateDiagnoses(id, diagnosesRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(diagnosesResponseDto);
    }

    /**
     * Endpoint para obtener una lista paginada de todos los diagnósticos.
     * <p>
     * Permite especificar la página y el tamaño de página para la paginación de resultados.
     * </p>
     *
     * @param page número de página a obtener (por defecto 0)
     * @param size tamaño de la página, cantidad de diagnósticos por página (por defecto 10)
     * @return una respuesta con código 200 (OK) que contiene una paginación de {@link DiagnosesResponseDto}
     */
    @Operation(summary = "Obtener todos los diagnosticos", description = "Obtiene todos los diagnosticos")
    @GetMapping
    public ResponseEntity<PaginatedResponse<DiagnosesResponseDto>> getAllDiagnoses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(diagnosesService.getAllDiagnoses(page, size));
    }

    /**
     * Endpoint para obtener un diagnóstico específico por su ID.
     * <p>
     * Busca y devuelve la información del diagnóstico correspondiente al identificador proporcionado.
     * </p>
     *
     * @param id el identificador único del diagnóstico que se desea obtener
     * @return una respuesta con código 200 (OK) y el DTO con la información del diagnóstico solicitado
     */
    @Operation(summary = "Obtener diagnostico por id", description = "Obtiene un diagnostico por id")
    @GetMapping("/{id}")
    public ResponseEntity<DiagnosesResponseDto> getDiagnosesById(@PathVariable Long id) {
        DiagnosesResponseDto diagnosesResponseDto = diagnosesService.getDiagnosesById(id);
        return ResponseEntity.status(HttpStatus.OK).body(diagnosesResponseDto);
    }

    /**
     * Endpoint para eliminar un diagnóstico específico por su ID.
     * <p>
     * Elimina el diagnóstico correspondiente al identificador proporcionado.
     * </p>
     *
     * @param id el identificador único del diagnóstico que se desea eliminar
     * @return una respuesta con código 200 (OK) y un mensaje confirmando la eliminación
     */
    @Operation(summary = "Eliminar diagnostico por id", description = "Elimina un diagnostico por id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiagnosesById(@PathVariable Long id) {
        diagnosesService.deleteDiagnoses(id);
        return ResponseEntity.status(HttpStatus.OK).body("Diagnostico eliminado correctamente");
    }


}
