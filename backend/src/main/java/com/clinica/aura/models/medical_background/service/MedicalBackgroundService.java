package com.clinica.aura.models.medical_background.service;

import com.clinica.aura.models.medical_background.dto.MedicalBackgroundRequestDto;
import com.clinica.aura.models.medical_background.dto.MedicalBackgroundResponseDto;
import com.clinica.aura.models.medical_background.model.MedicalBackgroundModel;
import com.clinica.aura.models.medical_background.repository.MedicalBackgroundRepository;
import com.clinica.aura.models.patient.repository.PatientRepository;
import com.clinica.aura.models.professional.repository.ProfessionalRepository;
import com.clinica.aura.models.patient.model.PatientModel;
import com.clinica.aura.models.professional.model.ProfessionalModel;
import com.clinica.aura.exceptions.ConflictWithExistingRecord;
import com.clinica.aura.exceptions.UnauthorizedAccessException;
import com.clinica.aura.exceptions.ProfessionalNotFoundException;
import com.clinica.aura.models.user_account.models.UserModel;
import com.clinica.aura.models.user_account.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class MedicalBackgroundService {

    private final MedicalBackgroundRepository medicalBackgroundRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final ProfessionalRepository professionalRepository;

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

        // Crear un nuevo objeto MedicalBackgroundModel y asignar las propiedades del DTO
        MedicalBackgroundModel background = new MedicalBackgroundModel();
        background.setPatient(patient);
        background.setCreatedBy(professional);
        background.setUpdatedBy(professional);

        // Asignar alergias y discapacidades desde el DTO
        background.setAllergies(dto.getAllergies());
        background.setDisabilities(dto.getDisabilities());
        //asignamos informe medico
        background.setSchoolReports(dto.getSchoolReports());

        // Guardar el modelo de antecedentes médicos
        medicalBackgroundRepository.save(background);

        return mapToDto(background);
    }

    public MedicalBackgroundResponseDto findById(Long id) {
        MedicalBackgroundModel background = medicalBackgroundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Antecedente médico no encontrado con ID: " + id));
        return mapToDto(background);
    }

    public MedicalBackgroundResponseDto findByPatientId(Long patientId) {
        MedicalBackgroundModel background = medicalBackgroundRepository.findByPatientId(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Antecedente médico no encontrado para el paciente con ID: " + patientId));
        return mapToDto(background);
    }

    public List<MedicalBackgroundResponseDto> findAll() {
        return medicalBackgroundRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public MedicalBackgroundResponseDto update(Long id, MedicalBackgroundRequestDto dto) {
        MedicalBackgroundModel background = medicalBackgroundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Antecedente médico no encontrado con ID: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        ProfessionalModel professional = professionalRepository.findByPerson(user.getPerson())
                .orElseThrow(() -> new ProfessionalNotFoundException("Profesional no encontrado para el usuario con id: " + user.getPerson().getId()));

        // Actualizar las alergias y discapacidades
        background.setAllergies(dto.getAllergies());
        background.setDisabilities(dto.getDisabilities());
        background.setUpdatedBy(professional);

        medicalBackgroundRepository.save(background);

        return mapToDto(background);
    }

    public void delete(Long id) {
        MedicalBackgroundModel background = medicalBackgroundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Antecedente médico no encontrado con ID: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            throw new UnauthorizedAccessException("Debe iniciar sesión un profesional para eliminar antecedentes médicos");
        }

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        professionalRepository.findByPerson(user.getPerson())
                .orElseThrow(() -> new ProfessionalNotFoundException("Profesional no encontrado para el usuario con id: " + user.getPerson().getId()));


        // Eliminar primero alergias y discapacidades asociadas
        background.getAllergies().clear();
        background.getDisabilities().clear();
        medicalBackgroundRepository.save(background);

        //  eliminar
        medicalBackgroundRepository.delete(background);

    }


    private MedicalBackgroundResponseDto mapToDto(MedicalBackgroundModel model) {
        return MedicalBackgroundResponseDto.builder()
                .id(model.getId())
                .patientId(model.getPatient().getId())
                .allergies(model.getAllergies())
                .disabilities(model.getDisabilities())
                .schoolReports(model.getSchoolReports())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }
}
