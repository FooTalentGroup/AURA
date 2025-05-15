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

    @Operation(summary = "Crear un nuevo historial",
            description = "Crea un nuevo historial clinico. Tener en cuenta que un profesional debe haber iniciado sesión para poder crear un historial.")

    @PostMapping("/create")
    public ResponseEntity<MedicalRecordsResponseDto> create(@RequestBody MedicalRecordsRequestDto dto) {
        MedicalRecordsResponseDto response = service.create(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener un historial",
            description = "Obtiene un historial clinico por su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Obtener seguimientos por ID de paciente",
            description = "Devuelve una lista de registros de seguimiento vinculados al paciente con el ID especificado. "
                    + "Este endpoint permite consultar todos los seguimientos registrados para un paciente en particular."
    )
    @GetMapping("/patient/{idPatient}")
    public ResponseEntity<?> findByIPatients(@PathVariable Long idPatient){
        return ResponseEntity.ok(service.findByPatientsId(idPatient));
    }

    /*
    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(service.getAllMedicalRecords());
    }*/

    //URL de prueba
    /*http://localhost:8080/medical-records/historiaPage?page=0&size=10*/
    @Operation(summary = "Obtener historiales con paginación",
            description = "Usá los parámetros 'page' y 'size' en la URL para controlar la paginación. Ejemplo: /medical-records/historiaPage?page=0&size=5")
    @GetMapping()
    public ResponseEntity<Page<MedicalRecordsResponseDto>> getMedicalRecordsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(service.getMedicalRecordsPage(page, size));
    }

    @Operation(summary = "Eliminar un historial",
            description = "Elimina un historial clinico del sistema junto con su usuario, roles y datos personales.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Historial con el ID "+ id +" eliminado exitosamente.");
    }

//    @Operation(summary = "Actualizar un historial",
//            description = "Actualiza los datos de un historial específico. Tener en cuenta que un profesional debe haber iniciado sesión para poder actualizar un historial.")
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MedicalRecordsRequestUpdateDto dto) {
//        return ResponseEntity.ok(service.update(id, dto));
//    }

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
