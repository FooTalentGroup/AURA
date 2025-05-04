package com.clinica.aura.models.user_account.dtoRequest;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthCreateRoleRequestDto {
    @Size(max = 1, message = "roles must be a maximum of 1 character in length" )
    private List<String> roles;
}
