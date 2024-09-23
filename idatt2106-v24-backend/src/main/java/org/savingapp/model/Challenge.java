package org.savingapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.savingapp.enums.Category;
import org.savingapp.enums.LifeSituation;


/**
 * Represents a challenge.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private Category category;

    @Column(name = "life_situation")
    private LifeSituation lifeSituation;

    @Column(name = "duration")
    private int duration;

    @Column(name = "sum")
    private int sum;

    @Column(name = "average_amount")
    private double averageAmount;
}
