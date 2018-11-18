package core;

import beans.YandexSpellerAnswer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static core.YandexSpellerConstants.*;

public class YandexSpellerApi {
    private YandexSpellerApi() {
    }

    private HashMap<String, String> params = new HashMap<>();
    private List<String> texts = new ArrayList<>();

    public static class ApiBuilder {

        YandexSpellerApi yandexSpellerApi;

        private ApiBuilder(YandexSpellerApi yandexSpellerApi) {
            this.yandexSpellerApi = yandexSpellerApi;
        }

        public ApiBuilder texts(String... texts) {
            Collections.addAll(yandexSpellerApi.texts, texts);
            return this;
        }

        public ApiBuilder options(Options... options) {
            int codeSum = 0;
            for (Options option : options) {
                codeSum += option.code;
            }
            yandexSpellerApi.params.put(PARAM_OPTIONS, String.valueOf(codeSum));
            return this;
        }

        public ApiBuilder language(Language language) {
            yandexSpellerApi.params.put(PARAM_LANG, language.languageCode);
            return this;
        }

        public Response callApi() {
            return RestAssured.with()
                    .queryParams(yandexSpellerApi.params)
                    .queryParam(PARAM_TEXT, yandexSpellerApi.texts)
                    .log().all()
                    .get(YANDEX_SPELLER_API_URI).prettyPeek();
        }

    }

    public static List<List<YandexSpellerAnswer>> getYandexSpellerAnswers(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<List<List<YandexSpellerAnswer>>>() {
        }.getType());
    }

    public static ApiBuilder with() {
        YandexSpellerApi yandexSpellerApi = new YandexSpellerApi();
        return new ApiBuilder(yandexSpellerApi);
    }

}
