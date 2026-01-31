package actions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;
import java.util.HashMap;

public class PlantAction {

    private String baseUrl = "http://localhost:8080";
    private String username;
    private String password;
    private String token;
    private int lastCreatedPlantId;
    private String lastCreatedPlantName;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setLastCreatedPlantName(String name) {
        this.lastCreatedPlantName = name;
    }

    public String getLastCreatedPlantName() {
        return this.lastCreatedPlantName;
    }

    @Step("Authenticate with credentials")
    public void authenticate(String username, String password) {
        this.username = username;
        this.password = password;

        // Attempt to get JWT token
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", username);
        loginData.put("password", password);

        Response response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post(baseUrl + "/api/auth/login");

        if (response.getStatusCode() == 200) {
            this.token = response.jsonPath().getString("token");
        } else {
            this.token = null; // Reset token if login fails
        }
    }

    @Step("Create a new plant")
    public void createPlant(int categoryId, Map<String, Object> plantData) {
        var request = SerenityRest.given()
                .contentType(ContentType.JSON);

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            // Fallback for unauthorized tests or if token retrieval failed
            request.auth().preemptive().basic(username, password);
        }

        Response response = request.body(plantData)
                .when()
                .post(baseUrl + "/api/plants/category/" + categoryId);

        if (response.getStatusCode() == 201) {
            lastCreatedPlantId = response.jsonPath().getInt("id");
        }
    }

    @Step("Delete a plant")
    public void deletePlant(int id) {
        var request = SerenityRest.given();

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            request.auth().preemptive().basic(username, password);
        }

        request.when()
                .delete(baseUrl + "/api/plants/" + id);
    }

    @Step("Get a plant")
    public void getPlant(int id) {
        var request = SerenityRest.given();

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            request.auth().preemptive().basic(username, password);
        }

        request.when()
                .get(baseUrl + "/api/plants/" + id);
    }

    public int getLastCreatedPlantId() {
        return lastCreatedPlantId;
    }

    @Step("Verify response status code")
    public void verifyStatusCode(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    @Step("Verify assigned ID")
    public void verifyAssignedId() {
        SerenityRest.then().body("id", org.hamcrest.Matchers.notNullValue());
    }

    @Step("Verify plant name")
    public void verifyPlantName(String expectedName) {
        SerenityRest.then().body("name", org.hamcrest.Matchers.equalTo(expectedName));
    }

    @Step("Get plants with pagination")
    public void getPlants(int page, int size) {
        var request = SerenityRest.given();

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            request.auth().preemptive().basic(username, password);
        }

        request.queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(baseUrl + "/api/plants");
    }

    @Step("Verify pagination metadata")
    public void verifyPaginationMetadata() {
        SerenityRest.then()
                .body("content", org.hamcrest.Matchers.notNullValue())
                .body("pageable", org.hamcrest.Matchers.notNullValue())
                .body("totalElements", org.hamcrest.Matchers.notNullValue());
    }
}
