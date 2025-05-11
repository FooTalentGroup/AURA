package com.clinica.aura.models.medical_records.service;

import com.clinica.aura.models.medical_records.dtoRequest.*;
import com.clinica.aura.models.medical_records.dtoResponse.MedicalRecordFilterRequest;
import com.clinica.aura.models.medical_records.dtoResponse.MedicalRecordsResponseDto;

import com.clinica.aura.models.medical_records.dtoResponse.MedicalRecordsSummaryDto;
import com.clinica.aura.models.medical_records.model.MedicalRecordsModel;
import com.clinica.aura.models.medical_records.repository.MedicalRecordsRepository;
import com.clinica.aura.models.patient.model.PatientModel;
import com.clinica.aura.models.patient.repository.PatientRepository;
import com.clinica.aura.models.person.model.PersonModel;
import com.clinica.aura.models.professional.model.ProfessionalModel;
import com.clinica.aura.models.professional.repository.ProfessionalRepository;
import com.clinica.aura.models.user_account.models.UserModel;
import com.clinica.aura.models.user_account.repository.UserRepository;
import com.clinica.aura.exceptions.ConflictWithExistingRecord;
import com.clinica.aura.exceptions.PatientNotFoundException;
import com.clinica.aura.exceptions.ProfessionalNotFoundException;
import com.clinica.aura.exceptions.UnauthorizedAccessException;
import com.clinica.aura.util.PaginatedResponse;
import com.clinica.aura.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordsService {

    private final MedicalRecordsRepository medicalRecordsRepository;
    private final PatientRepository patientRepository;
    private final SecurityUtil securityUtil;
    private final ModelMapper mapper;

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
        record.setSpecialty(dto.getSpeciality());
        medicalRecordsRepository.save(record);

        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setSpecialty(record.getSpecialty());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        response.setPatientId(record.getPatients().getId());
        response.setProfessionalId(record.getCreatedBy().getId());

        return response;
    }

    public MedicalRecordsResponseDto findById(Long id) {
        MedicalRecordsModel record = medicalRecordsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));
        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setSpecialty(record.getSpecialty());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        response.setPatientId(record.getPatients().getId());
        response.setProfessionalId(record.getCreatedBy().getId());

        return response;
    }
    public MedicalRecordsResponseDto findByPatientsId(Long patientId){
        MedicalRecordsModel record = medicalRecordsRepository.findByPatientsId(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Historia clínica no encontrada para el paciente con ID: " + patientId));

        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setSpecialty(record.getSpecialty());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        response.setPatientId(record.getPatients().getId());
        response.setProfessionalId(record.getCreatedBy().getId());

        return response;
    }

    public List<MedicalRecordsResponseDto> getAllMedicalRecords() {
        List<MedicalRecordsModel> records = medicalRecordsRepository.findAll();
        List<MedicalRecordsResponseDto> response = new ArrayList<>();
        for (MedicalRecordsModel record : records) {
            MedicalRecordsResponseDto dto = new MedicalRecordsResponseDto();
            dto.setId(record.getId());
            dto.setSpecialty(record.getSpecialty());
            dto.setCreatedAt(record.getCreatedAt());
            dto.setUpdatedAt(record.getUpdatedAt());
            dto.setPatientId(record.getPatients().getId());
            dto.setProfessionalId(record.getCreatedBy().getId());
            response.add(dto);
        }
        return response;
    }

    public MedicalRecordsResponseDto update(Long id, MedicalRecordsRequestUpdateDto dto) {
        MedicalRecordsModel record = medicalRecordsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));

        // Obtener profesional autenticado
        ProfessionalModel professional = securityUtil.getAuthenticatedProfessional();

        // Actualizar campos
        record.setSpecialty(dto.getSpeciality());
        record.setUpdatedBy(professional);
        medicalRecordsRepository.save(record);

        // Construir respuesta
        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setSpecialty(record.getSpecialty());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        response.setPatientId(record.getPatients().getId());
        response.setProfessionalId(record.getCreatedBy().getId());

        return response;
    }


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
                medicalRecordsModel.getSpecialty(),
                medicalRecordsModel.getCreatedAt(),
                medicalRecordsModel.getUpdatedAt(),
                medicalRecordsModel.getPatients().getId(),
                medicalRecordsModel.getCreatedBy().getId()
        );
    }


    public List<MedicalRecordsSummaryDto> getFilteredHistory(
            String specialty,
            Long professionalId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        List<MedicalRecordsModel> records = medicalRecordsRepository.filterClinicalHistory(
                specialty,
                professionalId,
                startDateTime,
                endDateTime
        );

        return records.stream().map(record ->
                new MedicalRecordsSummaryDto(
                        record.getSpecialty(),
                        record.getCreatedBy().getPerson().getName() + " " + record.getCreatedBy().getPerson().getLastName(),
                        record.getCreatedAt().toLocalDate()
                )
        ).toList();
    }

}
