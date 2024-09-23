package org.savingapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.savingapp.enums.Category;
import org.savingapp.enums.InterestEnum;
import org.savingapp.enums.LifeSituation;
import org.savingapp.enums.SkillLevel;

import java.util.List;


/**
 * Represents a user insight, which is a collection of user preferences and statistics.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "skill_level")
    private SkillLevel skillLevel;

    @Column(name = "life_situation")
    @Enumerated(EnumType.STRING)
    private LifeSituation lifeSituation;

    @Column(name = "interests")
    @ElementCollection(targetClass = InterestEnum.class)
    private List<InterestEnum> interests;

    @Column(name = "categories")
    @ElementCollection(targetClass = Category.class)
    private List<Category> categories;

    @ToString.Exclude
    @OneToMany(mappedBy = "userInsight", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserCategoryStats> categoryStats;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Constructs a new user insight.
     *
     * @param id            the id of the user insight
     * @param skillLevel    the skill level of the user
     * @param lifeSituation the life situation of the user
     * @param interests     the interests of the user
     * @param categories    the categories of the user
     * @param user          the user
     */
    public UserInsight(Long id, SkillLevel skillLevel, LifeSituation lifeSituation,
                       List<InterestEnum> interests, List<Category> categories, User user) {
        this.id = id;
        this.skillLevel = skillLevel;
        this.lifeSituation = lifeSituation;
        this.interests = interests;
        this.categories = categories;
        this.user = user;
    }

    /**
     * Sets the category statistics of the user insight.
     *
     * @param userCategoryStats the category statistics
     */
    public void setCategoryStats(List<UserCategoryStats> userCategoryStats) {
        if (this.categoryStats == null) {
            this.categoryStats = userCategoryStats;
            return;
        }

        if (userCategoryStats != null) {
            this.categoryStats.clear();
            for (UserCategoryStats stats : userCategoryStats) {
                stats.setUserInsight(this);
            }
            this.categoryStats.addAll(userCategoryStats);
        }
    }

    /**
     * Calculates the base efficiency score of the user insight.
     *
     * @return the base efficiency score
     */
    public float calculateBaseEfficiencyScore() {
        int score = switch (getSkillLevel()) {
            case BEGINNER -> 12;
            case INTERMEDIATE -> 15;
            case AVERAGE -> 17;
            case ADVANCED -> 20;
            case EXPERT -> 25;
        };
        switch (getLifeSituation()) {
            case UNEMPLOYED -> score *= 0.5;
            case STUDENT -> score *= 0.7;
            case WORKING -> score *= 1;
        }

        return score / 100f;
    }
}
