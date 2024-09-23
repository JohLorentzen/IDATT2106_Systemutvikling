package org.savingapp.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents user statistics.
 * Used to calculate the user's total savings and completed challenges and goals.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "total_saved")
    private double totalSaved;

    @Column(nullable = false, name = "completed_challenges")
    private int completedChallenges;

    @Column(nullable = false, name = "completed_goals")
    private int completedGoals;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
}
