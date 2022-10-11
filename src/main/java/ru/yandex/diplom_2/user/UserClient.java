package ru.yandex.diplom_2.user;
import io.restassured.response.ValidatableResponse;
import ru.yandex.diplom_2.config.Config;
import static io.restassured.RestAssured.given;

public class UserClient extends Config {
    private static final String ROOT = "api/auth";
    private static final String REGISTER = ROOT + "/register";
    private static final String LOGIN = ROOT + "/login";
    private static final String INFO = ROOT + "/user";


    public ValidatableResponse create(User user) {
        return getSpec()
                .body(user)
                .when()
                .post(REGISTER)
                .then().log().all();
    }


    public ValidatableResponse login(UserCredentials userCredentials) {
        return getSpec()
                .body(userCredentials)
                .when()
                .post(LOGIN)
                .then().log().all();
    }


    public ValidatableResponse checkUserInfo(String token) {
        return getSpec()
                .header("Authorization", token)
                .when()
                .get(INFO)
                .then().log().all();
    }


    public ValidatableResponse updateUserInfo(String token, UserCredentials userCredentials) {
        return getSpec()
                .header("Authorization", token)
                .body(userCredentials)
                .when()
                .patch(INFO)
                .then().log().all();
    }

    public void delete(String token) {
        getSpec()
                .header("Authorization", token)
                .when()
                .delete(INFO)
                .then().log().all()
                .assertThat()
                .statusCode(202)
                .extract()
                .path("success");
    }

}
