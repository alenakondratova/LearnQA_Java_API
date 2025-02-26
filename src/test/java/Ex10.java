import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex10 {

    @Test
    public void shortPhraseTest() {

        String testString = "Hi, I'm a new String";

        assertTrue(testString.length() > 15, "error");

    }


}
