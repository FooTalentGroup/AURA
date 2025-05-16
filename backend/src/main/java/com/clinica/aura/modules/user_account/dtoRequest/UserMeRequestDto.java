package com.clinica.aura.modules.user_account.dtoRequest;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeRequestDto {
    private String email;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String dni;
    private List<String> roles;
}
