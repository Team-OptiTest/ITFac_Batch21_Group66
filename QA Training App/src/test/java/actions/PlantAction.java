package actions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import io.restassured.http.ContentType;
import java.util.Map;

public class PlantAction {

    private String baseUrl = "http://localhost:8080";

    @Step("Authenticate as admin")
    public void authenticateAsAdmin(String username, String password) {
        SerenityRest.given()
                .auth().basic(username, password);
    }

    @Step("Create a new plant in category {0} with data {1}")
    public void createPlant(int categoryId, Map<String, Object> plantData) {
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .body(plantData)
                .when()
                .post(baseUrl + "/api/plants/category/" + categoryId);
    }

    @Step("Verify response status code is {0}")
    public void verifyStatusCode(int expectedStatus) {
        SerenityRest.restAssuredThat(response -> response.statusCode(expectedStatus));
    }

    @Step("Verify response contains assigned ID")
    public void verifyAssignedId() {
        SerenityRest.restAssuredThat(response -> response.body("id", org.hamcrest.Matchers.notNullValue()));
    }

    @Step("Verify plant name is {0}")
    public void verifyPlantName(String expectedName) {
        SerenityRest.restAssuredThat(response -> response.body("name", org.hamcrest.Matchers.equalTo(expectedName)));
    }
}
