package com.clinica.aura.modules.user_account.dtoResponse;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeResponseDto {
    private Long id;
    private String email;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String dni;
    private List<String> roles;
}
