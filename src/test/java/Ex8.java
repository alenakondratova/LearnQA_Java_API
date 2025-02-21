import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class Ex8 {

    @Test
    public void TestEx8() throws InterruptedException {
        String link = "https://playground.learnqa.ru/ajax/api/longtime_job";

        Response responseCreate = RestAssured
                .given()
                .when()
                .get(link)
                .then()
                .statusCode(200)
                .extract().response();

        int seconds = responseCreate.jsonPath().getInt("seconds");
        String token = responseCreate.jsonPath().getString("token");

        Response responseCheckStatus = RestAssured
                .given()
                .queryParam("token", token)
                .when()
                .get(link)
                .then()
                .statusCode(200)
                .body("status", equalTo("Job is NOT ready"))
                .extract().response();

        Thread.sleep(seconds * 1000);

        Response responseCheckResult = RestAssured
                .given()
                .queryParam("token", token)
                .when()
                .get(link)
                .then()
                .statusCode(200)
                .body("status", equalTo("Job is ready"))
                .body("result", notNullValue())
                .extract().response();

        System.out.println("Completed. Result: " +responseCheckResult.jsonPath().getString("result"));



    }

}