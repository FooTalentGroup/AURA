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
            regexp = "^(?=(.*[A-Za-zÁÉÍÓÚÜÑáéíóúüñ]){1,25})(?=(.*\\d){0,4})[A-Za-zÁÉÍÓÚÜÑáéíóúüñ\\d\\s°]+$",
            message = "El nombre de la escuela puede tener hasta 25 letras y un máximo de 4 números, permite el caracter °."
    )
    @Size(min = 6, max = 30, message = "El nombre de la escuela no puede exceder los 30 caracteres.")
    @Schema(description = "Nombre de la escuela (máximo 25 letras y 4 números, también acepta el caracter especial °). Ejemplo: 'Jardin maternal n°5'")
    private String schoolName;

    @Schema(description = "Correo electrónico del referente de la escuela",
            example = "gracielaPaez@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "El correo electrónico debe tener un formato válido (ej: nombre@dominio.com)")
    @NotBlank(message = "El correo de la escuela es obligatorio")
    private String emailSchool;

    @Schema(description = "El teléfono esta pensado en modo argentino, se permite el mínimo de 8 números en caso de" +
            " no agregar el 011 ", example = "02320484070, 1155150791, 01144697500", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^\\d{8,11}$", message = "El teléfono debe tener entre 8 y 11 dígitos numéricos")
    @NotBlank(message = "El teléfono de la escuela es obligatorio")
    private String phoneSchool;

   //private List<PatientModel>patients;
    /*comento la lista para que no salga en swagger
     * en los model de las tablas y en los metodos persiste la relacion de
     * forma correcta
     * */

}

