package com.clinica.aura.modules.user_account.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String name;
    private String lastName;
    private String dni;
    private String phone;
    private String email;
    private List<String> roles;
}
