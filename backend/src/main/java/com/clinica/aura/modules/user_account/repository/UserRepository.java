package com.clinica.aura.modules.user_account.repository;

import com.clinica.aura.modules.person.model.PersonModel;
import com.clinica.aura.modules.user_account.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findByPerson(PersonModel person);
    Optional<UserModel> findByPersonId(Long personId);

    List<UserModel> findAllBySuspensionEndBefore(LocalDateTime now);
}
