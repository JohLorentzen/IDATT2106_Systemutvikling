package org.savingapp.repository;

import org.savingapp.model.User;
import org.savingapp.model.UserChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Repository for the UserChallenge entity.
 */
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    Page<UserChallenge> findByUser(Pageable pageable, User user);

    List<UserChallenge> findByUserId(Long userId);
}
