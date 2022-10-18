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

public class GetOrdersForAuthorizedUserTest {


    UserClient userClient;
    String token;

    HashMap<String, List> orderHash;
    List<String> ingredients = new ArrayList<>();
    OrderClient orderClient;
    private boolean orderSuccess = true;
    private int statusExpected = 200;

    // Создаем пользователя и заказ за него
    @Before
    public void setup(){
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

    // Удаляем пользователя
    @After
    public void delete() {
            userClient.deleteUser(token);
    }

    @Test
    public void getOrdersAuthUserTest(){
        //Получаем раннее созданный заказ под авторизацией пользователя
        ValidatableResponse authOrder = orderClient.getOrderWithAuth(token);
        //Получаем статус ответа
        int actualStatus = authOrder.extract().statusCode();
        //Получаем тело ответа
        boolean isSuccessful = authOrder.extract().path("success");
        //Проверка, что статус корректный
        assertEquals(statusExpected, actualStatus);
        //Проверка, что список получен успешно
        assertEquals(orderSuccess, isSuccessful);
    }

}
