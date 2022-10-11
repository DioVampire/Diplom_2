package ru.yandex.diplom_2.config;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Config {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    public static RequestSpecification getSpec() {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL);
    }
}
