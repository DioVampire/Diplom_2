package ru.yandex.diplom_2.order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.diplom_2.config.Config;
import java.util.HashMap;


public class OrderClient extends Config {

    public static final String ORDER = "api/orders";
    public static final String INGREDIENTS = "api/ingredients";

    @Step("Get ingredients for burger")
    public ValidatableResponse getIngredients() {
        return getSpec()
                .when()
                .get(INGREDIENTS)
                .then().log().all();
    }
    @Step("Create order")
    public ValidatableResponse createOrder(HashMap ingredients, String token) {
        return getSpec()
                .header("Authorization", token)
                .body(ingredients)
                .when()
                .post(ORDER)
                .then().log().all();
    }

    @Step("Get orders for an authenticated user")
    public ValidatableResponse getOrderWithAuth(String token) {
        return getSpec()
                .header("Authorization", token)
                .when()
                .get(ORDER)
                .then().log().all();
    }
    @Step("Get orders for an unauthenticated user")
    public ValidatableResponse getOrderWithoutAuth() {
        return getSpec()
                .when()
                .get(ORDER)
                .then().log().all();
    }

}
