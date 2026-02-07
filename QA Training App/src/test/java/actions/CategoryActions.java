package actions;

import java.util.Collections;
import java.util.List;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import utils.TestUtils;

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
        } else {
            System.out.println("Warning: Failed to fetch existing category IDs. Status: " + response.getStatusCode());
            existingCategoryIds = null;
        }
    }

    @Step("Send GET request for category with non-existent ID")
    public void getCategoryWithNonExistentId() {
        fetchExistingCategoryIds();
        long nonExistentId = TestUtils.generateNonExistentId(existingCategoryIds);
        String token = getAuthToken();
        String viewUrl = getBaseUrl() + "/api/categories/" + nonExistentId;

        lastResponse = SerenityRest.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(viewUrl);

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
        if (lastResponse == null) {
            throw new IllegalStateException("No response available - request may not have been executed");
        }
        return lastResponse.getStatusCode();
    }

    @Step("Get response body")
    public String getLastResponseBody() {
        if (lastResponse == null) {
            return null;
        }
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

    @Step("Delete all categories via API")
    public void deleteAllCategories() {
        String token = getAuthToken();
        if (token == null) {
            throw new IllegalStateException("Auth token missing; cannot delete categories.");
        }

        // --- Delete all sales first (sales reference plants via FK) ---
        int salesDeletePass = 0;
        final int maxSalesDeletePasses = 10;
        List<Integer> previousSalesIds = null;
        while (salesDeletePass < maxSalesDeletePasses) {
            salesDeletePass++;
            Response salesResponse = SerenityRest.given()
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get(getBaseUrl() + "/api/sales");

            if (salesResponse.getStatusCode() != 200) {
                throw new IllegalStateException("Failed to fetch sales. Status: " + salesResponse.getStatusCode());
            }

            List<Integer> salesIds = salesResponse.jsonPath().getList("id", Integer.class);
            if (salesIds == null || salesIds.isEmpty()) {
                if (salesDeletePass > 1) {
                    System.out.println("All sales deleted after " + salesDeletePass + " passes.");
                } else {
                    System.out.println("No sales to delete.");
                }
                break;
            }
            if (previousSalesIds != null && previousSalesIds.equals(salesIds)) {
                throw new IllegalStateException("No progress deleting sales after pass " + salesDeletePass);
            }
            previousSalesIds = new java.util.ArrayList<>(salesIds);

            for (Integer saleId : salesIds) {
                Response deleteSaleResponse = SerenityRest.given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .delete(getBaseUrl() + "/api/sales/" + saleId);
                if (deleteSaleResponse.getStatusCode() < 200 || deleteSaleResponse.getStatusCode() >= 300) {
                    System.out.println("Failed to delete sale ID " + saleId + " - Status: " + deleteSaleResponse.getStatusCode());
                    System.out.println("Response: " + deleteSaleResponse.getBody().asString());
                } else {
                    System.out.println("Deleted sale ID " + saleId + " - Status: " + deleteSaleResponse.getStatusCode());
                }
            }
        }
        if (salesDeletePass >= maxSalesDeletePasses) {
            throw new IllegalStateException("Sales deletion exceeded " + maxSalesDeletePasses + " passes");
        }

        // --- Delete all plants (with error handling and loop for pagination/partial deletes) ---
        int plantDeletePass = 0;
        final int maxPlantDeletePasses = 10;
        List<Integer> previousPlantIds = null;
        while (plantDeletePass < maxPlantDeletePasses) {
            plantDeletePass++;
            Response plantsResponse = SerenityRest.given()
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get(getBaseUrl() + "/api/plants");

            if (plantsResponse.getStatusCode() != 200) {
                throw new IllegalStateException("Failed to fetch plants. Status: " + plantsResponse.getStatusCode());
            }

            List<Integer> plantIds = plantsResponse.jsonPath().getList("id", Integer.class);
            if (plantIds == null || plantIds.isEmpty()) {
                if (plantDeletePass > 1) {
                    System.out.println("All plants deleted after " + plantDeletePass + " passes.");
                } else {
                    System.out.println("No plants to delete.");
                }
                break;
            }
            if (previousPlantIds != null && previousPlantIds.equals(plantIds)) {
                throw new IllegalStateException("No progress deleting plants after pass " + plantDeletePass);
            }
            previousPlantIds = new java.util.ArrayList<>(plantIds);

            for (Integer plantId : plantIds) {
                Response deletePlantResponse = SerenityRest.given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .delete(getBaseUrl() + "/api/plants/" + plantId);
                if (deletePlantResponse.getStatusCode() < 200 || deletePlantResponse.getStatusCode() >= 300) {
                    System.out.println("Failed to delete plant ID " + plantId + " - Status: " + deletePlantResponse.getStatusCode());
                    System.out.println("Response: " + deletePlantResponse.getBody().asString());
                } else {
                    System.out.println("Deleted plant ID " + plantId + " - Status: " + deletePlantResponse.getStatusCode());
                }
            }
            // Loop again in case more plants remain (pagination or partial delete)
        }
        if (plantDeletePass >= maxPlantDeletePasses) {
            throw new IllegalStateException("Plant deletion exceeded " + maxPlantDeletePasses + " passes");
        }

        // --- Now delete all categories (with error handling) ---
        Response response = SerenityRest.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(getBaseUrl() + "/api/categories");

        if (response.getStatusCode() != 200) {
            throw new IllegalStateException("Failed to fetch categories. Status: " + response.getStatusCode());
        }

        List<Integer> categoryIds = response.jsonPath().getList("id", Integer.class);

        if (categoryIds == null || categoryIds.isEmpty()) {
            System.out.println("No categories to delete.");
            return;
        }

        // Copy into a mutable list, then delete in reverse order so children are removed before parents
        categoryIds = new java.util.ArrayList<>(categoryIds);
        Collections.sort(categoryIds, Collections.reverseOrder());

        for (Integer id : categoryIds) {
            Response deleteResponse = SerenityRest.given()
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .delete(getBaseUrl() + "/api/categories/" + id);
            if (deleteResponse.getStatusCode() < 200 || deleteResponse.getStatusCode() >= 300) {
                System.out.println("Failed to delete category ID " + id + " - Status: " + deleteResponse.getStatusCode());
                System.out.println("Response: " + deleteResponse.getBody().asString());
            } else {
                System.out.println("Deleted category ID " + id + " - Status: " + deleteResponse.getStatusCode());
            }
        }

        System.out.println("All categories delete attempt complete.");
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
        long nonExistentId = TestUtils.generateNonExistentId(existingCategoryIds);

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
        long nonExistentId = TestUtils.generateNonExistentId(existingCategoryIds);

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

    @Step("Create category with non-existent parent ID")
    public void createCategoryWithNonExistentParentId() {
        fetchExistingCategoryIds();
        long nonExistentParentId = TestUtils.generateNonExistentId(existingCategoryIds);

        String token = getAuthToken();
        String createUrl = getBaseUrl() + "/api/categories";
        String requestBody = String.format("{\"name\":\"TestCateg\",\"parent\":%d}", nonExistentParentId);

        lastResponse = SerenityRest.given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(createUrl);

    }

    @Step("Search categories with name: {0} and parentId: {1}")
    public void searchCategories(String categoryName, String parentId) {
        String token = getAuthToken();
        String searchUrl = getBaseUrl() + "/api/categories";

        System.out.println("=== SEARCH CATEGORIES DEBUG ===");
        System.out.println("Search URL: " + searchUrl);
        System.out.println("Category Name: " + categoryName);
        System.out.println("Parent ID: " + parentId);
        System.out.println("Auth Token: " + (token != null ? "Present" : "NULL"));
        System.out.println("===============================");

        var request = SerenityRest.given()
                .header("Authorization", "Bearer " + token);
        if (categoryName != null) {
            request = request.queryParam("name", categoryName);
        }
        if (parentId != null) {
            request = request.queryParam("parentId", parentId);
        }
        lastResponse = request.when().get(searchUrl);

        System.out.println("Status Code: " + lastResponse.getStatusCode());
        System.out.println("Response: " + lastResponse.getBody().asString());
    }

    @Step("Get main categories (categories without parent)")
    public void getMainCategories() {
        String token = getAuthToken();
        String mainCategoriesUrl = getBaseUrl() + "/api/categories/main";

        lastResponse = SerenityRest.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(mainCategoriesUrl);

    }

    @Step("Verify response contains a list of main categories")
    public boolean responseContainsMainCategoriesList() {
        if (lastResponse == null) {
            return false;
        }
        try {
            List<?> categories = lastResponse.jsonPath().getList("$");
            return categories != null && !categories.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Create category and a parent category with name: {0} and parentName {1}")
    public void createCategoryAndSubCategory(String categoryName, String parentCategory){
        String requestBody = String.format("{\"name\":\"%s\"}", parentCategory);
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
                int parentId = lastResponse.jsonPath().getInt("id");
                String createUrl = getBaseUrl() + "/api/categories";
                String categoryRequestBody = String.format("{\"name\":\"%s\",\"parent\": { \"name\": \"%s\", \"id\": \"%d\"}}", categoryName, parentCategory, parentId);

                Response categeoryResponse = SerenityRest.given()
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .body(categoryRequestBody)
                        .when()
                        .post(createUrl);

                Serenity.setSessionVariable("childCatIdForPlantRead").to(categeoryResponse.jsonPath().getInt("id"));

            } catch (Exception e) {
                System.out.println("Could not extract category ID: " + e.getMessage());
            }
        } else {
            // Request failed - don't try to extract ID
            System.out.println("Category creation failed with status: " + lastResponse.getStatusCode());
        }
    }

    @Step("Get main category names")
    public java.util.List<String> getMainCategoryNames() {
        String token = getAuthToken();
        String mainCategoriesUrl = getBaseUrl() + "/api/categories/main";

        Response response = SerenityRest.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(mainCategoriesUrl);

        if (response.getStatusCode() == 200) {
            try {
                return response.jsonPath().getList("name", String.class);
            } catch (Exception e) {
                return java.util.Collections.emptyList();
            }
        }
        return java.util.Collections.emptyList();
    }

    @Step("Ensure at least one main category exists")
    public void ensureAtLeastOneMainCategoryExists() {
        String token = getAuthToken();
        String mainCategoriesUrl = getBaseUrl() + "/api/categories/main";

        Response response = SerenityRest.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(mainCategoriesUrl);

        if (response.getStatusCode() == 200) {
            List<?> categories = response.jsonPath().getList("$");
            if (categories != null && !categories.isEmpty()) {
                return; // Main category already exists
            }
        }

        // Create a main category if none exists
        createCategory("MainCatTest");
    }

    @Step("Search categories with name: {0}")
    public int searchAndGetCategoryIdFromName(String categoryName) {
        String token = getAuthToken();
        String searchUrl = getBaseUrl() + "/api/categories";

        var request = SerenityRest.given()
                .header("Authorization", "Bearer " + token);
        if (categoryName != null) {
            request = request.queryParam("name", categoryName);
        }
        lastResponse = request.when().get(searchUrl);
        return lastResponse.getBody().jsonPath().getList("id", Integer.class).getFirst();
    }
}
