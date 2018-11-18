package core;

public class YandexSpellerConstants {

    public static final String YANDEX_SPELLER_API_URI = "https://speller.yandex.net/services/spellservice.json/checkTexts";
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_OPTIONS = "options";
    public static final String PARAM_LANG = "lang";

    public enum Language {
        RU("ru"),
        EN("en"),
        UK("uk");

        public String languageCode;

        Language(String languageCode) {
            this.languageCode = languageCode;
        }
    }

    public enum Options {
        IGNORE_DIGITS(2),
        IGNORE_URLS(4),
        FIND_REPEAT_WORDS(8),
        IGNORE_CAPITALIZATION(512);

        public int code;

        Options(int code) {
            this.code = code;
        }
    }

    public enum ErrorCode {
        ERROR_UNKNOWN_WORD(1),
        ERROR_REPEAT_WORD(2),
        ERROR_CAPITALIZATION(3),
        ERROR_TOO_MANY_ERRORS(4);

        public Integer code;

        ErrorCode(Integer code) {
            this.code = code;
        }
    }
}
