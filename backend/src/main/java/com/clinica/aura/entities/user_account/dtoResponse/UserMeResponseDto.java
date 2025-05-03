package com.clinica.aura.entities.user_account.dtoResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeResponseDto {
    private Long id;
    private String email;
    private List<String> roles;
}
