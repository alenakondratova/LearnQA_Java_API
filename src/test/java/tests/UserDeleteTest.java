package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @Test
    public void testDeleteUserById2() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseLogin = apiCoreRequests.login(authData.get("email"), authData.get("password"));

        Response responseDelete = apiCoreRequests.deleteUser(
                "https://playground.learnqa.ru/api/user/2",
                this.getHeader(responseLogin, "x-csrf-token"),
                this.getCookie(responseLogin, "auth_sid")
        );

        Assertions.assertResponseCodeEquals(responseDelete, 400); // Forbidden
        Assertions.assertJsonByName(responseDelete, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    public void testCreateAndDeleteUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreate = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath();

        String userId = responseCreate.getString("id");

        Response responseLogin = apiCoreRequests.login(userData.get("email"), userData.get("password"));

        Response responseDelete = apiCoreRequests.deleteUser(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseLogin, "x-csrf-token"),
                this.getCookie(responseLogin, "auth_sid")
        );

        Assertions.assertResponseCodeEquals(responseDelete, 200);

        Response responseGetUser = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseLogin, "x-csrf-token"),
                this.getCookie(responseLogin, "auth_sid")
        );

        Assertions.assertResponseCodeEquals(responseGetUser, 404);
    }

    @Test
    public void testDeleteUserAsAnotherUser() {
        Map<String, String> userData1 = DataGenerator.getRegistrationData();
        String userId1 = RestAssured
                .given()
                .body(userData1)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath().getString("id");

        Map<String, String> userData2 = DataGenerator.getRegistrationData();
        RestAssured
                .given()
                .body(userData2)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath();

        Response responseLoginAsUser2 = apiCoreRequests.login(userData2.get("email"), userData2.get("password"));

        Response responseDelete = apiCoreRequests.deleteUser(
                "https://playground.learnqa.ru/api/user/" + userId1,
                this.getHeader(responseLoginAsUser2, "x-csrf-token"),
                this.getCookie(responseLoginAsUser2, "auth_sid")
        );

        Assertions.assertResponseCodeEquals(responseDelete, 400);
        Assertions.assertJsonByName(responseDelete, "error", "This user can only delete their own account.");
    }
}
