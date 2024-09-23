package org.savingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.savingapp.model.SavingsPath;
import org.savingapp.model.UserChallenge;


/**
 * Represents a completed user challenge data transfer object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedUserChallengeDTO {
    private Long id;
    private UserChallengeDTO userChallenge;
    private Double pointOfCompletion;
}
