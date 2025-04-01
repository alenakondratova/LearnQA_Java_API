import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex13 {

    @ParameterizedTest
    @CsvSource({"'Mozilla/5.0 (iPhone; CPU iPhone OS 13_2 like Mac OS X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.72 Mobile Safari/537.36', 'iOS', 'Chrome', 'mobile'",
            "'Mozilla/5.0 (Linux; Android 10; SM-G975F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Mobile Safari/537.36', 'Android', 'Chrome', 'mobile'",
            "'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36', 'Unknown', 'Chrome', 'web'",
            "'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36', 'Unknown', 'Chrome', 'web'",
            "'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0', 'Unknown', 'Firefox', 'web'",
            "'Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0', 'Unknown', 'Firefox', 'web'"})

      public void UserAgentTest(String userAgent, String expectedDevice, String expectedBrowser, String expectedPlatform) {
        Response response = given()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .then()
                .extract()
                .response();

        String actualDevice = response.jsonPath().getString("device");
        String actualBrowser = response.jsonPath().getString("browser");
        String actualPlatform = response.jsonPath().getString("platform");

        System.out.println("Test User-Agent " + userAgent);
        System.out.println("Expected: device - " + expectedDevice + ", browser - " + expectedBrowser + ", platform - " + expectedPlatform);
        System.out.println("Actual: device - " + actualDevice + ", browser - " + actualBrowser + ", platform - " + actualPlatform);

        if (!actualDevice.equals(expectedDevice) ||
                !actualBrowser.equals(expectedBrowser) ||
                !actualPlatform.equals(expectedPlatform)) {
            System.out.println("Error in User-Agent: " + userAgent);
            if (!actualDevice.equals(expectedDevice)) {
                System.out.println("Wrong device");
            }
            if (!actualBrowser.equals(expectedBrowser)) {
                System.out.println("wrong browser");
            }
            if (!actualPlatform.equals(expectedPlatform)) {
                System.out.println("wrong platform");
            }
        }

        assertEquals(expectedBrowser, actualBrowser, "Browser is incorrect");
        assertEquals(expectedDevice, actualDevice, "Device is incorrect");
        assertEquals(expectedPlatform, actualPlatform, "Platform is incorrect");

    }
    }
