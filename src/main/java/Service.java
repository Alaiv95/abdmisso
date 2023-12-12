import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Service {

    public static List<Map<Object, Object>> getAllShortIsso() {
        String shortIssoUrl = MyConfig.SHORT_ISSO_URL;

        return given()
                .contentType(ContentType.JSON)
                .body("")
                .when()
                .post(shortIssoUrl)
                .then()
                .extract()
                .response()
                .getBody()
                .jsonPath()
                .getJsonObject("");
    }

    public static Map<Object, Object> getFullIsso(int issoCode) {
        String fullIssoUrl = String.format(MyConfig.FULL_ISSO_URL + "?issoCode=%d", issoCode);

        ResponseBody body = given()
                .contentType(ContentType.JSON)
                .when()
                .get(fullIssoUrl)
                .then()
                .extract()
                .response()
                .getBody();

        String bodyString = body.asString();

        if (bodyString.isEmpty()) {
            return Map.of();
        } else {
            return body
                    .jsonPath()
                    .getJsonObject("");
        }
    }
}
