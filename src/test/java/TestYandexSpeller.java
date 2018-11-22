import beans.YandexSpellerAnswer;
import core.YandexSpellerApi;
import org.testng.annotations.Test;

import java.util.List;

import static core.YandexSpellerConstants.ErrorCode.ERROR_CAPITALIZATION;
import static core.YandexSpellerConstants.ErrorCode.ERROR_REPEAT_WORD;
import static core.YandexSpellerConstants.ErrorCode.ERROR_UNKNOWN_WORD;
import static core.YandexSpellerConstants.Language.UK;
import static core.YandexSpellerConstants.Options.IGNORE_DIGITS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestYandexSpeller {

    @Test
    public void wordsWithErrorsTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .texts("папаа", "мамаа")
                .callApi());
        answers.forEach(x -> assertFalse(x.isEmpty(), "Answer is empty"));
        assertEquals(answers.get(0).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(0).get(0).s.get(0), "папа");
        assertEquals(answers.get(1).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(1).get(0).s.get(0), "мама");
    }

    @Test
    public void repeatedWordsTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .texts("я", "еду", "на", "на")
                .callApi());
        assertFalse(answers.get(2).isEmpty(), "Answer is empty");
        assertEquals(answers.get(2).get(0).code, ERROR_REPEAT_WORD.code);
        assertEquals(answers.get(2).get(0).word, "на");
    }

    @Test
    public void contextTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .texts("скучать", "музыку")
                .callApi());
        assertFalse(answers.get(0).isEmpty(), "Answer is empty");
        assertEquals(answers.get(0).get(0).word, "скучать");
        assertTrue(answers.get(0).get(0).s.contains("скачать"));
    }

    @Test
    public void contextMisspellingTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .texts("скучатьь", "музыку")
                .callApi());
        assertFalse(answers.get(0).isEmpty(), "Answer is empty");
        assertEquals(answers.get(0).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(0).get(0).word, "скучатьь");
        assertTrue(answers.get(0).get(0).s.contains("скачать"));
    }

    @Test
    public void urlTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .texts("www.google.com", "123@gmail.com")
                .callApi());
        answers.forEach(x -> assertFalse(x.isEmpty(), "Answer is empty"));
        assertEquals(answers.get(0).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(0).get(0).word, "www.google.com");
        assertEquals(answers.get(1).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(1).get(0).word, "123@gmail.com");
    }

    @Test
    public void ignoreDigitsTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .options(IGNORE_DIGITS)
                .texts("слушаю", "музыку123")
                .callApi());
        assertTrue(answers.get(0).isEmpty());
    }

    @Test
    public void digitsTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .texts("123слушаю", "музыку123", "сего2дня")
                .callApi());
        answers.forEach(x -> assertFalse(x.isEmpty(), "Answer is empty"));
        assertEquals(answers.get(0).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(0).get(0).s.get(0), "123 слушаю");
        assertEquals(answers.get(1).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(1).get(0).s.get(0), "музыку 123");
        assertEquals(answers.get(2).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(2).get(0).s.get(0), "сегодня");
    }

    @Test
    public void upperCaseTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .texts("япония", "moScoW")
                .callApi());
        answers.forEach(x -> assertFalse(x.isEmpty(), "Answer is empty"));
        assertEquals(answers.get(0).get(0).code, ERROR_CAPITALIZATION.code);
        assertEquals(answers.get(0).get(0).s.get(0), "Япония");
        assertEquals(answers.get(1).get(0).code, ERROR_CAPITALIZATION.code);
        assertEquals(answers.get(1).get(0).s.get(0), "Moscow");
    }

    @Test
    public void emptyRequestTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .texts()
                .callApi());
        assertTrue(answers.get(0).isEmpty());
    }

    @Test
    public void ukLanguageTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .language(UK)
                .texts("ничого", "немае")
                .callApi());
        answers.forEach(x -> assertFalse(x.isEmpty(), "Answer is empty"));
        assertEquals(answers.get(0).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(0).get(0).s.get(0), "нічого");
        assertEquals(answers.get(1).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(1).get(0).s.get(0), "немає");
    }

    @Test
    public void defaultLanguageTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .texts("motherr", "мамаа")
                .callApi());
        answers.forEach(x -> assertFalse(x.isEmpty(), "Answer is empty"));
        assertEquals(answers.get(0).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(0).get(0).s.get(0), "mother");
        assertEquals(answers.get(1).get(0).code, ERROR_UNKNOWN_WORD.code);
        assertEquals(answers.get(1).get(0).s.get(0), "мама");
    }

    @Test
    public void answerSizeTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(YandexSpellerApi.with()
                .texts("мамаа", "папаа", "motherr", "fatherr")
                .callApi());
        assertThat(answers, hasSize(4));
        assertThat(answers, not(hasItem(empty())));
    }

}
