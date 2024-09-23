package org.savingapp.repository;

import org.savingapp.model.User;
import org.savingapp.model.UserInsight;
import org.savingapp.dto.UserInsightDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Repository for the UserInsight entity.
 */
public interface UserInsightRepository extends JpaRepository<UserInsight, Long> {
    Optional<UserInsightDTO> findByUser(User user);
}
