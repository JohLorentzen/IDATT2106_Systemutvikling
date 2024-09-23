package org.savingapp.enums;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Enum for the type of category, used for transactions.
 */
@AllArgsConstructor
public enum Category implements Localizable {
    GROCERIES("Groceries", "Dagligvarer",
            List.of("Kjøp i bulk",
                    "Kjøp butikkens merker",
                    "Bruk kuponger",
                    "Planlegg måltidene dine",
                    "Kjøp sesongbaserte produkter",
                    "Sjekk tilbudsaviser",
                    "Sjekk kjøleskapet før du handler",
                    "Unngå å handle sulten",
                    "Bruk handleliste",
                    "Unngå å kaste mat"),
            List.of("Spar på supermarkedet",
                    "Handleheltene",
                    "Matmester-mønsteret",
                    "Prispiratene",
                    "Sparer i spisekammeret")),

    TRANSPORTATION("Transportation", "Transport",
            List.of("Bruk offentlig transport",
                    "Samkjør med kollegaer",
                    "Vedlikehold kjøretøyet ditt",
                    "Sykle eller gå når det er mulig",
                    "Bysykkel er billigere enn Ryde"),
            List.of("Kjør, gå eller spar",
                    "Penger på reise",
                    "Bil og beina begge brukes",
                    "Veien er lang, sparepengene er nær",
                    "Ferden til framskritt")),

    ENTERTAINMENT("Entertainment", "Underholdning",
            List.of("Bruk gratis streamingtjenester",
                    "Besøk lokale parker",
                    "Se etter rabatterte billetter til arrangementer",
                    "Del abonnementer med venner",
                    "Gå på gratis arrangementer",
                    "Sjekk hvilke strømme-tjenester som har gratis prøveperiode",
                    "Lån bøker på biblioteket i stedet for å kjøpe",
                    "Selg pent brukte bøker og spill",
                    "Byttelån spill og bøker med venner"),
            List.of("Fornøyelses-finansene",
                    "Sparing i scenelyset",
                    "Budsjett-biografien",
                    "Penger på premieren",
                    "Ballett av besparelser")),

    FOOD_AND_DRINKS("Food and Drinks", "Mat og Drikke",
            List.of("Spis ute sjeldnere",
                    "Se etter spesialtilbud under happy hour",
                    "Ta med lunsj til jobb",
                    "Unngå drinker på byen", "Lag mat hjemme",
                    "Drikk vann i stedet for brus",
                    "Lag kaffe hjemme og ta med i termos"),
            List.of("Smak for sparing",
                    "Drikke og drakter med diskonteringer",
                    "Budsjett-beruselsen",
                    "Pris-per-porsjon-pakten",
                    "Lønnsomme lunsjer")),

    PERSONAL_CARE("Personal Care", "Personlig Pleie",
            List.of("Bruk hjemmelagde midler",
                    "Kjøp skjønnhetsprodukter på salg",
                    "Begrens besøk hos frisøren"),
            List.of("Skjønnhetsbesparelsene",
                    "Velvære-verdivarpen",
                    "Egoistisk, men økonomisk",
                    "Penger på pleie",
                    "Frisk frugalitet")),

    NOT_CATEGORIZED("Not Categorized", "Ikke Kategorisert",
            List.of("Bruk mindre penger",
                    "Spar penger",
                    "Ikke bruk penger",
                    "Ikke bruk penger på noe",
                    "Ikke bruk penger på noe unødvendig",
                    "Ikke bruk penger på noe unødvendig i det hele tatt"),
            List.of("Penger på plass",
                    "Uklassifisert, men uovervinnelig",
                    "Budsjettbalansen",
                    "Kroner uten kategori",
                    "Sparingen uten kategori")),

    HOME("Home", "Hjem",
            List.of("Gjør små-reparasjoner selv",
                    "Reduser bruken av strøm og vann",
                    "Kjøp energieffektive apparater",
                    "Dusj på treningsstudioet",
                    "Dusj raskt",
                    "Slå av lysene når du forlater rommet",
                    "Bruk sparepærer"),
            List.of("Hjemmeholdningshjelpen",
                    "Boligbudsjettert",
                    "Hjemlige husholdningshelter",
                    "Husets hemmelige sparingsknep",
                    "Boligsparingens beist")),

    LOANS("Loans", "Lån",
            List.of("Refinansier lån med høy rente",
                    "Betal ekstra avdrag",
                    "Konsolider gjeld",
                    "Ring banken for å forhandle renten"),
            List.of("Rente-rivaliseringen",
                    "Kredittkampen",
                    "Lånekuppet",
                    "Budsjettet, lånt og lurt",
                    "Gjeldens grøssere")),

    VACATION("Vacation", "Ferie",
            List.of("Reis utenom sesong",
                    "Se etter pakketilbud",
                    "Bo på budsjettvennlige steder",
                    "Sjekk restplasser",
                    "Reis med venner for å dele kostnadene",
                    "Bruk Airbnb i stedet for hotell"),
            List.of("Ferie for fåtallet",
                    "Budsjettert badeferie",
                    "Reiselyst uten ruinene",
                    "Feriefornuftig ferdsel",
                    "Spar på solsengen")),

    CLOTHING("Clothing", "Klær",
            List.of("Kjøp utenfor sesong",
                    "Bruk salg",
                    "Bytt klær med venner"),
            List.of("Kle deg klokt",
                    "Motemysteriene løst",
                    "Kleskapets kupp",
                    "Stil på sparebluss",
                    "Frugalitetsfashionista")),

    SHOPPING("Shopping", "Shopping",
            List.of("Begrens impulskjøp",
                    "Bruk pris sammenligningstjenester",
                    "Sett et shoppingbudsjett",
                    "Sjekk finn.no først",
                    "Sjekk Tise først"),
            List.of("Butikkens besparelsesbonanza",
                    "Handlelykken",
                    "Kjøpesenter-kampen",
                    "Prisprestasjonene",
                    "Sparing i salgssirkuset")),

    INSURANCE("Insurance", "Forsikring",
            List.of("Kombiner forsikringspoliser",
                    "Øk egenandelene dine",
                    "Sammenlign forsikringstilbydere",
                    "Sjekk om du kan få rabatt for å betale årlig",
                    "Vurder å øke egenandelen"),
            List.of("Forsikringsfornuft",
                    "Premieprutingen",
                    "Sparing på sikkerhet",
                    "Besparelser på bekymringene",
                    "Forsikringens finesser")),

    INVESTMENTS("Investments", "Investeringer",
            List.of("Invester i lavkostnads indeksfond",
                    "Diversifiser porteføljen din",
                    "Overvåk investeringene regelmessig"),
            List.of("Investeringsintelligensen",
                    "Aksjemarkedets arkitekter",
                    "Investeringsinspirasjonen",
                    "Sparingens strateger",
                    "Investeringens innsats")),

    KIDS("Kids", "Barn",
            List.of("Kjøp brukte leker",
                    "Bruk offentlige skoler",
                    "Delta i gratis aktiviteter for barn"),
            List.of("Barnebudsjettert",
                    "Sparingen for smårollinger",
                    "Barnebudsjett-opplæringen",
                    "Lavpris-lykke for småtroll",
                    "Besparelser for barneflokken")),

    PETS("Pets", "Kjæledyr",
            List.of("Lag kjæledyrfôr hjemme",
                    "Stell kjæledyrene dine selv",
                    "Hold vaksinasjonene oppdatert for å unngå sykdommer",
                    "Ring forsikringsselskap for et tilbund"),
            List.of("Pengepelsene",
                    "Dyre detaljer",
                    "Budsjett for bjeffere",
                    "Besparelser for bikkja",
                    "Kattens kostnadskontroll")
    );

    private final String englishText;
    private final String norwegianText;

    private final List<String> savingTips;
    private final List<String> challengeTitles;

    @Override
    public String getEnglishText() {
        return englishText;
    }

    @Override
    public String getNorwegianText() {
        return norwegianText;
    }


    /**
     * Returns a random saving tip from the list of saving tips
     *
     * @return a random saving tip
     */
    public String getSavingTip() {
        return savingTips.get((int) (Math.random() * savingTips.size()));
    }


    /**
     * Returns a random challenge title from the list of challenge titles.
     *
     * @return A random challenge title.
     */
    public String getChallengeTitle() {
        return challengeTitles.get((int) (Math.random() * challengeTitles.size()));
    }
}