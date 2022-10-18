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

public class LoginExistentUserTest {

    private User user;
    private UserClient userClient;
    String token;

    // Создание пользователя
    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }

    // Удаление пользователя с токеном авторизации
    @After
    public void delete() {

        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Login user with one incorrect field")
    @Description("The test is to check that the existent user can be logged")
    public void loginExistentUserTest() {

        // Создание пользователя
        userClient.createUser(user);
        // Авторизация пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        // Получение статус кода авторизации
        int statusCode = login.extract().statusCode();
        // Получение ответа
        boolean isUserAuthorized = login.extract().path("success");
        // Получение токена авторизированого пользователя
        token = login.extract().path("accessToken");

        // Проверка что статус код соответсвует ожиданиям
        assertThat (statusCode, equalTo(200));
        // Проверка что пользователь авторизовался
        assertTrue (isUserAuthorized);
        // Проверка что токен пользователя не пустой
        assertThat(token, notNullValue());
    }

}
