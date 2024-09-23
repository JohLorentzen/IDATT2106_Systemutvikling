package org.savingapp.repository;

import org.savingapp.model.CompletedUserChallenge;
import org.savingapp.model.SavingsPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


/**
 * Repository for the CompletedUserChallenge entity.
 */
public interface CompletedUserChallengeRepository extends JpaRepository<CompletedUserChallenge, Long> {
    List<CompletedUserChallenge> findBySavingsPath(SavingsPath savingsPath);

    Optional<CompletedUserChallenge> findByUserChallengeId(Long userChallengeId);
}
