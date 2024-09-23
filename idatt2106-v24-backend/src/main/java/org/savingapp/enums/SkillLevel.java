package org.savingapp.enums;

/**
 * Enum for the skill level of the user.
 */
public enum SkillLevel implements Localizable {
    BEGINNER("Beginner", "Begynner"),
    INTERMEDIATE("Intermediate", "viderekommen"),
    AVERAGE("Average", "Gjennomsnitt"),
    ADVANCED("Advanced", "Avansert"),
    EXPERT("Expert", "Ekspert");

    private final String englishText;
    private final String norwegianText;

    SkillLevel(String englishText, String norwegianText) {
        this.englishText = englishText;
        this.norwegianText = norwegianText;
    }

    @Override
    public String getEnglishText() {
        return englishText;
    }

    @Override
    public String getNorwegianText() {
        return norwegianText;
    }
}
