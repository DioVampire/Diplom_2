package ru.yandex.diplom_2.user;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LoginUserWithoutRequiredFieldsParamTest {

    private static final UserClient userClient = new UserClient();
    private static final User user = User.getRandomUser();
    private int expectedStatus;
    private String expectedMessage;
    private final UserCredentials userCredentials;
    private String token;

    public LoginUserWithoutRequiredFieldsParamTest (UserCredentials userCredentials, int expectedStatus, String expectedMessage) {
        this.userCredentials = userCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedMessage = expectedMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData () {
        return new Object[][] {
                {UserCredentials.getUserWithEmail(user), 401, "email or password are incorrect"},
                {UserCredentials.getUserWithPassword(user), 401, "email or password are incorrect"},
                {UserCredentials.getUserWithName(user), 401, "email or password are incorrect"}
        };
    }

    //Удаление пользователя
    @After
    public void delete() {
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Login user without any of the required fields")
    @Description("The test is to check if a user can be authorized " +
            "1. with login only " +
            "2. with email only " +
            "3. with name only")

    public void courierLoginWithoutNecessaryField () {

        // Создание пользователя
        ValidatableResponse response = userClient.createUser(user);
        // Получение токена пользователя
        token = response.extract().path("accessToken");
        // Авторизация пользователя
        ValidatableResponse login = new UserClient().login(userCredentials);
        // Получение статус кода авторизации
        int ActualStatus = login.extract().statusCode();
        // Получение сообщения об ошибке
        String actualMessage = login.extract ().path ("message");

        // Проверяем что статус код соответствует ожидаемому
        assertEquals (expectedStatus, ActualStatus);
        // Проверяем что сообщение об ошибке соответствует ожидаемому
        assertEquals (expectedMessage, actualMessage);


    }

}
