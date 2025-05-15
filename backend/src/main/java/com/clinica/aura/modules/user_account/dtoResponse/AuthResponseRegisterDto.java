package com.clinica.aura.modules.user_account.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseRegisterDto {
    private Long userId;
    private String username;
    private String message;
    private String token;
    private boolean status;
}
