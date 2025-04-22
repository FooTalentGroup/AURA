package com.clinica.aura.entities.user_account.repository;

import com.clinica.aura.entities.user_account.Enum.EnumRole;
import com.clinica.aura.entities.user_account.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    List<RoleModel> findRoleEntitiesByEnumRoleIn(List<String> roles);
    Optional<RoleModel> findByEnumRole(EnumRole role);



}
