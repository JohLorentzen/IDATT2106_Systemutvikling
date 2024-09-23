package org.savingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.savingapp.enums.Category;
import org.savingapp.enums.InterestEnum;
import org.savingapp.enums.LifeSituation;
import org.savingapp.enums.SkillLevel;
import org.savingapp.model.UserCategoryStats;

import java.util.List;


/**
 * Represents a user's user insight, as a data transfer object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInsightDTO {
    private Long id;
    private SkillLevel skillLevel;
    private LifeSituation lifeSituation;
    private List<InterestEnum> interests;
    private List<Category> categories;
    private Long userId;
}
