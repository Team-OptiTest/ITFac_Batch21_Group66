package actions;

import java.util.List;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

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
        Integer sessionId = Serenity.sessionVariableCalled("lastCreatedCategoryId");
        if (sessionId != null) {
            return sessionId;
        }
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
                Serenity.setSessionVariable("lastCreatedCategoryId").to(lastCreatedCategoryId);
            } catch (Exception e) {
                System.out.println("Could not extract category ID: " + e.getMessage());
                lastCreatedCategoryId = null;
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
        if (categoryId == null) {
            System.out.println("=== DELETE CATEGORY DEBUG ===");
            System.out.println("Category ID is NULL - cannot delete");
            System.out.println("=============================");
            // Create a mock failed response by attempting to delete with invalid ID
            categoryId = -1;
        }

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
            .delete(deleteUrl);

        System.out.println("Status Code: " + lastResponse.getStatusCode());
    }
    @Step("Get categories summary")
public void getCategoriesSummary() {
    lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + getAuthToken())
            .get(getBaseUrl() + "/api/categories/summary");
}

    @Step("Delete category with non-existent ID")
    public void deleteCategoryWithNonExistentId() {
        fetchExistingCategoryIds();
        long nonExistentId = generateNonExistentId();
        
        String token = getAuthToken();
        String deleteUrl = getBaseUrl() + "/api/categories/" + nonExistentId;
        
        System.out.println("=== DELETE NON-EXISTENT CATEGORY DEBUG ===");
        System.out.println("Non-existent Category ID: " + nonExistentId);
        System.out.println("Delete URL: " + deleteUrl);
        System.out.println("Auth Token: " + (token != null ? "Present" : "NULL"));
        System.out.println("=========================================");

        lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .when()
            .delete(deleteUrl);

        System.out.println("Status Code: " + lastResponse.getStatusCode());
        System.out.println("Response: " + lastResponse.getBody().asString());
    }

    @Step("Update category with non-existent ID")
    public void updateCategoryWithNonExistentId() {
        fetchExistingCategoryIds();
        long nonExistentId = generateNonExistentId();
        
        String token = getAuthToken();
        String updateUrl = getBaseUrl() + "/api/categories/" + nonExistentId;
        String requestBody = String.format("{\"name\":\"%s\"}", "UpdatedName");
        
        System.out.println("=== UPDATE NON-EXISTENT CATEGORY DEBUG ===");
        System.out.println("Non-existent Category ID: " + nonExistentId);
        System.out.println("Update URL: " + updateUrl);
        System.out.println("Auth Token: " + (token != null ? "Present" : "NULL"));
        System.out.println("=========================================");

        lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body(requestBody)
            .when()
            .put(updateUrl);

        System.out.println("Status Code: " + lastResponse.getStatusCode());
        System.out.println("Response: " + lastResponse.getBody().asString());
    }

    @Step("Get category list")
    public void getCategoryList() {
        String token = getAuthToken();
        
        System.out.println("=== GET CATEGORY LIST DEBUG ===");
        System.out.println("Auth Token: " + (token != null ? "Present" : "NULL"));
        System.out.println("================================");

        lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get(getBaseUrl() + "/api/categories");

        System.out.println("Status Code: " + lastResponse.getStatusCode());
        System.out.println("Response: " + lastResponse.getBody().asString());
    }

    @Step("Update category with ID: {0} and name: {1}")
    public void updateCategory(Integer categoryId, String updatedName) {
        if (categoryId == null) {
            System.out.println("=== UPDATE CATEGORY DEBUG ===");
            System.out.println("Category ID is NULL - cannot update");
            System.out.println("=============================");
            // Use invalid ID to trigger expected failure
            categoryId = -1;
        }

        String token = getAuthToken();
        String updateUrl = getBaseUrl() + "/api/categories/" + categoryId;
        String requestBody = String.format("{\"name\":\"%s\"}", updatedName);
        
        System.out.println("=== UPDATE CATEGORY DEBUG ===");
        System.out.println("Category ID: " + categoryId);
        System.out.println("Updated Name: " + updatedName);
        System.out.println("Update URL: " + updateUrl);
        System.out.println("Auth Token: " + (token != null ? "Present" : "NULL"));
        System.out.println("=============================");

        lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body(requestBody)
            .when()
            .put(updateUrl);

        System.out.println("Status Code: " + lastResponse.getStatusCode());
        System.out.println("Response: " + lastResponse.getBody().asString());
    }

    @Step("Search categories with name: {0} and parentId: {1}")
    public void searchCategories(String categoryName, String parentId) {
        String token = getAuthToken();
        
        String searchUrl = getBaseUrl() + "/api/categories";
        if (categoryName != null || parentId != null) {
            searchUrl += "?";
            if (categoryName != null) {
                searchUrl += "name=" + categoryName;
                if (parentId != null) {
                    searchUrl += "&parentId=" + parentId;
                }
            } else if (parentId != null) {
                searchUrl += "parentId=" + parentId;
            }
        }
        
        System.out.println("=== SEARCH CATEGORIES DEBUG ===");
        System.out.println("Search URL: " + searchUrl);
        System.out.println("Category Name: " + categoryName);
        System.out.println("Parent ID: " + parentId);
        System.out.println("Auth Token: " + (token != null ? "Present" : "NULL"));
        System.out.println("===============================");

        lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get(searchUrl);

        System.out.println("Status Code: " + lastResponse.getStatusCode());
        System.out.println("Response: " + lastResponse.getBody().asString());
    }
}