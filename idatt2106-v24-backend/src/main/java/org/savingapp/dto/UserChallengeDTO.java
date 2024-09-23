package org.savingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.savingapp.model.Challenge;

import java.time.LocalDate;


/**
 * Represents a user challenge data transfer object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChallengeDTO {
    private Long id;
    private Double goalAmount;
    private Double savedAmount;
    private LocalDate fromDate;
    private LocalDate toDate;
    private ChallengeDTO challenge;
}
