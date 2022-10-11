package ru.yandex.diplom_2.user;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class RegisterUserWithoutRequiredFieldsParamTest {

    private final User user;
    private final int expectedStatus;
    private final String expectedMessage;

    // Метод для параметризации
    public RegisterUserWithoutRequiredFieldsParamTest (User user,int expectedStatus, String expectedMessage) {
        this.user = user;
        this.expectedStatus = expectedStatus;
        this.expectedMessage = expectedMessage;
    }
    // Параметризация условий авторизации
    @Parameterized.Parameters
    public static Object[][] getTestData () {
        return new Object[][] {
                {User.getUserRandomEmail(), 403, "Email, password and name are required fields"},
                {User.getUserRandomPassword(), 403, "Email, password and name are required fields"},
                {User.getUserRandomName(), 403, "Email, password and name are required fields"},
                {User.getUserWithoutEmail(),403,"Email, password and name are required fields"},
                {User.getUserWithoutPassword(),403,"Email, password and name are required fields"},
                {User.getUserWithoutName(),403,"Email, password and name are required fields"},
                {User.getUserWithEmptyEmail(),403,"Email, password and name are required fields"},
                {User.getUserWithEmptyPassword(),403,"Email, password and name are required fields"},
                {User.getUserWithEmptyName(),403,"Email, password and name are required fields"}
        };
    }

    @Test
    @DisplayName("Register user without any of the required fields")
    @Description("The test is to check that a user can't be created" +
            "1. With email field only " +
            "2. With password field only " +
            "3. With name field only " +
            "4. Without email field" +
            "5. Without password field" +
            "6. Without name field" +
            "7. With empty email field" +
            "8. With empty password field" +
            "9. With empty name field")

    public void courierNotCreatedWithoutNecessaryField () {

        // Создание пользователя
        ValidatableResponse response = new UserClient().create(user);
        // Получение статус кода
        int actualStatus = response.extract().statusCode();
        // Получение тела ответа при создании пользователя
        boolean isUserNotCreated = response.extract().path("success");
        // Получение сообщения об ошибке
        String errorMessage = response.extract().path("message");

        // Проверка, что статус код соответствует ожидаемому
        assertEquals(expectedStatus, actualStatus);
        // Проверка, что пользователь не создан
        assertFalse (isUserNotCreated);
        // Проверка, что сообщение об ошибке корректное
        assertEquals(expectedMessage, errorMessage);
    }

}
