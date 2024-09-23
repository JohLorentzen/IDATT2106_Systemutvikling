package org.savingapp.enums;

/**
 * Enum for the life situation of the user.
 */
public enum LifeSituation implements Localizable {
    STUDENT("Student", "Student"),
    WORKING("Working", "Arbeider"),
    UNEMPLOYED("Unemployed", "Arbeidsledig");

    private final String englishText;
    private final String norwegianText;

    LifeSituation(String englishText, String norwegianText) {
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