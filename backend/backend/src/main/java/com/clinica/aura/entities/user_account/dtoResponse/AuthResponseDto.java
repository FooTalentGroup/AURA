package com.clinica.aura.entities.user_account.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private Long id;
    private String email;
    private String message;
    private String token;
    private boolean success;
}
