package com.clinica.aura.modules.clinical_report.service;

import com.clinica.aura.exceptions.ProfessionalNotFoundException;
import com.clinica.aura.exceptions.UnauthorizedAccessException;
import com.clinica.aura.modules.clinical_report.dto.ClinicalReportRequestDto;
import com.clinica.aura.modules.clinical_report.dto.ClinicalReportResponseDto;
import com.clinica.aura.modules.clinical_report.model.ClinicalReportModel;
import com.clinica.aura.modules.clinical_report.repository.ClinicalReportRepository;
import com.clinica.aura.modules.medical_background.model.MedicalBackgroundModel;
import com.clinica.aura.modules.medical_background.repository.MedicalBackgroundRepository;
import com.clinica.aura.modules.person.model.PersonModel;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import com.clinica.aura.modules.professional.repository.ProfessionalRepository;
import com.clinica.aura.modules.user_account.models.UserModel;
import com.clinica.aura.modules.user_account.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicalReportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClinicalReportRepository clinicalReportRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private MedicalBackgroundRepository medicalBackgroundRepository;


    /**
     * Crea un nuevo informe clínico
     * @param dto
     * @return
     */
    public ClinicalReportResponseDto create(ClinicalReportRequestDto dto) {
        // 1. Validar existencia del MedicalBackground
        MedicalBackgroundModel background = medicalBackgroundRepository.findById(dto.getMedicalBackgroundId())
                .orElseThrow(() -> new EntityNotFoundException("Antecedente médico no encontrado"));

        // 2. Obtener email autenticado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            throw new UnauthorizedAccessException("Debe iniciar sesión un profesional para registrar el informe clínico");
        }

        // 3. Obtener usuario y profesional
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email " + email + " no existe"));

        PersonModel person = user.getPerson();
        ProfessionalModel professional = professionalRepository.findByPerson(person)
                .orElseThrow(() -> new ProfessionalNotFoundException("El profesional con  ID " + person.getId() + " no existe"));

        // 4. Crear y guardar el modelo
        ClinicalReportModel report = new ClinicalReportModel();
        report.setName(dto.getName());
        report.setDate(dto.getDate());
        report.setProfessional(professional);
        report.setMedicalBackground(background);

        clinicalReportRepository.save(report);

        // 5. Retornar DTO
        return toDto(report);
    }


    /**
     * Busca todos los informes clínicos
     * @return
     */

    public List<ClinicalReportResponseDto> findAll() {
        return clinicalReportRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ClinicalReportResponseDto findById(Long id) {
        ClinicalReportModel report = clinicalReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Informe clínico no encontrado"));
        return toDto(report);
    }

    //método para modifica un informe clinico
    public ClinicalReportResponseDto update(Long id, ClinicalReportRequestDto dto) {
        // 1. Buscar el informe
        ClinicalReportModel report = clinicalReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Informe clínico no encontrado"));

        // 2. Verificar autenticación
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            throw new UnauthorizedAccessException("Debe iniciar sesión un profesional para modificar el informe clínico");
        }

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email " + email + " no existe"));

        PersonModel person = user.getPerson();

        ProfessionalModel professional = professionalRepository.findByPerson(person)
                .orElseThrow(() -> new ProfessionalNotFoundException("El profesional no existe"));

        // 3. Validar que sea el profesional que creó el informe)
        if (!report.getProfessional().getId().equals(professional.getId())) {
            throw new UnauthorizedAccessException("No tiene permiso para modificar este informe clínico");
        }

        // 4. Actualizar los campos permitidos
        report.setName(dto.getName());
        report.setDate(dto.getDate());

        //  No dejamos cambiar el profesional ni el antecedente médico (ya están seteados)

        clinicalReportRepository.save(report);
        return toDto(report);
    }

    /**
     * Elimina un informe clínico
     * @param id
     */

    public void delete(Long id) {
        ClinicalReportModel report = clinicalReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Informe clínico no encontrado"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            throw new UnauthorizedAccessException("Debe iniciar sesión para eliminar informes clínicos");
        }

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email " + email + " no existe"));

        ProfessionalModel professional = professionalRepository.findByPerson(user.getPerson())
                .orElseThrow(() -> new ProfessionalNotFoundException("El profesional no existe"));

        // Validar que sea el creador del informe
        if (!report.getProfessional().getId().equals(professional.getId())) {
            throw new UnauthorizedAccessException("No tiene permiso para eliminar este informe clínico");
        }

        clinicalReportRepository.delete(report);
    }

    /**
     * Convierte el modelo a DTO
     * @param model
     */
    private ClinicalReportResponseDto toDto(ClinicalReportModel model) {
        ClinicalReportResponseDto dto = new ClinicalReportResponseDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDate(model.getDate());
        dto.setProfessionalId(model.getProfessional().getId());
        dto.setProfessionalName(
                model.getProfessional().getPerson().getName() + " " +
                        model.getProfessional().getPerson().getLastName()
        );
        dto.setMedicalBackgroundId(model.getMedicalBackground().getId());
        return dto;
    }
}