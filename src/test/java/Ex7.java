import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class Ex7 {

    @Test
    public void TestEx7() {
        String link = "https://playground.learnqa.ru/api/long_redirect";
        int redirectCount = 0;
        while (true) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(link)
                    .andReturn();

            int statusCode = response.getStatusCode();
            if (statusCode == 200) {
                break;
            }
            if (statusCode >= 300 && statusCode < 400) {
                String redirectLink = response.getHeader("Location");
                if (redirectLink == null) {
                    System.out.println("finish. redirect not found");
                    break;
                }

                link = redirectLink;
                redirectCount++;
            } else {
                System.out.println("unexpected status: " + statusCode);
            }
       }

        System.out.println("amount of redirects is " + redirectCount);
    }
}