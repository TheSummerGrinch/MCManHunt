package io.github.thesummergrinch.mcmanhunt.io.lang;

public enum SupportedLanguages {

    EN_GB("_en_GB.properties"),
    EN_US("_en_US.properties"),
    NL_NL("_nl_NL.properties"),
    DEFAULT(".properties");

    private final String correspondingFileSuffix;

    SupportedLanguages(final String correspondingFileSuffix) {

        this.correspondingFileSuffix = correspondingFileSuffix;

    }

    public String getCorrespondingFileSuffix() {

        return correspondingFileSuffix;

    }

    public SupportedLanguages fromString(final String supportedLanguagesString) {

        switch (supportedLanguagesString) {

            case "enGB":
                return EN_GB;

            case "enUS":
                return EN_US;

            case "nlNL":
                return NL_NL;

            default:
                return DEFAULT;

        }
    }
}
