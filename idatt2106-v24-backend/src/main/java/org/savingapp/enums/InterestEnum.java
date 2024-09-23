package org.savingapp.enums;


/**
 * Enum for the interests of the user.
 */
public enum InterestEnum implements Localizable {
    STOCKS("Stocks", "Aksjer"),
    BONDS("Bonds", "Obligasjoner"),
    REAL_ESTATE("Real Estate", "Eiendom"),
    CRYPTOCURRENCY("Cryptocurrency", "Kryptovaluta"),
    PERSONAL_FINANCE("Personal Finance", "Personlig Finans"),
    RETIREMENT("Retirement", "Pensjon"),
    INVESTING("Investing", "Investering"),
    DEBT("Debt", "Gjeld");

    private final String englishText;
    private final String norwegianText;

    InterestEnum(String englishText, String norwegianText) {
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