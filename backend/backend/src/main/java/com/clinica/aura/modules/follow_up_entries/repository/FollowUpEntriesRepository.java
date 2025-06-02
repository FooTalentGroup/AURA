package com.clinica.aura.modules.follow_up_entries.repository;

import com.clinica.aura.modules.follow_up_entries.model.FollowUpEntriesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowUpEntriesRepository extends JpaRepository<FollowUpEntriesModel,Long> {
}
