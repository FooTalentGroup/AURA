package com.clinica.aura.modules.medical_records.service;

import com.clinica.aura.modules.diagnoses.model.DiagnosesModel;
import com.clinica.aura.modules.follow_up_entries.model.FollowUpEntriesModel;
import com.clinica.aura.modules.medical_records.dtoRequest.*;
import com.clinica.aura.modules.medical_records.dtoResponse.MedicalRecordsResponseDto;

import com.clinica.aura.modules.medical_records.dtoResponse.MedicalRecordsSummaryDto;
import com.clinica.aura.modules.medical_records.model.MedicalRecordsModel;
import com.clinica.aura.modules.medical_records.repository.MedicalRecordsRepository;
import com.clinica.aura.modules.patient.model.PatientModel;
import com.clinica.aura.modules.patient.repository.PatientRepository;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import com.clinica.aura.exceptions.ConflictWithExistingRecord;
import com.clinica.aura.exceptions.PatientNotFoundException;
import com.clinica.aura.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalRecordsService {

    private final MedicalRecordsRepository medicalRecordsRepository;
    private final PatientRepository patientRepository;
    private final SecurityUtil securityUtil;
    private final ModelMapper mapper;

    @Transactional
    public MedicalRecordsResponseDto create(MedicalRecordsRequestDto dto) {
        PatientModel patient = patientRepository.findById(dto.getPatientId()).orElseThrow(() -> new PatientNotFoundException("Paciente con id " + dto.getPatientId() + " no encontrado"));

        if(medicalRecordsRepository.existsByPatientId(dto.getPatientId())) {
            throw new ConflictWithExistingRecord("El paciente con id " + dto.getPatientId() + " ya tiene una historia clínica registrada");
        }

        ProfessionalModel professionalModel = securityUtil.getAuthenticatedProfessional();
        MedicalRecordsModel record = new MedicalRecordsModel();
        record.setCreatedBy(professionalModel);
        record.setUpdatedBy(professionalModel);
        record.setPatients(patient);
        medicalRecordsRepository.save(record);

        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        response.setPatientId(record.getPatients().getId());
        response.setProfessionalId(record.getCreatedBy().getId());

        // Maneja el caso donde getDiagnoses() puede ser null
        List<Long> diagnosisIds = Optional.ofNullable(record.getDiagnoses())
                .orElse(Collections.emptyList())
                .stream()
                .map(DiagnosesModel::getId)
                .toList();
        response.setDiagnosisIds(diagnosisIds);

        // Maneja el caso donde getFollowUps() puede ser null
        List<Long> followUpIds = Optional.ofNullable(record.getFollowUps())
                .orElse(Collections.emptyList())
                .stream()
                .map(FollowUpEntriesModel::getId)
                .toList();
        response.setFollowUpIds(followUpIds);

        return response;
    }

    public MedicalRecordsResponseDto findById(Long id) {
        MedicalRecordsModel record = medicalRecordsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));
        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        response.setPatientId(record.getPatients().getId());
        response.setProfessionalId(record.getCreatedBy().getId());
        response.setDiagnosisIds(record.getDiagnoses().stream().map(DiagnosesModel::getId).toList());
        response.setFollowUpIds(record.getFollowUps().stream().map(FollowUpEntriesModel::getId).toList());

        return response;
    }
    public MedicalRecordsResponseDto findByPatientsId(Long patientId){
        MedicalRecordsModel record = medicalRecordsRepository.findByPatientsId(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Historia clínica no encontrada para el paciente con ID: " + patientId));

        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        response.setPatientId(record.getPatients().getId());
        response.setProfessionalId(record.getCreatedBy().getId());
        response.setDiagnosisIds(record.getDiagnoses().stream().map(DiagnosesModel::getId).toList());
        response.setFollowUpIds(record.getFollowUps().stream().map(FollowUpEntriesModel::getId).toList());

        return response;
    }

    public List<MedicalRecordsResponseDto> getAllMedicalRecords() {
        List<MedicalRecordsModel> records = medicalRecordsRepository.findAll();
        List<MedicalRecordsResponseDto> response = new ArrayList<>();
        for (MedicalRecordsModel record : records) {
            MedicalRecordsResponseDto dto = new MedicalRecordsResponseDto();
            dto.setId(record.getId());
            dto.setCreatedAt(record.getCreatedAt());
            dto.setUpdatedAt(record.getUpdatedAt());
            dto.setPatientId(record.getPatients().getId());
            dto.setProfessionalId(record.getCreatedBy().getId());
            dto.setDiagnosisIds(record.getDiagnoses().stream().map(DiagnosesModel::getId).toList());
            dto.setFollowUpIds(record.getFollowUps().stream().map(FollowUpEntriesModel::getId).toList());
            response.add(dto);
        }
        return response;
    }

    @Transactional
    public void delete(Long id) {
        MedicalRecordsModel record = medicalRecordsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));
        medicalRecordsRepository.delete(record);
    }

    //paginacion medical records
    public Page<MedicalRecordsResponseDto>  getMedicalRecordsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return medicalRecordsRepository.findAll(pageable).map(this::mapToDto);
    }

    private MedicalRecordsResponseDto mapToDto(MedicalRecordsModel medicalRecordsModel) {
        return new MedicalRecordsResponseDto(
                medicalRecordsModel.getId(),
                medicalRecordsModel.getCreatedAt(),
                medicalRecordsModel.getUpdatedAt(),
                medicalRecordsModel.getPatients().getId(),
                medicalRecordsModel.getCreatedBy().getId(),
                medicalRecordsModel.getDiagnoses().stream().map(DiagnosesModel::getId).toList(),
                medicalRecordsModel.getFollowUps().stream().map(FollowUpEntriesModel::getId).toList()
        );
    }


    public List<MedicalRecordsSummaryDto> getFilteredHistory(
            String specialty,
            String professionalName,
            LocalDate date
    ) {
        String nameQuery = (professionalName != null && !professionalName.isBlank())
                ? "%" + professionalName.toLowerCase() + "%"
                : null;

        List<MedicalRecordsModel> records = medicalRecordsRepository.filterClinicalHistory(
                specialty,
                date,
                nameQuery
        );

        return records.stream()
                .map(record -> new MedicalRecordsSummaryDto(
                        record.getId(),
                        record.getCreatedBy().getSpecialty(),
                        record.getCreatedBy().getPerson().getName() + " " + record.getCreatedBy().getPerson().getLastName(),
                        record.getCreatedAt().toLocalDate()
                ))
                .toList();
    }
}
