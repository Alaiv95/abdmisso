package services;

import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import properties.IssoCodesTypes;
import properties.MyConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class IssoService {
    private static final Logger logger = LoggerFactory.getLogger(IssoService.class);

    public static List<Map<Object, Object>> getAllShortIsso() {
        String shortIssoUrl = MyConfig.SHORT_ISSO_URL;
        logger.info("Getting all short isso from ABDM.");

        List<Map<Object, Object>> shortIssoObjects = given()
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

        logger.info("Finished getting all short isso from ABDM.");

        return shortIssoObjects;
    }

    public static Map<Object, Object> getFullIsso(int issoCode) {
        logger.info("Getting full isso based on issoCode");
        String fullIssoUrl = String.format(MyConfig.FULL_ISSO_URL + "?issoCode=%d", issoCode);

        ResponseBody<?> body = given()
                .contentType(ContentType.JSON)
                .when()
                .get(fullIssoUrl)
                .then()
                .extract()
                .response()
                .getBody();

        logger.info("Finished getting full isso based on issoCode.");

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
        logger.info("Getting all full isso based on types from ABDM.");
        Set<Integer> allIssoTypes = IssoCodesTypes.getAllIssoTypes();

        return allIssoTypes
                .stream()
                .map(IssoService::getAllFullIssoOfType)
                .flatMap(List::stream)
                .toList();
    }
}
