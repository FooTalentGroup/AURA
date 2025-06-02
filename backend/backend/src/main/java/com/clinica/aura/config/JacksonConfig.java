package com.clinica.aura.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * Configura y proporciona una instancia de ObjectMapper para la aplicación.
     *
     * @return un ObjectMapper configurado para:
     *         - Manejar tipos de tiempo Java (LocalDate, LocalDateTime, Instant, etc.)
     *         - Deshabilitar la serialización de fechas como timestamps
     *         - Usar formatos ISO-8601 para fechas y horas
     *
     * Configuración específica:
     * 1. Registra el módulo JavaTimeModule para manejo de tipos de tiempo modernos
     * 2. Deshabilita la serialización de fechas como timestamps numéricos
     *
     * Nota: Esta configuración asegura que las fechas se manejen de manera consistente
     *       en toda la aplicación, usando formatos estándar ISO-8601.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
