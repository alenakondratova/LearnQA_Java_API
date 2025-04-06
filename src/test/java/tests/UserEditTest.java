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

public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testEditJustCreatedTest() {
       // GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData= new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    public void testEditUserWithoutAuth() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreate = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath();
        String userId = responseCreate.getString("id");

        Map<String, String> editData = Map.of("firstName", "Unauthorized");

        Response responseEdit = apiCoreRequests.makePutRequestWithoutAuth(
                "https://playground.learnqa.ru/api/user/" + userId,
                editData
        );

        Assertions.assertResponseTextContains(responseEdit, "Auth token not supplied");
        Assertions.assertResponseCodeEquals(responseEdit, 400);
    }

    @Test
    public void testEditAnotherUserData() {
        Map<String, String> user1Data = DataGenerator.getRegistrationData();
        String userId1 = RestAssured
                .given()
                .body(user1Data)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath().getString("id");

        Map<String, String> user2Data = DataGenerator.getRegistrationData();
        RestAssured
                .given()
                .body(user2Data)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath();

        Response loginAsUser2 = apiCoreRequests.login(user2Data.get("email"), user2Data.get("password"));

        Map<String, String> editData = Map.of("firstName", "Hacker");

        Response responseEdit = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId1,
                this.getHeader(loginAsUser2, "x-csrf-token"),
                this.getCookie(loginAsUser2, "auth_sid"),
                editData
        );

        Assertions.assertResponseCodeEquals(responseEdit, 400);
    }

    @Test
    public void testEditEmailToInvalidFormat() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreate = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath();

        String userId = responseCreate.getString("id");

        Response login = apiCoreRequests.login(userData.get("email"), userData.get("password"));

        Map<String, String> editData = Map.of("email", "invalidemail.com");

        Response responseEdit = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(login, "x-csrf-token"),
                this.getCookie(login, "auth_sid"),
                editData
        );

        Assertions.assertResponseTextContains(responseEdit, "Invalid email format");
        Assertions.assertResponseCodeEquals(responseEdit, 400);
    }

    @Test
    public void testEditFirstNameToShortValue() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreate = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath();

        String userId = responseCreate.getString("id");

        Response login = apiCoreRequests.login(userData.get("email"), userData.get("password"));

        Map<String, String> editData = Map.of("firstName", "A");

        Response responseEdit = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(login, "x-csrf-token"),
                this.getCookie(login, "auth_sid"),
                editData
        );

        Assertions.assertJsonByName(responseEdit,  "error", "The value for field `firstName` is too short");
        Assertions.assertResponseCodeEquals(responseEdit, 400);
    }
}

