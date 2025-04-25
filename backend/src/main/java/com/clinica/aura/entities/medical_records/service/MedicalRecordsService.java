package com.clinica.aura.entities.medical_records.service;

import com.clinica.aura.entities.medical_records.dtoRequest.MedicalRecordsResponseDto;
import com.clinica.aura.entities.medical_records.dtoRequest.MedicalRecordsRequestDto;
import com.clinica.aura.entities.medical_records.dtoRequest.MedicalRecordsRequestUpdateDto;
import com.clinica.aura.entities.medical_records.model.MedicalRecordsModel;
import com.clinica.aura.entities.medical_records.repository.MedicalRecordsRepository;
import com.clinica.aura.entities.patient.model.PatientModel;
import com.clinica.aura.entities.patient.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordsService {

    private final MedicalRecordsRepository medicalRecordsRepository;
    private final PatientRepository patientRepository;

    public MedicalRecordsResponseDto create(MedicalRecordsRequestDto dto) {
        PatientModel patient = patientRepository.findById(dto.getPatientId()).orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        MedicalRecordsModel record = new MedicalRecordsModel();
        record.setPatients(patient);
        record.setNotes(dto.getNotes());
        record.setAllergies(dto.getAllergies());
        record.setPreviousConditions(dto.getPreviousConditions());
        medicalRecordsRepository.save(record);

        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setNotes(record.getNotes());
        response.setAllergies(record.getAllergies());
        response.setPreviousConditions(record.getPreviousConditions());
        response.setPatientId(record.getPatients().getId());

        return response;
    }

    public MedicalRecordsResponseDto findById(Long id) {
        MedicalRecordsModel record = medicalRecordsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));
        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setNotes(record.getNotes());
        response.setAllergies(record.getAllergies());
        response.setPreviousConditions(record.getPreviousConditions());
        response.setPatientId(record.getPatients().getId());
        return response;
    }

    public List<MedicalRecordsResponseDto> getAllMedicalRecords() {
        List<MedicalRecordsModel> records = medicalRecordsRepository.findAll();
        List<MedicalRecordsResponseDto> response = new ArrayList<>();
        for (MedicalRecordsModel record : records) {
            MedicalRecordsResponseDto dto = new MedicalRecordsResponseDto();
            dto.setId(record.getId());
            dto.setNotes(record.getNotes());
            dto.setAllergies(record.getAllergies());
            dto.setPreviousConditions(record.getPreviousConditions());
            dto.setPatientId(record.getPatients().getId());
            response.add(dto);
        }
        return response;
    }

    public MedicalRecordsResponseDto update(Long id, MedicalRecordsRequestUpdateDto dto) {
        MedicalRecordsModel record = medicalRecordsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));

        record.setNotes(dto.getNotes());
        record.setAllergies(dto.getAllergies());
        record.setPreviousConditions(dto.getPreviousConditions());
        medicalRecordsRepository.save(record);


        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setNotes(record.getNotes());
        response.setAllergies(record.getAllergies());
        response.setPreviousConditions(record.getPreviousConditions());
        response.setPatientId(record.getPatients().getId());
        return response;
    }

    public void delete(Long id) {
        MedicalRecordsModel record = medicalRecordsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));
        medicalRecordsRepository.delete(record);
    }

    //paginacion medical records
    public Page<MedicalRecordsResponseDto> getMedicalRecordsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return medicalRecordsRepository.findAll(pageable).map(this::mapToDto);
    }

    private MedicalRecordsResponseDto mapToDto(MedicalRecordsModel medicalRecordsModel) {
        return new MedicalRecordsResponseDto(
                medicalRecordsModel.getId(),
                medicalRecordsModel.getNotes(),
                medicalRecordsModel.getAllergies(),
                medicalRecordsModel.getPreviousConditions(),
                medicalRecordsModel.getPatients().getId()
        );
    }
}
