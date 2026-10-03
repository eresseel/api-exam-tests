package hu.exam.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tesztcsomag 2 - Egy konkrét post lekérdezése")
class SinglePostTest extends BaseTest {

    @Test
    @DisplayName("TC-2.1 - Létező post lekérdezése (pozitív teszt)")
    void getExistingPost_returns200AndCorrectId() {
        Response response = given().spec(spec)
                .when().get("/posts/1")
                .then().extract().response();

        assertEquals(200, response.statusCode(), "A státuszkód 200 kell legyen");
        assertEquals(1, response.jsonPath().getInt("id"), "Az id mező értéke 1 kell legyen");
    }

    @Test
    @DisplayName("TC-2.2 - Nem létező post lekérdezése (negatív teszt)")
    void getNonExistingPost_returns404AndEmptyBody() {
        Response response = given().spec(spec)
                .when().get("/posts/9999")
                .then().extract().response();

        assertEquals(404, response.statusCode(), "A státuszkód 404 kell legyen");

        String body = response.asString().replaceAll("\\s", "");
        assertTrue(body.isEmpty() || body.equals("{}"),
                "A body üres objektum vagy üres válasz kell legyen, de ez volt: " + body);
    }
}
