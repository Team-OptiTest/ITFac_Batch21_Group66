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
        } else {
            System.out.println("FAILED TO CREATE PLANT. Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody().asString());
            try {
                java.nio.file.Files.writeString(
                        java.nio.file.Paths.get("d:/Study/ITQA/ITFac_Batch21_Group66/QA Training App/error_log.txt"),
                        response.getBody().asString());
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Step("Get plants with name filter")
    public void getPlants(String name, int page, int size) {
        var request = SerenityRest.given();

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            request.auth().preemptive().basic(username, password);
        }

        request.queryParam("search", name)
                .queryParam("page", page)
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

    @Step("Verify filtered plant list contains name")
    public void verifyPlantListContainsName(String name) {
        SerenityRest.then()
                .body("content.name", org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.containsString(name)));
    }

    @Step("Get plants by category")
    public void getPlantsByCategory(int categoryId) {
        var request = SerenityRest.given();

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            request.auth().preemptive().basic(username, password);
        }

        request.when()
                .get(baseUrl + "/api/plants/category/" + categoryId);

        System.out.println("GET CATEGORY RESPONSE: " + SerenityRest.lastResponse().getBody().asString());
        try {
            java.nio.file.Files.writeString(
                    java.nio.file.Paths.get("d:/Study/ITQA/ITFac_Batch21_Group66/QA Training App/get_log.txt"),
                    SerenityRest.lastResponse().getBody().asString());
        } catch (Exception e) {
        }
    }

    @Step("Verify plant list is not empty")
    public void verifyPlantListNotEmpty() {
        // Assuming the response is a list or pageable content.
        // If it's a direct list: body("$", not(empty()))
        // If it's pageable: body("content", not(empty()))
        // Based on previous tests, it seems to return a Page (content field), but
        // "Returns array of plants" might imply a direct list for this endpoint.
        // Let's assume it returns a list based on typical "get all by category"
        // endpoints,
        // but if it follows the same pattern as /api/plants, it might be paginated.
        // However, usually specific category lists without page params might be just a
        // list.
        // Let's check the previous CREATE test which posted to /api/plants/category/5.
        // If unsure, we can check size > 0.
        // Trying generic check first.
        SerenityRest.then().body("size()", org.hamcrest.Matchers.greaterThan(0));
    }

    @Step("Update plant price")
    public void updatePlantPrice(int id, double newPrice) {
        var request = SerenityRest.given();

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            request.auth().preemptive().basic(username, password);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("name", lastCreatedPlantName != null ? lastCreatedPlantName : "Updated Plant");
        body.put("price", newPrice);
        body.put("quantity", 50);

        request.contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(baseUrl + "/api/plants/" + id);
    }

    @Step("Verify detailed error message for {0}")
    public void verifyDetailErrorMessage(String field, String expectedMessage) {
        SerenityRest.then().body("details." + field, org.hamcrest.Matchers.equalTo(expectedMessage));
    }

    @Step("Verify plant price")
    public void verifyPlantPrice(double price) {
        SerenityRest.then().body("price", org.hamcrest.Matchers.is((float) price));
    }

    @Step("Update plant quantity")
    public void updatePlantQuantity(int id, int quantity) {
        var request = SerenityRest.given();

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            request.auth().preemptive().basic(username, password);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("name", lastCreatedPlantName != null ? lastCreatedPlantName : "Temp Plant");
        body.put("price", 10.0);
        body.put("quantity", quantity);

        // Include category to avoid 500 error
        Map<String, Object> category = new HashMap<>();
        category.put("id", 5);
        category.put("name", "Flowering");
        body.put("category", category);

        request.contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(baseUrl + "/api/plants/" + id);
    }

    @Step("Verify response message")
    public void verifyResponseMessage(String expectedMessage) {
        SerenityRest.then().body("details.quantity", org.hamcrest.Matchers.equalTo(expectedMessage));
    }
}
