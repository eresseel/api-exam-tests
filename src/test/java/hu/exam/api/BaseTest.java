package hu.exam.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseTest {

    protected static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    protected static RequestSpecification spec;

    @BeforeAll
    static void setUpSpec() {
        spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addHeader("User-Agent", "Mozilla/5.0 (api-exam-tests)")
                .addHeader("Accept", "application/json")
                .build();
    }
}
