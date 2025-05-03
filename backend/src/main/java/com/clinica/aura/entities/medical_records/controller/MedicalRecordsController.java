package com.clinica.aura.entities.medical_records.controller;

import com.clinica.aura.entities.medical_records.dtoRequest.MedicalRecordsResponseDto;
import com.clinica.aura.entities.medical_records.dtoRequest.MedicalRecordsRequestDto;
import com.clinica.aura.entities.medical_records.dtoRequest.MedicalRecordsRequestUpdateDto;
import com.clinica.aura.entities.medical_records.service.MedicalRecordsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Records", description = "Historial clínico")
public class MedicalRecordsController {

    public final MedicalRecordsService service;

    @PostMapping("/create")
    public ResponseEntity<MedicalRecordsResponseDto> create(@RequestBody MedicalRecordsRequestDto dto) {
        MedicalRecordsResponseDto response = service.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /*
    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(service.getAllMedicalRecords());
    }*/

    //URL de prueba
    /*http://localhost:8080/medical-records/historiaPage?page=0&size=10*/
    @GetMapping("/historiaPage")
    public ResponseEntity<Page<MedicalRecordsResponseDto>> getMedicalRecordsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(service.getMedicalRecordsPage(page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Historial con el ID "+ id +" eliminado exitosamente.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MedicalRecordsRequestUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }


}
