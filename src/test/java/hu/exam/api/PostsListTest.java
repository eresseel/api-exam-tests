package hu.exam.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tesztcsomag 1 - Postok listázása")
class PostsListTest extends BaseTest {

    @Test
    @DisplayName("TC-1.1 - Postok lekérdezése (pozitív teszt)")
    void getAllPosts_returnsNonEmptyListWithRequiredFields() {
        Response response = given().spec(spec)
                .when().get("/posts")
                .then().extract().response();

        assertEquals(200, response.statusCode(), "A státuszkód 200 kell legyen");
        assertFalse(response.asString().isBlank(), "A válasz body nem lehet üres");

        Object root = response.jsonPath().get("$");
        assertInstanceOf(List.class, root, "A válasznak listának kell lennie");

        List<Map<String, Object>> posts = response.jsonPath().getList("$");
        assertFalse(posts.isEmpty(), "A lista legalább egy elemet tartalmazzon");

        for (Map<String, Object> post : posts) {
            assertTrue(post.containsKey("id"), "Hiányzik az id mező: " + post);
            assertTrue(post.containsKey("title"), "Hiányzik a title mező: " + post);
            assertTrue(post.containsKey("body"), "Hiányzik a body mező: " + post);
        }
    }
}
