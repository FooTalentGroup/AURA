package com.clinica.aura.models.school.dto;

import com.clinica.aura.models.patient.model.PatientModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SchoolRequestDto {
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚÑáéíóúñ0-9°\\s]{6,30}$",
            message = "El nombre de la escuela solo puede tener letras, números, espacios y el caracter °."
    )
    @Size(min = 6, max = 30, message = "El nombre de la escuela no puede exceder los 30 caracteres.")
    @Schema(
            description = "Nombre de la escuela (entre 6 y 30 caracteres). Solo letras, números, espacios y el caracter especial °. Ejemplo: 'Jardín Maternal Arcoiris °'"
    )
    private String schoolName;

    @Schema(description = "Correo electrónico del referente de la escuela",
            example = "gracielaPaez@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "Formato de email inválido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(\\.[A-Za-z]{2,})?$",
            message = "El email debe tener un dominio válido, como .com o .com.ar"
    )
    @NotBlank(message = "El correo de la escuela es obligatorio")
    private String emailSchool;

    @Schema(description = "El teléfono esta pensado en modo argentino, se permite el mínimo de 8 números en caso de" +
            " no agregar el 011 ", example = "02320484070, 1155150791, 01144697500", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^\\d{8,11}$", message = "El teléfono debe tener entre 8 y 11 dígitos numéricos")
    @NotBlank(message = "El teléfono de la escuela es obligatorio")
    private String phoneSchool;


}

