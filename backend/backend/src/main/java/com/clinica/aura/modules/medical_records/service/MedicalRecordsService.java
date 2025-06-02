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

    /**
     * Crea un nuevo registro médico para un paciente.
     *
     * @param dto los datos del registro médico a crear
     * @return un MedicalRecordsResponseDto con los detalles del registro creado
     * @throws PatientNotFoundException si el paciente no existe
     * @throws ConflictWithExistingRecord si el paciente ya tiene un registro médico
     *
     * Proceso detallado:
     * 1. Valida que el paciente exista
     * 2. Verifica que no exista otro registro médico para el mismo paciente
     * 3. Obtiene el profesional autenticado actual
     * 4. Crea un nuevo registro médico con:
     *    - Creador y actualizador: el profesional autenticado
     *    - Paciente asociado
     * 5. Convierte el modelo a DTO incluyendo:
     *    - ID del registro
     *    - Fechas de creación y actualización
     *    - ID del paciente
     *    - ID del profesional
     *    - IDs de diagnósticos asociados (si existen)
     *    - IDs de entradas de seguimiento asociadas (si existen)
     *
     * Nota: La transacción es manejada por @Transactional para garantizar la integridad de los datos
     */
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

        List<Long> diagnosisIds = Optional.ofNullable(record.getDiagnoses())
                .orElse(Collections.emptyList())
                .stream()
                .map(DiagnosesModel::getId)
                .toList();
        response.setDiagnosisIds(diagnosisIds);

        List<Long> followUpIds = Optional.ofNullable(record.getFollowUps())
                .orElse(Collections.emptyList())
                .stream()
                .map(FollowUpEntriesModel::getId)
                .toList();
        response.setFollowUpIds(followUpIds);

        return response;
    }

    /**
     * Busca un registro médico por su ID.
     *
     * @param id el ID del registro médico a buscar
     * @return un MedicalRecordsResponseDto con los detalles del registro encontrado
     * @throws EntityNotFoundException si no se encuentra el registro
     *
     * El DTO incluye:
     * - ID del registro
     * - Fechas de creación y actualización
     * - ID del paciente
     * - ID del profesional
     * - IDs de diagnósticos asociados
     * - IDs de entradas de seguimiento
     */

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

    /**
     * Busca la historia clínica de un paciente por su ID.
     *
     * @param patientId el ID del paciente a buscar
     * @return un MedicalRecordsResponseDto con los detalles de la historia clínica
     * @throws EntityNotFoundException si no se encuentra la historia clínica
     *
     * El DTO incluye:
     * - ID del registro
     * - Fechas de creación y actualización
     * - ID del paciente
     * - ID del profesional
     * - IDs de diagnósticos asociados
     * - IDs de entradas de seguimiento
     *
     * Nota: Este método asume que cada paciente tiene una única historia clínica
     */
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

    /**
     * Obtiene todos los registros médicos.
     *
     * @return una lista de MedicalRecordsResponseDto con los detalles de cada registro
     *
     * El DTO incluye:
     * - ID del registro
     * - Fechas de creación y actualización
     * - ID del paciente
     * - ID del profesional
     * - IDs de diagnósticos asociados
     * - IDs de entradas de seguimiento
     */

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

    /**
     * Obtiene una página de registros médicos.
     *
     * @param page el número de la página a obtener
     * @param size el tamaño de la página
     * @return una página de MedicalRecordsResponseDto con los detalles de cada registro
     *
     * El DTO incluye:
     * - ID del registro
     * - Fechas de creación y actualización
     * - ID del paciente
     * - ID del profesional
     * - IDs de diagnósticos asociados
     * - IDs de entradas de seguimiento
     */

    public Page<MedicalRecordsResponseDto>  getMedicalRecordsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return medicalRecordsRepository.findAll(pageable).map(this::mapToDto);
    }


    /**
     * Mapea un MedicalRecordsModel a un MedicalRecordsResponseDto.
     *
     * @param medicalRecordsModel el MedicalRecordsModel a mapear
     * @return un MedicalRecordsResponseDto con los detalles del MedicalRecordsModel
     */

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


    /**
     * Obtiene el historial clínico filtrado por especialidad, profesional y fecha.
     *
     * @param specialty la especialidad
     * @param professionalName el nombre del profesional
     * @param date la fecha
     * @return una lista de MedicalRecordsSummaryDto con los detalles de cada registro
     *
     * El DTO incluye:
     * - ID del registro
     * - Especialidad del profesional
     * - Nombre y apellido del profesional
     * - Fecha de creación del registro
     */

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
