package ru.yandex.diplom_2.user;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class ChangeUserDataTests {

    private static User user;
    private UserClient userClient;
    String token;

    // Создание пользователя
    @Before
    public void setUp() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Change User email with authorization")
    @Description("The test is to check if an authorized user can change his email")
    public void changeEmailWithAuthorizationTest() {

        // Создание пользователя
        userClient.createUser(user);
        // Авторизация пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        // Получение токена пользователя
        token = login.extract().path("accessToken");
        // Изменение мейла пользователя
        ValidatableResponse userData = userClient.updateUserInfo(token, UserCredentials.getUserWithRandomEmail());
        // Получение статус кода
        int statusCode = userData.extract().statusCode();
        // Получение тела ответа после изменений
        boolean isDataChanged = userData.extract().path("success");
        String newEmail = userData.extract().path("email");
        String oldEmail = user.email;

        // Проверка, что статус код соответсвует ожиданиям
        assertThat(statusCode, equalTo(200));
        // Проверка, что информация изменилась
        assertTrue(isDataChanged);
        // Проверка, что имейл изменился успешно
        assertNotEquals(oldEmail,newEmail);

        //Удаление пользователя
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Change User password with authorization")
    @Description("The test is to check if an authorized user can change his password")
    public void changePasswordWithAuthorizationTest() {

        // Создание пользователя
        userClient.createUser(user);
        // Авторизация пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        // Получение токена пользователя
        token = login.extract().path("accessToken");
        // Изменение пароля пользователя
        ValidatableResponse userData = userClient.updateUserInfo(token, UserCredentials.getUserWithRandomPassword());
        // Получение статус кода
        int statusCode = userData.extract().statusCode();
        // Получение тела ответа после изменений
        boolean isDataChanged = userData.extract().path("success");

        // Проверка, что статус код соответсвует ожиданиям
        assertThat (statusCode, equalTo(200));
        // Проверка, что информация изменилась
        assertTrue (isDataChanged);

        //Удаление пользователя
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Change User name with authorization")
    @Description("The test is to check if an authorized user can change his name")
    public void changeNameWithAuthorizationTest() {

        // Создание пользователя
        userClient.createUser(user);
        // Авторизация пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        // Получение токена пользователя
        token = login.extract().path("accessToken");
        // Изменение имени пользователя
        ValidatableResponse info = userClient.updateUserInfo(token, UserCredentials.getUserWithRandomName());
        // Получение статус кода
        int statusCode = info.extract().statusCode();
        // Получение тела ответа после изменений
        boolean isDataChanged = info.extract().path("success");
        String newName = info.extract().path("name");
        String oldName = user.name;

        // Проверка что статус код соответсвует ожиданиям
        assertThat (statusCode, equalTo(200));
        // Проверка, что информация изменилась
        assertTrue (isDataChanged);
        // Проверка, что имя изменилось успешно
        assertNotEquals(oldName, newName);
    }

    @Test
    @DisplayName("Change email without authorization")
    @Description("The test is to check if an unauthorized user can change his email")
    public void changeEmailWithoutAuthorization() {
        token = "";
        // Изменение имейла пользователя
        ValidatableResponse info = userClient.updateUserInfo(token,UserCredentials.getUserWithRandomEmail());
        // Получение статус кода
        int statusCode = info.extract().statusCode();
        // Получение тела ответа после попытки изменений
        boolean isDataChanged = info.extract().path("success");
        String actualMessage = info.extract().path("message");

        // Проверка что статус код соответсвует ожиданиям
        assertThat (statusCode, equalTo(401));
        // Проверка, что информация не изменилась
        assertFalse (isDataChanged);
        // Проверка сообщения
        assertEquals("You should be authorised", actualMessage);
    }

    @Test
    @DisplayName("Change password without authorization")
    @Description("The test is to check if an unauthorized user can change his password")
    public void changePasswordWithoutAuthorizationTest() {

        token = "";
        // Изменение пароля пользователя
        ValidatableResponse info = userClient.updateUserInfo(token, UserCredentials.getUserWithRandomPassword());
        // Получение статус кода
        int statusCode = info.extract().statusCode();
        // Получение тела ответа после попытки изменений
        boolean isDataChanged = info.extract().path("success");
        String actualMessage = info.extract().path("message");

        // Проверка, что статус код соответсвует ожиданиям
        assertThat (statusCode, equalTo(401));
        // Проверка, что информация не изменилась
        assertFalse(isDataChanged);
        // Проверка сообщения
        assertEquals("You should be authorised", actualMessage);
    }

    @Test
    @DisplayName("Change name without authorization")
    @Description("The test is to check if an unauthorized user can change his name")
    public void changeNameWithoutAuthorizationTest() {

        token = "";
        // Получение информации о пользователе
        ValidatableResponse info = userClient.updateUserInfo(token, UserCredentials.getUserWithRandomName());
        // Получение статус кода с тела информации о пользователе
        int statusCode = info.extract().statusCode();
        // Получение тела ответа после попытки изменений
        boolean isDataChanged = info.extract().path("success");
        String actualMessage = info.extract().path("message");

        // Проверка, что статус код соответсвует ожиданиям
        assertThat (statusCode, equalTo(401));
        // Проверка, что информация не изменилась
        assertFalse (isDataChanged);
        // Проверка сообщения
        assertEquals("You should be authorised", actualMessage);
    }


}
