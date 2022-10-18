package ru.yandex.diplom_2.user;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class RegisterUserWithValidDataTest {

    private User user;
    private UserClient userClient;
    String token;

    // Создание пользователя
    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }

    // Удаление пользователя
    @After
    public void delete(){
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Register a new user successfully")
    @Description("The test is to check if a user can be registered with valid data successfully")
    public void createUserTest() {

        // Создание пользователя
        ValidatableResponse response = userClient.createUser(user);
        // Получение статус кода пользователя
        int statusCode = response.extract().statusCode();
        // Получение ответа
        boolean isUserCreated = response.extract().path("success");
        // Получение токена пользователя
        token = response.extract().path("accessToken");

        // Проверка, что статус код соответствует ожиданиям
        assertThat (statusCode, equalTo(200));
        // Проверка, что пользователь создан
        assertTrue (isUserCreated);
        // Проверка, что токен пользователя не пустой
        assertThat(token, notNullValue());
    }

}
