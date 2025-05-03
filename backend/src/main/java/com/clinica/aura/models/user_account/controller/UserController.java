package com.clinica.aura.models.user_account.controller;

import com.clinica.aura.models.user_account.dtoResponse.UserResponseDto;
import com.clinica.aura.models.user_account.service.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Controlador de usuarios")
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userDetailsService.getUserById(id));
    }
}
