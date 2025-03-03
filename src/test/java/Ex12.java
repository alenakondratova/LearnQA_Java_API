import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex12 {

    @Test
    public void headerAssert(){
        Response response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/homework_header")
                .then()
                .statusCode(200)
                .extract().response();
        String headerName = "x-secret-homework-header";
        String actualHeaderValue = response.getHeader(headerName);
        String expectedHeaderValue = "Some secret value";
        assertNotNull(actualHeaderValue, "header doesn't exists");
        assertEquals(expectedHeaderValue, actualHeaderValue, "Wrong header value");
    }
}
