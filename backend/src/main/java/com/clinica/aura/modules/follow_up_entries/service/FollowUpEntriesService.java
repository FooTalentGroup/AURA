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


@RequiredArgsConstructor
@Service
public class FollowUpEntriesService {
    private final FollowUpEntriesRepository followUpEntriesRepository;
    //private final FollowUpEntriesController followUpEntriesController;
    private final MedicalRecordsRepository medicalRecordsRepository;
    private final SecurityUtil securityUtil;

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

    public FollowUpEntriesDtoResponse findById(Long id) {
        FollowUpEntriesModel record = followUpEntriesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro con id "+ id + " no encontrado"));
        FollowUpEntriesDtoResponse response = new FollowUpEntriesDtoResponse();
        response.setId(record.getId());
        response.setObservations(record.getObservations());
        response.setInterventions(record.getInterventions());
        response.setNextSessionInstructions(record.getNextSessionInstructions());

        response.setMedicalRecordId(record.getMedicals().getId());

        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());

        return response;
    }

    public void delete(Long id) {
        FollowUpEntriesModel record = followUpEntriesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro con id "+ id + " no encontrado"));
        followUpEntriesRepository.delete(record);
    }

    public FollowUpEntriesDtoResponse update(Long id, FollowUpEntriesDtoRequestUpdate dto) {
        FollowUpEntriesModel record = followUpEntriesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro con id "+ id + " no encontrado"));

        //FollowUpEntriesModel record = new FollowUpEntriesModel();
        //record.setMedicals(medicalRecord);

        ProfessionalModel professionalModel = securityUtil.getAuthenticatedProfessional();
        record.setUpdatedBy(professionalModel);
        record.setObservations(dto.getObservations());
        record.setInterventions(dto.getInterventions());
        record.setNextSessionInstructions(dto.getNextSessionInstructions());
        //record.setUpdatedAt(dto.getUpdatedAt());

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

    public Page<FollowUpEntriesDtoResponse> getFollowUpEntriesPage(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return followUpEntriesRepository.findAll(pageable).map(this::mapToDto);
    }

    private FollowUpEntriesDtoResponse mapToDto(FollowUpEntriesModel followUpEntriesModel) {
        return new FollowUpEntriesDtoResponse(
                followUpEntriesModel.getId(),
                followUpEntriesModel.getObservations(),
                followUpEntriesModel.getInterventions(),
                followUpEntriesModel.getNextSessionInstructions(),
                followUpEntriesModel.getCreatedAt(),
                followUpEntriesModel.getUpdatedAt(),
                followUpEntriesModel.getCreatedBy().getId(),
                followUpEntriesModel.getMedicals().getId()
        );
    }
}
