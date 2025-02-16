import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class Ex5 {

    @Test
    public void TestEx5(){
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        ArrayList<JsonPath> messages = new ArrayList<>(response.getJsonObject("messages"));
        System.out.println(messages.get(1));
    }
}