package ru.yandex.diplom_2.user;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LoginUserWithIncorrectFieldsTests {

    private User user;
    private UserClient userClient;
    private int expectedStatus;
    private String expectedMessage;
    String token;

    // Создание пользователя
    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
        expectedStatus = 401;
        expectedMessage = "email or password are incorrect";

    }


    @Test
    @DisplayName("Login user with incorrect password")
    @Description("The test is to check that the user can't be authorized with incorrect password")
    public void loginUserWithIncorrectPasswordTest() {

        // Создание пользователя
        ValidatableResponse response = userClient.createUser(user);
        // Получение токена пользователя
        token = response.extract().path("accessToken");
        //Заменить пароль, чтобы отличался от использоывнного при регистрации
        user.setPassword("New_word");
        // Авторизация пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        // Получение статус кода авторизации
        int ActualStatus = login.extract().statusCode();
        // Получение сообщения об ошибке
        String actualMessage = login.extract().path("message");

        // Проверяем что статус код соответствует ожидаемому
        assertEquals (expectedStatus, ActualStatus);
        // Проверяем что сообщение об ошибке соответствует ожидаемому
        assertEquals (expectedMessage, actualMessage);

        // Удаление пользователя
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Login user with incorrect email")
    @Description("The test is to check that the existent user can be logged")
    public void loginUserWithIncorrectEmailTest() {

        // Создание пользователя
        ValidatableResponse response = userClient.createUser(user);
        // Получение токена пользователя
        token = response.extract().path("accessToken");
        //Заменить имейл, чтобы отличался от использоывнного при регистрации
        user.setEmail("neq@example.com");
        // Авторизация пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        // Получение статус кода авторизации
        int ActualStatus = login.extract().statusCode();
        // Получение сообщения об ошибке
        String actualMessage = login.extract().path("message");

        // Проверяем что статус код соответствует ожидаемому
        assertEquals (expectedStatus, ActualStatus);
        // Проверяем что сообщение об ошибке соответствует ожидаемому
        assertEquals (expectedMessage, actualMessage);

        // Удаление пользователя
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Login deleted user")
    @Description("The test is to check that the existent user can't be authorized")
    public void loginDeletedUserTest() {

        // Создание пользователя
        ValidatableResponse response = userClient.createUser(user);
        // Получение токена пользователя
        token = response.extract().path("accessToken");
        // Удаление пользователя
        userClient.deleteUser(token);
        // Авторизация пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        // Получение статус кода авторизации
        int ActualStatus = login.extract().statusCode();
        // Получение сообщения об ошибке
        String actualMessage = login.extract().path("message");

        // Проверяем что статус код соответствует ожидаемому
        assertEquals (expectedStatus, ActualStatus);
        // Проверяем что сообщение об ошибке соответствует ожидаемому
        assertEquals (expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Login non-existent User")
    @Description("The test is to check that the non-existent user can't be authorized'")
    public void loginNonExistentUserTest() {

        // Создание пользователя без регистрации
        User user = new User("new@example.com", "ssddd", "Lechat");
        // Авторизация пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        // Получение статус кода авторизации
        int ActualStatus = login.extract().statusCode();
        // Получение сообщения об ошибке
        String actualMessage = login.extract().path("message");

        // Проверяем что статус код соответствует ожидаемому
        assertEquals (expectedStatus, ActualStatus);
        // Проверяем что сообщение об ошибке соответствует ожидаемому
        assertEquals (expectedMessage, actualMessage);
    }

}
