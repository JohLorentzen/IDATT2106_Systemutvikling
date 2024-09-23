package org.savingapp.repository;

import org.savingapp.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository for the Challenge entity.
 */
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
