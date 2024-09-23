package org.savingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.savingapp.enums.Category;


/**
 * Represents a challenge skeleton, used for creating a challenge.
 */
@Data
@AllArgsConstructor
public class ChallengeSkeleton {

    private double efficiency;

    private Category category;

    private int duration;

    private int sum;
}
