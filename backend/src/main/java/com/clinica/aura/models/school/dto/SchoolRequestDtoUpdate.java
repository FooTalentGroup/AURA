package com.clinica.aura.models.school.dto;

import com.clinica.aura.models.patient.model.PatientModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SchoolRequestDtoUpdate {
    @Schema(description = "Nombre de la escuela a la que asiste el paciente", example = "Instituto Modelo", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre de la escuela es obligatorio")
    private String schoolName;


    @NotBlank(message = "El email de la escuela es obligatorio")
    @Email(message = "El email debe tener un formato válido. Ejemplo = rayuela@gmail.com")
    private String emailSchool;

    @Schema(description = "El teléfono esta pensado en modo argentino, se permite el mínimo de 8 números en caso de no agregar el 011 ", example = "02320484070, 1155150791, 01144697500", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^\\d{8,11}$", message = "El teléfono debe tener entre 8 y 11 dígitos numéricos")
    @NotBlank(message = "El teléfono de la escuela es obligatorio")
    private String phoneSchool;

    //private List<PatientModel> patients;
    /*comento la lista para que no aparezca en swagger
    * solo quiero los campos de la escuela
    * */
}
