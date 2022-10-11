package ru.yandex.diplom_2.order;
import io.restassured.response.ValidatableResponse;
import ru.yandex.diplom_2.config.Config;
import java.util.HashMap;


public class OrderClient extends Config {

    public static final String ORDER = "api/orders";
    public static final String INGREDIENTS = "api/ingredients";

    public ValidatableResponse getIngredients() {
        return getSpec()
                .when()
                .get(INGREDIENTS)
                .then().log().all();
    }

    public ValidatableResponse createOrder(HashMap ingredients, String token) {
        return getSpec()
                .header("Authorization", token)
                .body(ingredients)
                .when()
                .post(ORDER)
                .then().log().all();
    }


    public ValidatableResponse getOrderWithAuth(String token) {
        return getSpec()
                .header("Authorization", token)
                .when()
                .get(ORDER)
                .then().log().all();
    }

    public ValidatableResponse getOrderWithoutAuth() {
        return getSpec()
                .when()
                .get(ORDER)
                .then().log().all();
    }

}
