package actions;

import java.util.Map;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class PlantActions {

    private io.restassured.specification.RequestSpecification requestSpec = SerenityRest.given();
    private Integer createdPlantId;
    private Map<String, Object> createdPlantData;
    private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    private String getAuthToken() {
        return net.serenitybdd.core.Serenity.sessionVariableCalled("authToken");
    }

    private io.restassured.specification.RequestSpecification getAuthenticatedRequest() {
        String token = getAuthToken();
        if (token != null) {
            return SerenityRest.given().header("Authorization", "Bearer " + token);
        }
        return requestSpec;
    }

    @Step("Create a new plant in category {0} with data {1}")
    public void createPlant(int categoryId, Map<String, Object> plantData) {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
        String categoryEndpoint = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.endpoints.plants.category");

        String fullUrl = baseUrl + categoryEndpoint + categoryId;

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .body(plantData)
                .when()
                .post(fullUrl);
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
        SerenityRest.restAssuredThat(response -> response.body("name",
                org.hamcrest.Matchers.containsString(expectedName)));
    }

    @Step("Verify error message contains {0}")
    public void verifyErrorMessage(String expectedMessage) {
        String messageField = SerenityRest.lastResponse().jsonPath().getString("message");
        String errorField = SerenityRest.lastResponse().jsonPath().getString("error");

        boolean messageContains = messageField != null && messageField.contains(expectedMessage);
        boolean errorContains = errorField != null && errorField.contains(expectedMessage);

        if (!messageContains && !errorContains) {
            throw new AssertionError("Expected error message containing '" + expectedMessage
                    + "' but got message: '" + messageField + "' and error: '" + errorField + "'");
        }
    }

    @Step("Create a new plant in category {0} with invalid data {1}")
    public void createPlantWithInvalidData(int categoryId, Map<String, Object> plantData) {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
        String categoryEndpoint = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.endpoints.plants.category");

        String fullUrl = baseUrl + categoryEndpoint + categoryId;

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .body(plantData)
                .when()
                .post(fullUrl);
    }

    @Step("Get plants with pagination: {0}?{1}")
    public void getPlantsWithPagination(String endpoint, String queryParams) {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        String fullUrl = baseUrl + endpoint + "?" + queryParams;

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .when()
                .get(fullUrl);
    }

    @Step("Verify response contains a list of plants")
    public void verifyPlantListExists() {
        SerenityRest.restAssuredThat(
                response -> response.body("content", org.hamcrest.Matchers.notNullValue()));
        SerenityRest.restAssuredThat(response -> response.body("content",
                org.hamcrest.Matchers.instanceOf(java.util.List.class)));
    }

    @Step("Verify response contains pagination metadata")
    public void verifyPaginationMetadata() {
        SerenityRest.restAssuredThat(
                response -> response.body("pageable", org.hamcrest.Matchers.notNullValue()));
        SerenityRest.restAssuredThat(
                response -> response.body("totalElements", org.hamcrest.Matchers.notNullValue()));
        SerenityRest.restAssuredThat(
                response -> response.body("totalPages", org.hamcrest.Matchers.notNullValue()));
        SerenityRest.restAssuredThat(response -> response.body("size", org.hamcrest.Matchers.notNullValue()));
        SerenityRest.restAssuredThat(response -> response.body("number", org.hamcrest.Matchers.notNullValue()));
    }

    @Step("Verify plants contain name: {0}")
    public void verifyPlantsContainName(String searchTerm) {
        java.util.List<String> plantNames = SerenityRest.lastResponse().jsonPath().getList("content.name",
                String.class);

        if (plantNames == null || plantNames.isEmpty()) {
            throw new AssertionError("No plants found in response");
        }

        for (int i = 0; i < plantNames.size(); i++) {
            String name = plantNames.get(i);
            if (name == null) {
                throw new AssertionError("Plant name is null for searchTerm '" + searchTerm + "' at index " + i);
            }
            if (!name.toLowerCase().contains(searchTerm.toLowerCase())) {
                throw new AssertionError(
                        "Plant name '" + name + "' does not contain '" + searchTerm + "'");
            }
        }
    }

    @Step("Get plants by category: {0}")
    public void getPlantsByCategory(String endpoint) {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        String fullUrl = baseUrl + endpoint;

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .when()
                .get(fullUrl);
    }

    @Step("Verify response contains an array of plants")
    public void verifyPlantsArrayExists() {
        SerenityRest.restAssuredThat(response -> response.body("$", org.hamcrest.Matchers.notNullValue()));
        SerenityRest.restAssuredThat(
                response -> response.body("$", org.hamcrest.Matchers.instanceOf(java.util.List.class)));
    }

    @Step("Create a new plant in category {0} and store its ID")
    public void createPlantAndStoreId(int categoryId, Map<String, Object> plantData) {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
        String categoryEndpoint = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.endpoints.plants.category");

        String fullUrl = baseUrl + categoryEndpoint + categoryId;

        io.restassured.response.Response response = getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .body(plantData)
                .when()
                .post(fullUrl);

        int statusCode = response.getStatusCode();
        if (statusCode == 200 || statusCode == 201) {
            try {
                this.createdPlantId = response.jsonPath().getInt("id");
                this.createdPlantData = new java.util.HashMap<>(plantData);
            } catch (Exception e) {
                System.out.println("Could not extract plant ID: " + e.getMessage());
                this.createdPlantId = null;
                this.createdPlantData = null;
            }
        } else {
            System.out.println("Plant creation failed with status: " + statusCode);
            System.out.println("Response: " + response.getBody().asString());
            this.createdPlantId = null;
            this.createdPlantData = null;
        }
    }

    @Step("Delete plant: {0}")
    public void deletePlant(String endpoint) {
        if (this.createdPlantId == null) {
            throw new IllegalStateException(
                    "createdPlantId is null — plant creation failed; cannot proceed with deletion");
        }

        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        String fullUrl = baseUrl + endpoint.replace("{id}", String.valueOf(this.createdPlantId));

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .when()
                .delete(fullUrl);
    }

    @Step("Verify plant no longer exists")
    public void verifyPlantNoLongerExists() {
        if (this.createdPlantId == null) {
            throw new IllegalStateException(
                    "createdPlantId is null — plant creation failed; cannot proceed with verification");
        }

        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        String fullUrl = baseUrl + "/api/plants/" + this.createdPlantId;

        io.restassured.response.Response response = getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .when()
                .get(fullUrl);

        int statusCode = response.getStatusCode();
        if (statusCode != 404) {
            throw new AssertionError(
                    "Expected plant to not exist (404), but got status code: " + statusCode);
        }
    }

    @Step("Update plant price: {0}")
    public void updatePlantPrice(String endpoint, Map<String, Object> updateData) {
        if (this.createdPlantId == null) {
            throw new IllegalStateException(
                    "createdPlantId is null — plant creation failed; cannot proceed with price update");
        }

        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        String fullUrl = baseUrl + endpoint.replace("{id}", String.valueOf(this.createdPlantId));

        Map<String, Object> completeBody = this.createdPlantData != null
                ? new java.util.HashMap<>(this.createdPlantData)
                : new java.util.HashMap<>();
        completeBody.put("price", updateData.get("price"));

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .body(completeBody)
                .when()
                .put(fullUrl);
    }

    @Step("Verify updated price is {0}")
    public void verifyUpdatedPrice(double expectedPrice) {
        Double actualPrice = SerenityRest.lastResponse().jsonPath().getDouble("price");
        org.hamcrest.MatcherAssert.assertThat(
                "Price should match expected value",
                actualPrice,
                org.hamcrest.Matchers.closeTo(expectedPrice, 0.001));
    }

    public void setToken(String token) {
        this.requestSpec = SerenityRest.given().header("Authorization", "Bearer " + token);
    }

    public int getLastCreatedPlantId() {
        return this.createdPlantId != null ? this.createdPlantId : 0;
    }

    public int getLastResponseStatusCode() {
        return SerenityRest.lastResponse().getStatusCode();
    }

    @Step("Get plant by ID: {0}")
    public void getPlant(int plantId) {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl + "/api/plants/" + plantId);
    }

    @Step("Delete plant by ID: {0}")
    public void deletePlant(int plantId) {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .when()
                .delete(baseUrl + "/api/plants/" + plantId);
    }

    @Step("Update plant quantity: {0}")
    public void updatePlantQuantity(String endpoint, Map<String, Object> updateData) {
        if (this.createdPlantId == null) {
            throw new IllegalStateException(
                    "createdPlantId is null — plant creation failed; cannot proceed with quantity update");
        }

        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        String fullUrl = baseUrl + endpoint.replace("{id}", String.valueOf(this.createdPlantId));

        Map<String, Object> completeBody = this.createdPlantData != null
                ? new java.util.HashMap<>(this.createdPlantData)
                : new java.util.HashMap<>();
        completeBody.put("quantity", updateData.get("quantity"));

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .body(completeBody)
                .when()
                .put(fullUrl);
    }

    @Step("Ensure at least one plant exists in the system")
    public void ensureAtLeastOnePlantExists() {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        io.restassured.response.Response response = getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl + "/api/plants");

        boolean plantsExist = false;
        int existingCategoryId = 5;

        if (response.getStatusCode() == 200) {
            try {
                java.util.List<java.util.Map<String, Object>> plants = response.jsonPath().getList("content");
                if (plants == null || plants.isEmpty()) {
                    plants = response.jsonPath().getList("$");
                }
                if (plants != null && !plants.isEmpty()) {
                    java.util.Map<String, Object> existingPlant = plants.get(0);
                    this.createdPlantData = new java.util.HashMap<>();
                    this.createdPlantData.put("name", existingPlant.get("name"));
                    this.createdPlantData.put("price", existingPlant.get("price"));
                    this.createdPlantData.put("quantity", existingPlant.get("quantity"));

                    if (existingPlant.get("category") != null) {
                        Object categoryObj = existingPlant.get("category");
                        if (categoryObj instanceof java.util.Map) {
                            java.util.Map<String, Object> category = (java.util.Map<String, Object>) categoryObj;
                            if (category.get("id") != null) {
                                existingCategoryId = ((Number) category.get("id")).intValue();
                            }
                        } else if (categoryObj instanceof Number) {
                            existingCategoryId = ((Number) categoryObj).intValue();
                        } else if (categoryObj instanceof String) {
                            try {
                                existingCategoryId = Integer.parseInt((String) categoryObj);
                            } catch (NumberFormatException e) {
                                System.out.println("Warning: Could not parse category as integer: " + categoryObj);
                            }
                        }
                    }

                    this.createdPlantId = ((Number) existingPlant.get("id")).intValue();
                    net.serenitybdd.core.Serenity.setSessionVariable("existingPlantCategoryId").to(existingCategoryId);
                    plantsExist = true;
                }
            } catch (Exception e) {
                System.out.println("Warning: Could not parse existing plants: " + e.getMessage());
            }
        }

        if (!plantsExist) {
            Map<String, Object> newPlantData = new java.util.HashMap<>();
            newPlantData.put("name", "TestPlant_" + System.currentTimeMillis());
            newPlantData.put("price", 25.00);
            newPlantData.put("quantity", 100);

            createPlantAndStoreId(existingCategoryId, newPlantData);
            net.serenitybdd.core.Serenity.setSessionVariable("existingPlantCategoryId").to(existingCategoryId);
        }
    }

    @Step("Create a plant with the same name and category as an existing plant")
    public void createDuplicatePlant() {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
        String categoryEndpoint = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.endpoints.plants.category");

        io.restassured.response.Response response = getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl + "/api/plants");

        if (response.getStatusCode() != 200) {
            throw new IllegalStateException("Failed to fetch plants. Status: " + response.getStatusCode());
        }

        java.util.List<java.util.Map<String, Object>> plants = response.jsonPath().getList("content");
        if (plants == null || plants.isEmpty()) {
            plants = response.jsonPath().getList("$");
        }

        if (plants == null || plants.isEmpty()) {
            throw new IllegalStateException("No plants found in the system to duplicate");
        }

        java.util.Map<String, Object> existingPlant = plants.get(0);
        String plantName = (String) existingPlant.get("name");
        Object price = existingPlant.get("price");
        Object quantity = existingPlant.get("quantity");

        int categoryId = 5;
        if (existingPlant.get("category") != null) {
            Object categoryObj = existingPlant.get("category");
            if (categoryObj instanceof java.util.Map) {
                java.util.Map<String, Object> category = (java.util.Map<String, Object>) categoryObj;
                if (category.get("id") != null) {
                    categoryId = ((Number) category.get("id")).intValue();
                }
            } else if (categoryObj instanceof Number) {
                categoryId = ((Number) categoryObj).intValue();
            } else if (categoryObj instanceof String) {
                try {
                    categoryId = Integer.parseInt((String) categoryObj);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Could not parse category as integer: " + categoryObj);
                }
            }
        }

        String fullUrl = baseUrl + categoryEndpoint + categoryId;

        Map<String, Object> duplicateData = new java.util.HashMap<>();
        duplicateData.put("name", plantName);
        duplicateData.put("price", price != null ? price : 25.00);
        duplicateData.put("quantity", quantity != null ? quantity : 100);

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .body(duplicateData)
                .when()
                .post(fullUrl);
    }

    @Step("Find an existing plant with stock")
    public Integer findExistingPlantWithStock() {
        io.restassured.response.Response response = getAuthenticatedRequest()
                .get(EnvironmentSpecificConfiguration.from(environmentVariables)
                        .getProperty("api.base.url") + "/api/plants");

        if (response.getStatusCode() == 200) {
            java.util.List<Map<String, Object>> plants = response.jsonPath().getList("$");
            if (plants != null) {
                for (Map<String, Object> plant : plants) {
                    Number quantity = (Number) plant.get("quantity");
                    if (quantity != null && quantity.intValue() >= 10) {
                        this.createdPlantId = ((Number) plant.get("id")).intValue();
                        this.createdPlantData = plant;
                        return this.createdPlantId;
                    }
                }
            }
        }
        return null;
    }

    @Step("Get plant quantity for plant ID: {0}")
    public int getPlantQuantity(int plantId) {
        io.restassured.response.Response response = getAuthenticatedRequest()
                .get(EnvironmentSpecificConfiguration.from(environmentVariables)
                        .getProperty("api.base.url") + "/api/plants/" + plantId);
        if (response.getStatusCode() == 200) {
            return response.jsonPath().getInt("quantity");
        }
        return 0;
    }

    public String getLastResponseBody() {
        return SerenityRest.lastResponse().getBody().asString();
    }
}

