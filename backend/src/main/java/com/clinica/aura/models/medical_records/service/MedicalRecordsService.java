package com.clinica.aura.models.medical_records.service;

import com.clinica.aura.models.medical_records.dtoRequest.*;
import com.clinica.aura.models.medical_records.dtoResponse.MedicalRecordsResponseDto;
import com.clinica.aura.models.medical_records.dtoResponse.MedicalRecordsResponseInfoDto;
import com.clinica.aura.models.medical_records.dtoResponse.MedicalRecordsSummaryDto;
import com.clinica.aura.models.medical_records.dtoResponse.ProfessionalSummaryDto;
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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordsService {

    private final MedicalRecordsRepository medicalRecordsRepository;
    private final PatientRepository patientRepository;
    private final ProfessionalRepository professionalRepository;
    private final UserRepository userRepository;

    public MedicalRecordsResponseDto create(MedicalRecordsRequestDto dto) {
        PatientModel patient = patientRepository.findById(dto.getPatientId()).orElseThrow(() -> new PatientNotFoundException("Paciente con id " + dto.getPatientId() + " no encontrado"));

        if(medicalRecordsRepository.existsByPatientId(dto.getPatientId())) {
            throw new ConflictWithExistingRecord("El paciente con id " + dto.getPatientId() + " ya tiene una historia clínica registrada");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if ("anonymousUser".equals(email)) {
            throw new UnauthorizedAccessException("Debe iniciar sesión un profesional para registrar la historia clínica");
        }
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con el email " + email + ", que intenta registrar la historia clínica, no existe"));

        PersonModel person = user.getPerson();

        ProfessionalModel professional = professionalRepository.findByPerson(person)
                .orElseThrow(() -> new ProfessionalNotFoundException("El profesional con id " + person.getId() + ", que intenta registrar la historia clínica, no existe"));

        MedicalRecordsModel record = new MedicalRecordsModel();
        record.setCreatedBy(professional);
        record.setUpdatedBy(professional);
        record.setPatients(patient);
        record.setNotes(dto.getNotes());
        //record.setAllergies(dto.getAllergies());
        record.setPreviousConditions(dto.getPreviousConditions());
        medicalRecordsRepository.save(record);

        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setNotes(record.getNotes());
        //response.setAllergies(record.getAllergies());
        response.setPreviousConditions(record.getPreviousConditions());
        response.setPatientId(record.getPatients().getId());

        return response;
    }

    public MedicalRecordsResponseInfoDto findById(Long id) {
        MedicalRecordsModel record = medicalRecordsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));
        MedicalRecordsResponseInfoDto response = new MedicalRecordsResponseInfoDto();
        response.setId(record.getId());
        response.setNotes(record.getNotes());
        //response.setAllergies(record.getAllergies());
        response.setPreviousConditions(record.getPreviousConditions());
        response.setPatientId(record.getPatients().getId());


        ProfessionalSummaryDto createdBy = new ProfessionalSummaryDto();
        createdBy.setId(record.getCreatedBy().getId());
        createdBy.setName(record.getCreatedBy().getPerson().getName());
        createdBy.setSpecialty(record.getCreatedBy().getSpecialty());
        response.setCreatedBy(createdBy);

        ProfessionalSummaryDto updatedBy = new ProfessionalSummaryDto();
        updatedBy.setId(record.getUpdatedBy().getId());
        updatedBy.setName(record.getUpdatedBy().getPerson().getName());
        updatedBy.setSpecialty(record.getUpdatedBy().getSpecialty());
        response.setUpdatedBy(updatedBy);

        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        return response;
    }
    public MedicalRecordsSummaryDto findByPatientsId(Long patientId){
        MedicalRecordsModel record = medicalRecordsRepository.findByPatientsId(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Historia clínica no encontrada para el paciente con ID: " + patientId));

        MedicalRecordsSummaryDto dto = new MedicalRecordsSummaryDto();
        dto.setId(record.getId());
        dto.setPatientId(record.getPatients().getId());
        dto.setNotes(record.getNotes());
        dto.setPreviousConditions(record.getPreviousConditions());
        dto.setCreatedById(record.getCreatedBy().getId());
        dto.setUpdatedById(record.getUpdatedBy().getId());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());

        return dto;
    }

    public List<MedicalRecordsResponseDto> getAllMedicalRecords() {
        List<MedicalRecordsModel> records = medicalRecordsRepository.findAll();
        List<MedicalRecordsResponseDto> response = new ArrayList<>();
        for (MedicalRecordsModel record : records) {
            MedicalRecordsResponseDto dto = new MedicalRecordsResponseDto();
            dto.setId(record.getId());
            dto.setNotes(record.getNotes());
            //dto.setAllergies(record.getAllergies());
            dto.setPreviousConditions(record.getPreviousConditions());
            dto.setPatientId(record.getPatients().getId());
            response.add(dto);
        }
        return response;
    }

    public MedicalRecordsResponseDto update(Long id, MedicalRecordsRequestUpdateDto dto) {
        MedicalRecordsModel record = medicalRecordsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));

        // Obtener profesional autenticado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con el email " + email + " no existe"));

        PersonModel person = user.getPerson();

        ProfessionalModel professional = professionalRepository.findByPerson(person)
                .orElseThrow(() -> new ProfessionalNotFoundException("El profesional con id " + person.getId() + " no existe"));

        // Actualizar campos
        record.setNotes(dto.getNotes());
        //record.setAllergies(dto.getAllergies());
        record.setPreviousConditions(dto.getPreviousConditions());
        record.setUpdatedBy(professional);
        medicalRecordsRepository.save(record);

        // Construir respuesta
        MedicalRecordsResponseDto response = new MedicalRecordsResponseDto();
        response.setId(record.getId());
        response.setNotes(record.getNotes());
        //response.setAllergies(record.getAllergies());
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
                //medicalRecordsModel.getAllergies(),
                medicalRecordsModel.getPreviousConditions(),
                medicalRecordsModel.getPatients().getId()
        );
    }
}
