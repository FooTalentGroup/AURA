package com.clinica.aura.modules.user_account.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeRequestDto {

    @NotBlank
    @Email(message = "Formato de email inválido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[A-Za-z]{2,}([A-Za-z]{2,})?$",
            message = "El email debe tener un dominio válido, como .com o .com.ar"
    )
    @Schema(description = "Email del paciente", example = "9k6w5@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo debe contener letras")
    @NotBlank
    @Size(min = 3, max = 15, message = "El nombre debe tener entre 3 y 15 caracteres")
    @Schema(description = "Nombre del paciente", example = "Juan Carlos", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El apellido solo debe contener letras")
    @NotBlank
    @Size(min = 3, max = 15, message = "El apellido debe tener entre 3 y 15 caracteres")
    @Schema(description = "Apellido del paciente", example = "Perez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Past
    private LocalDate birthDate;

    @Pattern(regexp = "^[1-9]d{7}$", message = "El DNI solo debe tener números y maximo 8 caracteres")
    @Size(min =8, max=8, message = "El dni debe 8 caracteres")
    @NotBlank(message = "El DNI es obligatorio")
    @Schema(description = "DNI del paciente", requiredMode = Schema.RequiredMode.REQUIRED, example = "40650777")
    private String dni;
}
