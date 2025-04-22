package com.clinica.aura.entities.medical_records.controller;

import com.clinica.aura.entities.medical_records.dtoRequest.MedicalRecordsRequestDto;
import com.clinica.aura.entities.medical_records.model.MedicalRecordsModel;
import com.clinica.aura.entities.medical_records.service.MedicalRecordsService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordsController {
    private final MedicalRecordsService medicalRecordsService;

//    @PostMapping
//    public ResponseEntity<MedicalRecordsModel> create(@Valid @RequestBody MedicalRecordsRequestDto dto) {
//        return ResponseEntity.ok(medicalRecordsService.create(dto));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<MedicalRecordsModel>> getAll() {
//        return ResponseEntity.ok(medicalRecordsService.findAll());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<MedicalRecordsModel> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(medicalRecordsService.findById(id));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<MedicalRecordsModel> update(@PathVariable Long id, @Valid @RequestBody MedicalRecordsRequestDto dto) {
//        return ResponseEntity.ok(medicalRecordsService.update(id, dto));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        medicalRecordsService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
}
