package com.clinica.aura.modules.medical_background.service;


import com.clinica.aura.modules.medical_background.dto.MedicalBackgroundRequestDto;
import com.clinica.aura.modules.medical_background.dto.MedicalBackgroundResponseDto;
import com.clinica.aura.modules.medical_background.model.MedicalBackgroundModel;
import com.clinica.aura.modules.medical_background.repository.MedicalBackgroundRepository;
import com.clinica.aura.modules.patient.repository.PatientRepository;
import com.clinica.aura.modules.professional.repository.ProfessionalRepository;
import com.clinica.aura.modules.patient.model.PatientModel;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import com.clinica.aura.exceptions.ConflictWithExistingRecord;
import com.clinica.aura.exceptions.UnauthorizedAccessException;
import com.clinica.aura.exceptions.ProfessionalNotFoundException;
import com.clinica.aura.modules.user_account.models.UserModel;
import com.clinica.aura.modules.user_account.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de antecedentes médicos de pacientes.
 * Permite crear, consultar, actualizar y eliminar antecedentes médicos.
 */
@Service
@RequiredArgsConstructor
public class MedicalBackgroundService {

    private final MedicalBackgroundRepository medicalBackgroundRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final ProfessionalRepository professionalRepository;

    /**
     * Crea un nuevo antecedente médico para un paciente.
     *
     * @param dto Objeto con los datos necesarios para crear el antecedente.
     * @return DTO de respuesta con la información del antecedente creado.
     * @throws EntityNotFoundException si el paciente no existe.
     * @throws ConflictWithExistingRecord si el paciente ya tiene un antecedente registrado.
     * @throws UnauthorizedAccessException si el usuario no está autenticado.
     * @throws UsernameNotFoundException si no se encuentra el usuario autenticado.
     * @throws ProfessionalNotFoundException si el usuario autenticado no es un profesional.
     */
    @Transactional
    public MedicalBackgroundResponseDto create(MedicalBackgroundRequestDto dto) {
        PatientModel patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con ID: " + dto.getPatientId()));

        if (medicalBackgroundRepository.existsByPatientId(dto.getPatientId())) {
            throw new ConflictWithExistingRecord("El paciente con id " + dto.getPatientId() + " ya tiene antecedentes médicos registrados");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            throw new UnauthorizedAccessException("Debe iniciar sesión un profesional para registrar antecedentes médicos");
        }

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        ProfessionalModel professional = professionalRepository.findByPerson(user.getPerson())
                .orElseThrow(() -> new ProfessionalNotFoundException("Profesional no encontrado para el usuario con id: " + user.getPerson().getId()));

        MedicalBackgroundModel background = new MedicalBackgroundModel();
        background.setPatient(patient);
        background.setCreatedBy(professional);
        background.setUpdatedBy(professional);

        background.setAllergies(dto.getAllergies());
        background.setDisabilities(dto.getDisabilities());

        medicalBackgroundRepository.save(background);

        return mapToDto(background);
    }

    /**
     * Busca un antecedente médico por su ID.
     *
     * @param id ID del antecedente médico.
     * @return DTO de respuesta con los datos del antecedente.
     * @throws EntityNotFoundException si no se encuentra el antecedente con el ID dado.
     */
    public MedicalBackgroundResponseDto findById(Long id) {
        MedicalBackgroundModel background = medicalBackgroundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Antecedente médico no encontrado con ID: " + id));
        return mapToDto(background);
    }



    /**
     * Busca un antecedente médico por el ID del paciente.
     *
     * @param patientId ID del paciente.
     * @return DTO de respuesta con los datos del antecedente médico.
     * @throws EntityNotFoundException si el paciente no tiene antecedentes registrados.
     */
    public MedicalBackgroundResponseDto findByPatientId(Long patientId) {
        MedicalBackgroundModel background = medicalBackgroundRepository.findByPatientId(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Antecedente médico no encontrado para el paciente con ID: " + patientId));
        return mapToDto(background);
    }

    /**
     * Obtiene la lista de todos los antecedentes médicos registrados.
     *
     * @return Lista de DTOs con los antecedentes médicos.
     */
    public List<MedicalBackgroundResponseDto> findAll() {
        return medicalBackgroundRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de un antecedente médico existente.
     *
     * @param id ID del antecedente médico a actualizar.
     * @param dto Objeto con los nuevos datos.
     * @return DTO de respuesta con los datos actualizados.
     * @throws EntityNotFoundException si no se encuentra el antecedente.
     * @throws UsernameNotFoundException si el usuario no existe.
     * @throws ProfessionalNotFoundException si el usuario no es un profesional.
     */
    @Transactional
    public MedicalBackgroundResponseDto update(Long id, MedicalBackgroundRequestDto dto) {
        MedicalBackgroundModel background = medicalBackgroundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Antecedente médico no encontrado con ID: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        ProfessionalModel professional = professionalRepository.findByPerson(user.getPerson())
                .orElseThrow(() -> new ProfessionalNotFoundException("Profesional no encontrado para el usuario con id: " + user.getPerson().getId()));

        background.setAllergies(dto.getAllergies());
        background.setDisabilities(dto.getDisabilities());

        background.setUpdatedBy(professional);

        medicalBackgroundRepository.save(background);

        return mapToDto(background);
    }


    /**
     * Convierte un modelo de entidad a un DTO de respuesta.
     *
     * @param model Entidad MedicalBackgroundModel.
     * @return DTO con los datos del modelo.
     */
    private MedicalBackgroundResponseDto mapToDto(MedicalBackgroundModel model) {


        return MedicalBackgroundResponseDto.builder()
                .id(model.getId())
                .patientId(model.getPatient() != null ? model.getPatient().getId() : null)
                .allergies(model.getAllergies())
                .disabilities(model.getDisabilities())

                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())

                .build();
    }

}
