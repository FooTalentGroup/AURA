package com.clinica.aura.modules.follow_up_entries.service;

import com.clinica.aura.exceptions.MedicalRecordsNotFoundException;
import com.clinica.aura.modules.follow_up_entries.dtoRequest.FollowUpEntriesDtoRequest;
import com.clinica.aura.modules.follow_up_entries.dtoRequest.FollowUpEntriesDtoRequestUpdate;
import com.clinica.aura.modules.follow_up_entries.dtoResponse.FollowUpEntriesDtoResponse;
import com.clinica.aura.modules.follow_up_entries.model.FollowUpEntriesModel;
import com.clinica.aura.modules.follow_up_entries.repository.FollowUpEntriesRepository;
import com.clinica.aura.modules.medical_records.model.MedicalRecordsModel;
import com.clinica.aura.modules.medical_records.repository.MedicalRecordsRepository;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import com.clinica.aura.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para gestionar los registros de seguimiento (FollowUpEntries) asociados a las historias clínicas.
 * Permite crear, actualizar, eliminar y obtener registros individuales o paginados.
 *
 * Utiliza seguridad para asociar al profesional autenticado con los registros creados o modificados.
 */
@RequiredArgsConstructor
@Service
public class FollowUpEntriesService {
    private final FollowUpEntriesRepository followUpEntriesRepository;
    private final MedicalRecordsRepository medicalRecordsRepository;
    private final SecurityUtil securityUtil;

    /**
     * Crea un nuevo registro de seguimiento asociado a una historia clínica existente.
     *
     * @param dto DTO con los datos del registro de seguimiento.
     * @return DTO de respuesta con los datos guardados.
     * @throws MedicalRecordsNotFoundException si no se encuentra la historia clínica.
     */
    @Transactional
    public FollowUpEntriesDtoResponse create(FollowUpEntriesDtoRequest dto) {
        MedicalRecordsModel medicalRecord = medicalRecordsRepository.findById(dto.getMedicalRecordId())
                .orElseThrow(() -> new MedicalRecordsNotFoundException(
                        "La historia clínica con ID " + dto.getMedicalRecordId() + " no existe"));

        ProfessionalModel professionalModel = securityUtil.getAuthenticatedProfessional();
        FollowUpEntriesModel record = new FollowUpEntriesModel();
        record.setCreatedBy(professionalModel);
        record.setUpdatedBy(professionalModel);
        record.setMedicals(medicalRecord);
        record.setObservations(dto.getObservations());
        record.setInterventions(dto.getInterventions());
        record.setNextSessionInstructions(dto.getNextSessionInstructions());

        followUpEntriesRepository.save(record);

        FollowUpEntriesDtoResponse response = new FollowUpEntriesDtoResponse();
        response.setId(record.getId());
        response.setObservations(record.getObservations());
        response.setInterventions(record.getInterventions());
        response.setNextSessionInstructions(record.getNextSessionInstructions());
        response.setMedicalRecordId(record.getMedicals().getId());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        response.setProfessionalId(record.getCreatedBy().getId());

        return response;

    }

    /**
     * Busca un registro de seguimiento por su ID.
     *
     * @param id Identificador del registro.
     * @return DTO con los datos del registro encontrado.
     * @throws EntityNotFoundException si el registro no existe.
     */
    public FollowUpEntriesDtoResponse findById(Long id) {
        FollowUpEntriesModel record = followUpEntriesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro con id "+ id + " no encontrado"));
        FollowUpEntriesDtoResponse response = new FollowUpEntriesDtoResponse();
        response.setId(record.getId());
        response.setObservations(record.getObservations());
        response.setInterventions(record.getInterventions());
        response.setNextSessionInstructions(record.getNextSessionInstructions());

        response.setMedicalRecordId(record.getMedicals().getId());

        response.setProfessionalId(record.getUpdatedBy().getId());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());

        return response;
    }

    /**
     * Elimina un registro de seguimiento por su ID.
     *
     * @param id Identificador del registro a eliminar.
     * @throws EntityNotFoundException si el registro no existe.
     */
    @Transactional
    public void delete(Long id) {
        FollowUpEntriesModel record = followUpEntriesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro con id "+ id + " no encontrado"));
        followUpEntriesRepository.delete(record);
    }

    /**
     * Actualiza un registro de seguimiento existente.
     *
     * @param id  Identificador del registro a actualizar.
     * @param dto DTO con los nuevos datos.
     * @return DTO de respuesta con los datos actualizados.
     * @throws EntityNotFoundException si el registro no existe.
     */
    @Transactional
    public FollowUpEntriesDtoResponse update(Long id, FollowUpEntriesDtoRequestUpdate dto) {
        FollowUpEntriesModel record = followUpEntriesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro con id "+ id + " no encontrado"));

        ProfessionalModel professionalModel = securityUtil.getAuthenticatedProfessional();
        record.setUpdatedBy(professionalModel);
        record.setObservations(dto.getObservations());
        record.setInterventions(dto.getInterventions());
        record.setNextSessionInstructions(dto.getNextSessionInstructions());

        followUpEntriesRepository.save(record);

        FollowUpEntriesDtoResponse response = new FollowUpEntriesDtoResponse();

        response.setId(record.getId());
        response.setObservations(record.getObservations());
        response.setInterventions(record.getInterventions());
        response.setNextSessionInstructions(record.getNextSessionInstructions());
        response.setUpdatedAt(record.getUpdatedAt());
        response.setCreatedAt(record.getCreatedAt());
        response.setMedicalRecordId(record.getMedicals().getId());
        response.setProfessionalId(record.getUpdatedBy().getId());

        return response;
    }

    /**
     * Obtiene una página de registros de seguimiento.
     *
     * @param page Número de página (empezando desde 0).
     * @param size Tamaño de la página.
     * @return Página con los registros en formato DTO.
     */
    public Page<FollowUpEntriesDtoResponse> getFollowUpEntriesPage(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return followUpEntriesRepository.findAll(pageable).map(this::mapToDto);
    }

    /**
     * Convierte un modelo de entidad a un DTO de respuesta.
     *
     * @param followUpEntriesModel La entidad a convertir.
     * @return DTO de respuesta.
     */
    private FollowUpEntriesDtoResponse mapToDto(FollowUpEntriesModel followUpEntriesModel) {
        return new FollowUpEntriesDtoResponse(
                followUpEntriesModel.getId(),
                followUpEntriesModel.getObservations(),
                followUpEntriesModel.getInterventions(),
                followUpEntriesModel.getNextSessionInstructions(),
                followUpEntriesModel.getCreatedAt(),
                followUpEntriesModel.getUpdatedAt(),
                followUpEntriesModel.getUpdatedBy().getId(),
                followUpEntriesModel.getMedicals().getId()
        );
    }
}
