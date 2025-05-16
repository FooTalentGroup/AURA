package com.clinica.aura.modules.medical_records.controller;

import com.clinica.aura.modules.medical_records.dtoResponse.MedicalRecordsResponseDto;
import com.clinica.aura.modules.medical_records.dtoRequest.MedicalRecordsRequestDto;
import com.clinica.aura.modules.medical_records.dtoResponse.MedicalRecordsSummaryDto;
import com.clinica.aura.modules.medical_records.service.MedicalRecordsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Records", description = "Historial clínico")
public class MedicalRecordsController {

    public final MedicalRecordsService service;

    /**
     * Endpoint para crear un nuevo historial clínico.
     * <p>
     * Crea un historial clínico nuevo. Se debe tener en cuenta que
     * un profesional debe haber iniciado sesión para poder realizar esta acción.
     * </p>
     *
     * @param dto el objeto {@link MedicalRecordsRequestDto} que contiene los datos para crear el historial clínico
     * @return una respuesta con código 200 (OK) y el DTO con la información del historial clínico creado
     */

    @Operation(summary = "Crear un nuevo historial",
            description = "Crea un nuevo historial clinico. Tener en cuenta que un profesional debe haber iniciado sesión para poder crear un historial.")

    @PostMapping("/create")
    public ResponseEntity<MedicalRecordsResponseDto> create(@RequestBody MedicalRecordsRequestDto dto) {
        MedicalRecordsResponseDto response = service.create(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obtener un historial clínico por su ID.
     * <p>
     * Obtiene un historial clínico por su ID. Se debe tener en cuenta que
     * un profesional debe haber iniciado sesión para poder realizar estaacción.
     * </p>
     *
     * @param id el ID del historial clínico que se desea obtener
     * @return una respuesta con código 200 (OK) y el DTO con la información del historial clínico obtenido
     */

    @Operation(summary = "Obtener un historial",
            description = "Obtiene un historial clinico por su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Endpoint para obtener los registros de seguimiento asociados a un paciente específico.
     * <p>
     * Devuelve una lista con todos los seguimientos médicos registrados para el paciente
     * identificado por el ID proporcionado.
     * </p>
     *
     * @param idPatient el identificador único del paciente cuyos seguimientos se desean consultar
     * @return una respuesta con código 200 (OK) y la lista de seguimientos del paciente
     */

    @Operation(
            summary = "Obtener seguimientos por ID de paciente",
            description = "Devuelve una lista de registros de seguimiento vinculados al paciente con el ID especificado. "
                    + "Este endpoint permite consultar todos los seguimientos registrados para un paciente en particular."
    )
    @GetMapping("/patient/{idPatient}")
    public ResponseEntity<?> findByIPatients(@PathVariable Long idPatient) {
        return ResponseEntity.ok(service.findByPatientsId(idPatient));
    }

    /**
     * Endpoint para obtener historiales clínicos con paginación.
     * <p>
     * Permite controlar la paginación usando los parámetros 'page' y 'size' en la URL.
     * Ejemplo de uso: /medical-records/historiaPage?page=0&size=5
     * </p>
     *
     * @param page número de página a obtener (por defecto 0)
     * @param size tamaño de la página, cantidad de historiales por página (por defecto 10)
     * @return una respuesta con código 200 (OK) que contiene una página de {@link MedicalRecordsResponseDto}
     */

    @Operation(summary = "Obtener historiales con paginación",
            description = "Usá los parámetros 'page' y 'size' en la URL para controlar la paginación. Ejemplo: /medical-records/historiaPage?page=0&size=5")
    @GetMapping()
    public ResponseEntity<Page<MedicalRecordsResponseDto>> getMedicalRecordsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(service.getMedicalRecordsPage(page, size));
    }

    /**
     * Endpoint para eliminar un historial clínico.
     * <p>
     * Elimina un historial clínico del sistema junto con su usuario, roles y datos personales.
     * </p>
     *
     * @param id el ID del historial clínico que se desea eliminar
     * @return una respuesta con código 200 (OK) y un mensaje indicando que el historial clínico fue eliminado exitosamente
     */

    @Operation(summary = "Eliminar un historial",
            description = "Elimina un historial clinico del sistema junto con su usuario, roles y datos personales.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Historial con el ID "+ id +" eliminado exitosamente.");
    }


    /**
     * Endpoint para obtener un listado de historiales clínicos filtrados.
     * <p>
     * Permite filtrar los historiales por especialidad, nombre del profesional y/o fecha.
     * Todos los parámetros son opcionales, si no se especifican se devuelven todos los historiales.
     * </p>
     *
     * @param specialty        especialidad médica para filtrar (opcional)
     * @param professionalName nombre del profesional para filtrar (opcional)
     * @param date             fecha del historial para filtrar (opcional), formato esperado: yyyy-MM-dd
     * @return una respuesta con código 200 (OK) y una lista de {@link MedicalRecordsSummaryDto} que cumplen con los filtros
     */

    @GetMapping("/filter")
    public ResponseEntity<List<MedicalRecordsSummaryDto>> getHistory(
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String professionalName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return ResponseEntity.ok(
                service.getFilteredHistory(specialty, professionalName, date)
        );
    }



}
