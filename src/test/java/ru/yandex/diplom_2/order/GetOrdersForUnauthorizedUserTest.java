package ru.yandex.diplom_2.order;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.diplom_2.user.User;
import ru.yandex.diplom_2.user.UserClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GetOrdersForUnauthorizedUserTest {

    UserClient userClient;
    String token;
    HashMap<String, List> orderHash;
    List<String> ingredients = new ArrayList<>();
    OrderClient orderClient;
    private String expectedMessage = "You should be authorised";
    private int expectedStatus = 401;

    // Создаем пользователя и заказ за него
    @Before
    public void before(){
        User user = User.getRandomUser();
        userClient = new UserClient();
        token = userClient.createUser(user)
                .extract()
                .path("accessToken");

        orderHash = new HashMap<>();
        orderClient = new OrderClient();
        List <String> IngredientsIds = orderClient.getIngredients().extract().path("data._id");

        ingredients.add(IngredientsIds.get(1));
        ingredients.add(IngredientsIds.get(2));
        ingredients.add(IngredientsIds.get(3));
        orderHash.put("ingredients", IngredientsIds);
        orderClient.createOrder(orderHash, token);
    }

    @Test
    public void getOrdersWithNoAuthUserTest() {
        //Получаем раннее созданный заказ без авторизации
        ValidatableResponse noAuthOrder = orderClient.getOrderWithoutAuth();
        //Получаем статус ответа
        int actualStatus = noAuthOrder.extract().statusCode();
        //Получаем тело ответа
        String actualMessage = noAuthOrder.extract().path("message");
        //Проверка, что статус корректный
        assertEquals(expectedStatus, actualStatus);
        //Проверка, что список нельзя получить без авторизации
        assertEquals(expectedMessage, actualMessage);
    }

}
