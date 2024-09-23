package org.savingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.savingapp.enums.Category;
import org.savingapp.enums.LifeSituation;

/**
 * Represents a challenge data transfer object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeDTO {
    private Long id;
    private String name;
    private String description;
    private Category category;
    private Integer duration;
    private Integer sum;
    private Double averageAmount;
    private LifeSituation lifeSituation;

    public String getTip() {
        return category.getSavingTip();
    }
}
