package com.clinica.aura.entities.user_account.repository;

import com.clinica.aura.entities.user_account.models.PermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionModel, Long> {
    Optional<PermissionModel> findByName(String create);
}
