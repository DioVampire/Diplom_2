package ru.yandex.diplom_2.order;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.diplom_2.user.User;
import ru.yandex.diplom_2.user.UserClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateOrderWithValidDataTests {

    private static UserClient userClient;
    String token;

    HashMap<String, List> orderHash;
    List<String> ingredients = new ArrayList<>();
    OrderClient orderClient;
    private int expectedStatus = 200;
    private boolean successExpected = true;

    //Создаем пользователя и генерируем список ингредиентов
    @Before
    public void setup(){
        User user = User.getRandomUser();
        userClient = new UserClient();
        token = userClient.create(user)
                .extract()
                .path("accessToken");

        orderHash = new HashMap<>();
        orderClient = new OrderClient();
        List <String> ingredientsIds = orderClient.getIngredients().extract().path("data._id");
        for (int i = 1; i <= 4; i = i + 1) {
            ingredients.add(ingredientsIds.get(i + 4));
        }
        orderHash.put("ingredients", ingredientsIds);
    }

    // Удаление пользователя
    @After
    public void delete() {
        userClient.delete(token);
    }

    @Test
    public void createOrderWithIngredientsAuthTest() {
        // Делаем заказ с авторизацией раннее созданного пользователя и списка ингредиентов для него
        ValidatableResponse orderCreated = orderClient.createOrder(orderHash, token);
        // Берем статус создания заказа
        int actualStatus = orderCreated.extract().statusCode();
        // Берем тело сообщения об успешности создания заказа
        boolean isSuccessful = orderCreated.extract().path("success");
        // Проверка, что статус корректный
        assertEquals(expectedStatus, actualStatus);
        // Проверка, что создание заказа прошло успешно
        assertEquals(successExpected, isSuccessful);
    }

    @Test
    public void createOrderWithIngredientsWithoutAuthTest() {
        // Делаем заказ без авторизации, но с раннее созданным списком ингредиентов
        ValidatableResponse orderCreated = orderClient.createOrder(orderHash, "");
        // Берем статус создания заказа
        int actualStatus = orderCreated.extract().statusCode();
        // Берем тело сообщения об успешности создания заказа
        boolean isSuccessful = orderCreated.extract().path("success");
        // Проверка, что статус корректный
        assertEquals(expectedStatus, actualStatus);
        // Проверка, что создание заказа прошло успешно
        assertEquals(successExpected, isSuccessful);
    }


}
