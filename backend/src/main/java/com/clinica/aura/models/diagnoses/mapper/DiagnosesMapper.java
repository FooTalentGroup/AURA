package com.clinica.aura.models.diagnoses.mapper;

import com.clinica.aura.models.diagnoses.dtoRequest.DiagnosesRequestDto;
import com.clinica.aura.models.diagnoses.dtoRequest.DiagnosesRequestUpdateDto;
import com.clinica.aura.models.diagnoses.dtoResponse.DiagnosesResponseDto;
import com.clinica.aura.models.diagnoses.model.DiagnosesModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiagnosesMapper {

    private final ModelMapper modelMapper;

    public DiagnosesResponseDto toDiagnosesResponseDto(DiagnosesModel diagnosesModel) {
        DiagnosesResponseDto dto = new DiagnosesResponseDto();
        dto.setId(diagnosesModel.getId());
        dto.setDate(diagnosesModel.getCreatedAt());
        dto.setIdProfessional(diagnosesModel.getCreatedBy().getId());
        dto.setTitle(diagnosesModel.getTitle());
        dto.setDetails(diagnosesModel.getDetails());
        dto.setMedicalRecordId(diagnosesModel.getMedicalRecord().getId());
        return dto;
    }

    public DiagnosesModel toDiagnosesModel(DiagnosesRequestDto diagnosesRequestDto) {
        return modelMapper.map(diagnosesRequestDto, DiagnosesModel.class);
    }

    public void updateEntityFromDto(DiagnosesRequestUpdateDto dto, DiagnosesModel entity) {

        TypeMap<DiagnosesRequestUpdateDto, DiagnosesModel> typeMap = modelMapper.getTypeMap(DiagnosesRequestUpdateDto.class, DiagnosesModel.class);

        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(DiagnosesRequestUpdateDto.class, DiagnosesModel.class);
            typeMap.addMappings(mapper -> {
                mapper.skip(DiagnosesModel::setId);
                mapper.skip(DiagnosesModel::setCreatedAt);
                mapper.skip(DiagnosesModel::setCreatedBy);
                mapper.skip(DiagnosesModel::setMedicalRecord);
                mapper.skip(DiagnosesModel::setUpdatedBy);
                mapper.skip(DiagnosesModel::setUpdatedAt);
            });
        }

        modelMapper.map(dto, entity);
    }
}
