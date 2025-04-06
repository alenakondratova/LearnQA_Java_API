package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET request with token and auth cookie")
    public Response makeGetRequest (String url, String token, String cookie) {
       return given()
               .filter(new AllureRestAssured())
               .header(new Header("x-csrf-token", token))
               .cookie("auth_sid", cookie)
               .get(url)
               .andReturn();
       }

    @Step("Make a GET request with auth cookie only")
    public Response makeGetRequestWithCookie (String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();

    }

    @Step("Make a GET request with auth token only")
    public Response makeGetRequestWithToken (String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-scrf-token", token))
                .get(url)
                .andReturn();

    }

    @Step("Make a POST request ")
    public Response makePostRequest (String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }
    @Step("Authorization with email")
    public Response login(String email, String password) {
        Map<String, String> authData = Map.of(
                "email", email,
                "password", password
        );

        return RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
    }

    @Step("Get UserData with ID {userId}")
    public Response getUserData(String csrfToken, String authSid, int userId) {
        return RestAssured
                .given()
                .header("x-csrf-token", csrfToken)
                .cookie("auth_sid", authSid)
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
    }
}
