package com.clinica.aura.models.receptionist.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionistRequestUpdateDto {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El correo electrónica no es válido")
    @Schema(description = "Correo electrónico del recepcionista", example = "juan.perez@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del recepcionista",requiredMode = Schema.RequiredMode.REQUIRED, example = "admin123")
    private String password;

    // Datos de la persona
    @NotBlank(message = "El DNI es obligatorio")
    @Schema(description = "DNI del recepcionista", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345678A")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del recepcionista", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido del recepcionista", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Schema(description = "Número de teléfono del recepcionista", example = "+573001112233")
    private String phoneNumber;

//    @Schema(description = "País de residencia del paciente", example = "Colombia")
//    private String country; // campo que se pide eliminar 02/05/2025
//
//    @Schema(description = "URL de la foto del paciente", example = "https://example.com/foto.jpg")
//    private String photoUrl; // campo que se pide eliminar 02/05/2025

    @NotBlank(message = "La localidad es obligatoria")
    @Schema(description = "Localidad del recepcionista", example = "Bogotá", requiredMode = Schema.RequiredMode.REQUIRED)
    private String locality;

    @NotBlank(message = "La dirección es obligatoria")
    @Schema(description = "Dirección del recepcionista", example = "Calle 1 # 1-1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;
    //
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Schema(description = "Fecha de nacimiento del recepcionista", example = "1990-05-20", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate birthDate;

    @NotBlank(message = "El cuil es obligatorio")
    @Schema(description = "Cuil del recepcionista", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cuil;
}
