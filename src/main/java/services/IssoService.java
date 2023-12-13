package services;

import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

import properties.IssoCodesTypes;
import properties.MyConfig;

import static io.restassured.RestAssured.given;

public class IssoService {

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

        ResponseBody<?> body = given()
                .contentType(ContentType.JSON)
                .when()
                .get(fullIssoUrl)
                .then()
                .extract()
                .response()
                .getBody();

        if (body.asString().isEmpty()) {
            return Map.of();
        } else {
            return body
                    .jsonPath()
                    .getJsonObject("");
        }
    }

    public static List<Map<Object, Object>> getAllFullIssoOfType(int issoType) {
        String allIssoUrl = MyConfig.ALL_FULL_ISSO_URL + issoType;

        ResponseBody<?> body = given()
                .contentType(ContentType.JSON)
                .when()
                .get(allIssoUrl)
                .then()
                .extract()
                .response()
                .getBody();

        if (body.asString().isEmpty()) {
            return List.of(Map.of());
        } else {
            return body
                    .jsonPath()
                    .getJsonObject("");
        }
    }

    public static List<Map<Object, Object>> getAllFullIsso() {
        Set<Integer> allIssoTypes = IssoCodesTypes.getAllIssoTypes();

        return allIssoTypes
                .stream()
                .map(IssoService::getAllFullIssoOfType)
                .flatMap(List::stream)
                .toList();
    }
}
