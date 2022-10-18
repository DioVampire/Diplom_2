package ru.yandex.diplom_2.user;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class RegisterTheSameUserTest {

    private User user;
    private UserClient userClient;
    private int expectedCode;
    private String expectedMessage;
    String token;

    // Создание пользователя
    @Before
    public void setUp() {
        user = User.getRandomUser();
        userClient = new UserClient();
        expectedCode = 403;
        expectedMessage = "User already exists";
    }

    // Удаление созданного пользователя
    @After
    public void delete(){
        userClient.deleteUser(token);
    }


    @Test
    @DisplayName("Register the same user twice")
    @Description("The test is to check that the user can't be created twice")
    public void userCanBeCreatedTest (){

        // Создание клиента
        ValidatableResponse response =userClient.createUser(user);
        // Получение токена пользователя
        token = response.extract().path("accessToken");
        // Попытка создания пользователя с теми же данными
        ValidatableResponse response2 = userClient.createUser(user);
        // Получение кода повторной регистрации
        int actualCode = response2.extract().statusCode();
        // Получение тела ответа повторной регистрации
        boolean isUserCreated = response2.extract().path("success");
        // Получение сообщения об ошибке
        String actualMessage = response2.extract().path("message");

        // Проверка, что статус код соответствует ожидаемому
        assertThat (actualCode, equalTo(expectedCode));
        // Проверка, что пользователь не создан
        assertFalse (isUserCreated);
        // Проверка, что сообщение об ошибке корректное
        assertEquals(actualMessage, expectedMessage);
    }

}
