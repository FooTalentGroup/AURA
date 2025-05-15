package com.clinica.aura.modules.user_account.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginRequestDto {
    @NotBlank(message = "El email es obligatorio")
    @Schema(description = "Correo del usuario", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin@example.com")
    @Email(message = "El correo electrónico no es válido")
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario",requiredMode = Schema.RequiredMode.REQUIRED, example = "admin123")
    private String password;
}
