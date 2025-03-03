import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex11 {

    @Test
    public void cookieAssert(){
        Response response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .then()
                .statusCode(200)
                .extract().response();
       String cookieName = "HomeWork";
       String actualCookieValue = response.getCookie(cookieName);
       String expectedCookieValue = "hw_value";
       assertNotNull(actualCookieValue, "cookie doesn't exists");
       assertEquals(expectedCookieValue, actualCookieValue, "Wrong cookie value");
    }
}
