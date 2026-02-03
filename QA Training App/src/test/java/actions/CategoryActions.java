package actions;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;
import net.thucydides.model.environment.SystemEnvironmentVariables;

import java.util.List;

public class CategoryActions {

    private Response lastResponse;
    private Integer lastCreatedCategoryId;
    private List<Integer> existingCategoryIds;
    
    private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    
    private String getBaseUrl() {
        return EnvironmentSpecificConfiguration.from(environmentVariables)
            .getProperty("api.base.url");
    }

    /**
     * Retrieves auth token from Serenity session (set by AuthenticationActions)
     */
    private String getAuthToken() {
        return Serenity.sessionVariableCalled("authToken");
    }

    @Step("Retrieve existing category IDs from the system")
    public void fetchExistingCategoryIds() {
        Response response = SerenityRest.given()
            .header("Authorization", "Bearer " + getAuthToken())
            .when()
            .get(getBaseUrl() + "/api/categories");
        
        if (response.getStatusCode() == 200) {
            existingCategoryIds = response.jsonPath().getList("id", Integer.class);
        }
    }

    @Step("Send GET request for category with non-existent ID")
    public void getCategoryWithNonExistentId() {
        long nonExistentId = generateNonExistentId();
        
        lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + getAuthToken())
            .when()
            .get(getBaseUrl() + "/api/categories/" + nonExistentId);
    }

    @Step("Send GET request for category with ID: {0}")
    public void getCategoryById(long categoryId) {
        lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + getAuthToken())
            .when()
            .get(getBaseUrl() + "/api/categories/" + categoryId);
    }

    @Step("Get response status code")
    public int getLastResponseStatusCode() {
        return lastResponse.getStatusCode();
    }

    @Step("Get response body")
    public String getLastResponseBody() {
        return lastResponse.getBody().asString();
    }

    @Step("Get last created category ID")
    public Integer getLastCreatedCategoryId() {
        return lastCreatedCategoryId;
    }
    
    private long generateNonExistentId() {
        long nonExistentId = 999999L;
        if (existingCategoryIds != null && !existingCategoryIds.isEmpty()) {
            nonExistentId = existingCategoryIds.stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0) + 99999;
        }
        return nonExistentId;
    }

    @Step("Create category with name: {0}")
    public void createCategory(String categoryName) {
        String requestBody = String.format("{\"name\":\"%s\"}", categoryName);
        String token = getAuthToken();
        
        lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body(requestBody)
            .when()
            .post(getBaseUrl() + "/api/categories");
        
        // Only try to extract ID if the request was successful
        if (lastResponse.getStatusCode() == 201 || lastResponse.getStatusCode() == 200) {
            try {
                lastCreatedCategoryId = lastResponse.jsonPath().getInt("id");
            } catch (Exception e) {
                System.out.println("Could not extract category ID: " + e.getMessage());
            }
        } else {
            // Request failed - don't try to extract ID
            lastCreatedCategoryId = null;
            System.out.println("Category creation failed with status: " + lastResponse.getStatusCode());
        }
        
        System.out.println("=== CREATE CATEGORY RESULT ===");
        System.out.println("Input: " + categoryName);
        System.out.println("Used: " + categoryName);
        System.out.println("Status: " + lastResponse.getStatusCode());
        System.out.println("Response: " + lastResponse.getBody().asString());
        System.out.println("==============================");
    }

    @Step("Delete category with ID: {0}")
    public void deleteCategoryById(Integer categoryId) {

        String token = getAuthToken();
        String deleteUrl = getBaseUrl() + "/api/categories/" + categoryId;
        
        System.out.println("=== DELETE CATEGORY DEBUG ===");
        System.out.println("Category ID: " + categoryId);
        System.out.println("Delete URL: " + deleteUrl);
        System.out.println("Auth Token: " + (token != null ? "Present" : "NULL"));
        System.out.println("=============================");

        lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .when()
            .delete(getBaseUrl() + "/api/categories/" + categoryId);

        System.out.println("Status Code: " + lastResponse.getStatusCode());
    }
}