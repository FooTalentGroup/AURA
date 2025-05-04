package com.clinica.aura.models.user_account.service.impl;

import com.clinica.aura.models.user_account.models.UserModel;
import com.clinica.aura.models.user_account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class UserSchedulerService {
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void checkExpiredSuspensions() {
        List<UserModel> suspendedUsers = userRepository.findAllBySuspensionEndBefore(LocalDateTime.now());

        if (suspendedUsers.isEmpty()) {
            log.info("No hay usuarios para reactivar.");
            return;
        }

        suspendedUsers.forEach(user -> {
            try {
                user.setSuspensionEnd(null);
                userRepository.save(user);
                log.info("Usuario {} reactivado automáticamente", user.getEmail());
            } catch (Exception e) {
                log.error("Error al reactivar al usuario {}: {}", user.getEmail(), e.getMessage());
            }
        });
    }
}
