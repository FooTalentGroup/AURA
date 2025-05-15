package com.clinica.aura.modules.diagnoses.service;

import com.clinica.aura.exceptions.DianosesNotFoundException;
import com.clinica.aura.modules.diagnoses.dtoRequest.DiagnosesRequestDto;
import com.clinica.aura.modules.diagnoses.dtoRequest.DiagnosesRequestUpdateDto;
import com.clinica.aura.modules.diagnoses.dtoResponse.DiagnosesResponseDto;
import com.clinica.aura.modules.diagnoses.mapper.DiagnosesMapper;
import com.clinica.aura.modules.diagnoses.model.DiagnosesModel;
import com.clinica.aura.modules.diagnoses.repository.DiagnosesRepository;
import com.clinica.aura.exceptions.MedicalRecordsNotFoundException;
import com.clinica.aura.modules.medical_records.model.MedicalRecordsModel;
import com.clinica.aura.modules.medical_records.repository.MedicalRecordsRepository;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import com.clinica.aura.util.PaginatedResponse;
import com.clinica.aura.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosesService {

    private final DiagnosesRepository diagnosesRepository;
    private final MedicalRecordsRepository medicalRecordsRepository;
    private final DiagnosesMapper diagnosesMapper;
    private final SecurityUtil securityUtil;

    @Transactional
    public DiagnosesResponseDto createDiagnoses(DiagnosesRequestDto diagnosesRequestDto) {
        MedicalRecordsModel medicalRecordsModel = medicalRecordsRepository.findById(diagnosesRequestDto.getMedicalRecordId())
                .orElseThrow(() -> new MedicalRecordsNotFoundException("No se encontró la historia clínica con id: " + diagnosesRequestDto.getMedicalRecordId()));

        ProfessionalModel professional = securityUtil.getAuthenticatedProfessional();

        DiagnosesModel diagnosesModel = diagnosesMapper.toDiagnosesModel(diagnosesRequestDto);
        diagnosesModel.setMedicalRecord(medicalRecordsModel);
        diagnosesModel.setCreatedBy(professional);
        diagnosesModel.setUpdatedBy(professional);

        return diagnosesMapper.toDiagnosesResponseDto(diagnosesRepository.save(diagnosesModel));
    }


    public DiagnosesResponseDto updateDiagnoses(Long id, DiagnosesRequestUpdateDto diagnosesRequestUpdateDto) {
        DiagnosesModel diagnosesModel = diagnosesRepository.findById(id)
                .orElseThrow(() -> new DianosesNotFoundException("No se encontro el diagnostico con id: " + id));
        diagnosesMapper.updateEntityFromDto(diagnosesRequestUpdateDto, diagnosesModel);
        diagnosesModel.setUpdatedBy(securityUtil.getAuthenticatedProfessional());
        return diagnosesMapper.toDiagnosesResponseDto(diagnosesRepository.save(diagnosesModel));
    }

    public PaginatedResponse<DiagnosesResponseDto> getAllDiagnoses(int page, int size) {
        Page<DiagnosesModel> diagnoses = diagnosesRepository.findAll(PageRequest.of(page, size));
        List<DiagnosesResponseDto> content = diagnoses.getContent().stream()
                .map(diagnosesMapper::toDiagnosesResponseDto)
                .toList();
        return new PaginatedResponse<>(content, diagnoses.getNumber(), diagnoses.getSize(), diagnoses.getTotalPages(), diagnoses.getTotalElements());
    }

    public DiagnosesResponseDto getDiagnosesById(Long id) {
        DiagnosesModel diagnosesModel = diagnosesRepository.findById(id)
                .orElseThrow(() -> new DianosesNotFoundException("No se encontro el diagnostico con id: " + id));
        return diagnosesMapper.toDiagnosesResponseDto(diagnosesModel);
    }

    public void deleteDiagnoses(Long id) {
        DiagnosesModel diagnosesModel = diagnosesRepository.findById(id)
                .orElseThrow(() -> new DianosesNotFoundException("No se encontro el diagnostico con id: " + id));
        diagnosesRepository.delete(diagnosesModel);
    }

}
