package com.clinica.aura.entities.medical_records.service;

import com.clinica.aura.entities.medical_records.dtoRequest.MedicalRecordsRequestDto;
import com.clinica.aura.entities.medical_records.model.MedicalRecordsModel;
import com.clinica.aura.entities.medical_records.repository.MedicalRecordsRepository;
import com.clinica.aura.entities.patient.model.PatientModel;
import com.clinica.aura.entities.patient.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordsService {

    private final MedicalRecordsRepository medicalRecordsRepository;
    private final PatientRepository patientRepository;

    public MedicalRecordsModel create(MedicalRecordsRequestDto dto) {
        PatientModel patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        MedicalRecordsModel record = MedicalRecordsModel.builder()
                .patients(patient)
                .notes(dto.getNotes())
                .allergies(dto.getAllergies())
                .previousConditions(dto.getPreviousConditions())
                .build();

        return medicalRecordsRepository.save(record);
    }

    public List<MedicalRecordsModel> findAll() {
        return medicalRecordsRepository.findAll();
    }

    public MedicalRecordsModel findById(Long id) {
        return medicalRecordsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historial no encontrado"));
    }

    @Transactional
    public MedicalRecordsModel update(Long id, MedicalRecordsRequestDto dto) {
        MedicalRecordsModel existing = medicalRecordsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historial no encontrado"));

        existing.setNotes(dto.getNotes());
        existing.setAllergies(dto.getAllergies());
        existing.setPreviousConditions(dto.getPreviousConditions());

        return existing;
    }

    public void delete(Long id) {
        if (!medicalRecordsRepository.existsById(id)) {
            throw new EntityNotFoundException("Historial no encontrado");
        }
        medicalRecordsRepository.deleteById(id);
    }
}
