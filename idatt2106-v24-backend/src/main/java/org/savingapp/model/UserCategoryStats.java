package org.savingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.savingapp.enums.Category;


/**
 * Represents user category statistics
 * Used to calculate the relevance and efficiency scores of a user's insights.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class UserCategoryStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private double trend;

    @Column(nullable = false)
    private float relevanceScore;

    @Column(nullable = false)
    private float efficiencyScoreOffset;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_insight_id")
    private UserInsight userInsight;
}
