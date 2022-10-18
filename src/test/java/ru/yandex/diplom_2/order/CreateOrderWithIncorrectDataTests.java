package ru.yandex.diplom_2.order;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.diplom_2.user.User;
import ru.yandex.diplom_2.user.UserClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateOrderWithIncorrectDataTests {

    private static UserClient userClient;
    HashMap<String, List> orderHash;
    List<String> ingredients = new ArrayList<>();
    OrderClient orderClient;
    String token;

    //Создаем пользователя и генерируем список ингредиентов
    @Before
    public void setup(){
        User user = User.getRandomUser();
        userClient = new UserClient();
        token = userClient.createUser(user)
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

    @Test
    public void createOrderWithoutIngredientsTest(){
        // Делаем список без ингредиентов
        HashMap<String, List> nullOrderHash = new HashMap<>();
        // Делаем заказ с авторизацией раннее созданного клиента, но с пустым списком ингредиентов
        ValidatableResponse orderCreated = orderClient.createOrder(nullOrderHash, token);
        // Берем статус создания заказа
        int actualStatus = orderCreated.extract().statusCode();
        // Берем тело сообщения об успешности создания заказа
        String actualMessage = orderCreated.extract().path("message");
        // Проверка, что статус корректный
        assertEquals("Status is not 400",400, actualStatus);
        // Проверка, что создание заказа без ингредиентов невозможно
        assertEquals("Ingredient ids must be provided", actualMessage);
    }

    @Test
    public void createOrderWithIncorrectHashTest(){
        // Делаем список из рандомных айди ингредиентов
        String invalidHashIngredients = RandomStringUtils.randomAlphanumeric(5,15);
        List<String> invalidIngredients = new ArrayList<>();
        HashMap<String, List> invalidOrderHash = new HashMap<>();
        invalidIngredients.add(invalidHashIngredients);
        invalidOrderHash.put("ingredients", invalidIngredients);

        // Делаем заказ с авторизацией раннее созданного клиента, но неккоректным списком айдишников
        ValidatableResponse orderCreated = orderClient.createOrder(invalidOrderHash, token);
        // Берем статус создания заказа
        int actualStatus = orderCreated.extract().statusCode();
        // Проверка, что создание заказа с рандомными ингридиентами невозможно
        assertEquals(500, actualStatus);
    }

}
