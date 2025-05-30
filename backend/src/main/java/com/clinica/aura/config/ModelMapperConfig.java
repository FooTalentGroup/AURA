package com.clinica.aura.config;

import com.clinica.aura.modules.diagnoses.dtoRequest.DiagnosesRequestDto;
import com.clinica.aura.modules.diagnoses.model.DiagnosesModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig  {

    /**
     * Configura y proporciona una instancia de ModelMapper para la aplicación.
     *
     * @return una instancia de ModelMapper configurada para:
     *         - Mapear entre DTOs y modelos de dominio
     *         - Omitir el ID durante la creación de nuevos diagnósticos
     *
     * Configuración específica:
     * 1. Mapeo entre DiagnosesRequestDto y DiagnosesModel
     * 2. Evita la asignación automática del ID en DiagnosesModel
     *
     * Nota: El ID se omite para permitir que la base de datos genere automáticamente
     *       los identificadores de los nuevos diagnósticos.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(DiagnosesRequestDto.class, DiagnosesModel.class)
                .addMappings(mapper -> mapper.skip(DiagnosesModel::setId));

        return modelMapper;
    }
}
