package com.clinica.aura.config;

import com.clinica.aura.models.diagnoses.dtoRequest.DiagnosesRequestDto;
import com.clinica.aura.models.diagnoses.model.DiagnosesModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig  {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(DiagnosesRequestDto.class, DiagnosesModel.class)
                .addMappings(mapper -> mapper.skip(DiagnosesModel::setId));

        return modelMapper;
    }
}
