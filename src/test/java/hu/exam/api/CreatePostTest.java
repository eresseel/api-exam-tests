package hu.exam.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tesztcsomag 3 - Új post létrehozása")
class CreatePostTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CreatePostTest.class);

    @Test
    @DisplayName("TC-3.1 - Új post létrehozása (pozitív teszt + logolás)")
    void createPost_returns201AndGeneratedId() {
        String requestBody = """
                {
                  "title": "projektfeladat",
                  "body": "api házivizsga",
                  "userId": 1
                }
                """;

        // A request mindig logolva van
        log.info("REQUEST: POST {}/posts | Content-Type: application/json | body: {}",
                BASE_URL, requestBody.replaceAll("\\s+", " ").trim());

        Response response = given().spec(spec)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/posts")
                .then().extract().response();

        try {
            assertEquals(201, response.statusCode(), "A státuszkód 201 kell legyen");

            Map<String, Object> json = response.jsonPath().getMap("$");
            assertTrue(json.containsKey("id"), "A válasznak tartalmaznia kell az id mezőt");
            assertNotNull(json.get("id"), "Az id mező nem lehet null");

            log.info("RESPONSE OK: status={} id={}", response.statusCode(), json.get("id"));
        } catch (AssertionError | RuntimeException e) {
            // Hiba esetén a response-t is logoljuk
            log.error("RESPONSE (HIBA): status={} | headers={} | body={}",
                    response.statusCode(), response.headers(), response.asString());
            throw e;
        }
    }
}
