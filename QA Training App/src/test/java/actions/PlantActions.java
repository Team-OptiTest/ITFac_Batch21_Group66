package actions;

import java.util.Map;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import utils.TestUtils;

public class PlantActions {

    private io.restassured.specification.RequestSpecification requestSpec = SerenityRest.given();
    private Integer createdPlantId;
    private Map<String, Object> createdPlantData;
    private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    private Response lastResponse;

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
                throw new AssertionError(
                        "Plant name is null for searchTerm '" + searchTerm + "' at index " + i);
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

        var catId = Serenity.sessionVariableCalled("childCatIdForPlantRead");
        String fullUrl = baseUrl + endpoint + catId;

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
                int createdPlantIdFoDelete = response.jsonPath().getInt("id");
                Serenity.setSessionVariable("createdPlantId").to(createdPlantIdFoDelete);
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
        var idForDelete = Serenity.sessionVariableCalled("createdPlantId");
        if (idForDelete == null) {
            throw new IllegalStateException(
                    "createdPlantId is null — plant creation failed; cannot proceed with deletion");
        }

        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        String fullUrl = baseUrl + endpoint.replace("{id}", String.valueOf(idForDelete));

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .when()
                .delete(fullUrl);
    }

    @Step("Verify plant no longer exists")
    public void verifyPlantNoLongerExists() {
        var idForDelete = Serenity.sessionVariableCalled("createdPlantId");
        if (idForDelete == null) {
            throw new IllegalStateException(
                    "createdPlantId is null — plant creation failed; cannot proceed with verification");
        }

        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        String fullUrl = baseUrl + "/api/plants/" + idForDelete;

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

        @Step("Update plant with name: {0}")
        public void updatePlantName(String name) {
        if (this.createdPlantId == null) {
            throw new IllegalStateException(
                "createdPlantId is null — plant creation failed; cannot proceed with name update");
        }

        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
            .getProperty("api.base.url");

        String fullUrl = baseUrl + "/api/plants/" + this.createdPlantId;

        Map<String, Object> completeBody = this.createdPlantData != null
            ? new java.util.HashMap<>(this.createdPlantData)
            : new java.util.HashMap<>();
        completeBody.put("name", name);

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
                java.util.List<java.util.Map<String, Object>> plants = response.jsonPath()
                        .getList("content");

                // Check if list contains only nulls (which happens when projecting "content" on a root list)
                boolean isListOfNulls = plants != null && !plants.isEmpty() && plants.get(0) == null;

                if (plants == null || plants.isEmpty() || isListOfNulls) {
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
                                existingCategoryId = ((Number) category.get("id"))
                                        .intValue();
                            }
                        } else if (categoryObj instanceof Number) {
                            existingCategoryId = ((Number) categoryObj).intValue();
                        } else if (categoryObj instanceof String) {
                            try {
                                existingCategoryId = Integer
                                        .parseInt((String) categoryObj);
                            } catch (NumberFormatException e) {
                                System.out.println(
                                        "Warning: Could not parse category as integer: "
                                        + categoryObj);
                            }
                        }
                    }

                    this.createdPlantId = ((Number) existingPlant.get("id")).intValue();
                    net.serenitybdd.core.Serenity.setSessionVariable("existingPlantCategoryId")
                            .to(existingCategoryId);
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
            net.serenitybdd.core.Serenity.setSessionVariable("existingPlantCategoryId")
                    .to(existingCategoryId);
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

        // Check if list contains only nulls (which happens when projecting "content" on a root list)
        boolean isListOfNulls = plants != null && !plants.isEmpty() && plants.get(0) == null;

        if (plants == null || plants.isEmpty() || isListOfNulls) {
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

    @Step("Fetch existing category IDs from the system")
    private java.util.List<Integer> fetchExistingCategoryIds() {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        io.restassured.response.Response response = getAuthenticatedRequest()
                .when()
                .get(baseUrl + "/api/categories");

        if (response.getStatusCode() == 200) {
            return response.jsonPath().getList("id", Integer.class);
        } else {
            System.out.println("Warning: Failed to fetch existing category IDs. Status: " + response.getStatusCode());
            return null;
        }
    }

    @Step("Create a plant with a non-existent category ID")
    public void createPlantWithNonExistentCategory() {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
        String categoryEndpoint = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.endpoints.plants.category");

        // Fetch existing category IDs and generate a non-existent one
        java.util.List<Integer> existingCategoryIds = fetchExistingCategoryIds();
        long nonExistentCategoryId = TestUtils.generateNonExistentId(existingCategoryIds);
        String fullUrl = baseUrl + categoryEndpoint + nonExistentCategoryId;

        Map<String, Object> plantData = new java.util.HashMap<>();
        plantData.put("name", "TestPlant_" + System.currentTimeMillis());
        plantData.put("price", 25.00);
        plantData.put("quantity", 100);

        getAuthenticatedRequest()
                .contentType(ContentType.JSON)
                .body(plantData)
                .when()
                .post(fullUrl);
    }

    @Step("Verify inventory statistics")
    public void verifyInventoryStatistics() {
        SerenityRest.then()
                .body("totalPlants", org.hamcrest.Matchers.notNullValue())
                .body("lowStockPlants", org.hamcrest.Matchers.notNullValue());
    }

    @Step("Verify page number is {0}")
    public void verifyPageNumber(int expectedPage) {
        SerenityRest.then().body("number", org.hamcrest.Matchers.equalTo(expectedPage));
    }

    @Step("Verify response has content array")
    public void verifyResponseHasContentArray() {
        SerenityRest.then()
                .body("content", org.hamcrest.Matchers.notNullValue());
    }

    @Step("Verify page size is {0}")
    public void verifyPageSize(int expectedSize) {
        SerenityRest.then().body("size", org.hamcrest.Matchers.equalTo(expectedSize));
    }

    @Step("Fetch existing plant IDs from the system")
    private java.util.List<Integer> fetchExistingPlantIds() {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        io.restassured.response.Response response = SerenityRest.given()
                .header("Authorization", "Bearer " + getAuthToken())
                .when()
                .get(baseUrl + "/api/plants");

        if (response.getStatusCode() == 200) {
            return response.jsonPath().getList("id", Integer.class);
        } else {
            return null;
        }
    }

    @Step("Send GET request for plant with non-existent ID")
    public void getPlantWithNonExistentId() {
        java.util.List<Integer> existingPlantIds = fetchExistingPlantIds();
        long nonExistentId = TestUtils.generateNonExistentId(existingPlantIds);
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
        String viewUrl = baseUrl + "/api/plants/" + nonExistentId;

        SerenityRest.given()
                .header("Authorization", "Bearer " + getAuthToken())
                .when()
                .get(viewUrl);

    }

            @Step("Send GET request for plants with non-existent category ID")
            public void getPlantsByNonExistentCategoryId() {
            java.util.List<Integer> existingCategoryIds = fetchExistingCategoryIds();
            long nonExistentCategoryId = TestUtils.generateNonExistentId(existingCategoryIds);
            String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
            String categoryEndpoint = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.endpoints.plants.category");

            String viewUrl = baseUrl + categoryEndpoint + nonExistentCategoryId;

            SerenityRest.given()
                .header("Authorization", "Bearer " + getAuthToken())
                .when()
                .get(viewUrl);
            }

    @Step("Ensure at least one plant with quantity less than 5 exists")
    public void ensureLowQuantityPlantExists() {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        io.restassured.response.Response response = getAuthenticatedRequest()
                .contentType(io.restassured.http.ContentType.JSON)
                .when()
                .get(baseUrl + "/api/plants");

        // Check if a low-quantity plant already exists
        if (response.getStatusCode() == 200) {
            try {
                java.util.List<java.util.Map<String, Object>> plants = response.jsonPath().getList("$");
                if (plants == null || plants.isEmpty()) {
                    plants = response.jsonPath().getList("content");
                }
                if (plants != null) {
                    for (java.util.Map<String, Object> plant : plants) {
                        Number quantity = (Number) plant.get("quantity");
                        if (quantity != null && quantity.intValue() < 5) {
                            System.out.println("Low-quantity plant already exists: " + plant.get("name")
                                    + " (qty=" + quantity + ")");
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Warning: Could not parse existing plants: " + e.getMessage());
            }
        }

        // No low-quantity plant found — create one
        // First, find or create an existing category
        io.restassured.response.Response catResponse = getAuthenticatedRequest()
                .when()
                .get(baseUrl + "/api/categories");

        int categoryId = -1;
        if (catResponse.getStatusCode() == 200) {
            // Find a sub-category (one with a parent) since plants can only be added to sub-categories
            java.util.List<java.util.Map<String, Object>> categories = catResponse.jsonPath().getList("$");
            if (categories != null) {
                for (java.util.Map<String, Object> cat : categories) {
                    if (cat.get("parent") != null) {
                        categoryId = ((Number) cat.get("id")).intValue();
                        break;
                    }
                }
            }
        }

        // No categories exist — create a parent + sub-category so the plant has a valid sub-category
        if (categoryId < 0) {
            // Create parent category
            String suffix = String.valueOf(System.currentTimeMillis() % 10000);
            io.restassured.response.Response createParentResponse = getAuthenticatedRequest()
                    .contentType(io.restassured.http.ContentType.JSON)
                    .body("{\"name\":\"Par" + suffix + "\"}")
                    .when()
                    .post(baseUrl + "/api/categories");

            if (createParentResponse.getStatusCode() != 200 && createParentResponse.getStatusCode() != 201) {
                throw new IllegalStateException(
                        "Failed to create parent category. Status: " + createParentResponse.getStatusCode()
                                + " Body: " + createParentResponse.getBody().asString());
            }
            int parentId = createParentResponse.jsonPath().getInt("id");
            String parentName = createParentResponse.jsonPath().getString("name");
            System.out.println("Created parent category ID " + parentId);

            // Create sub-category under parent
            String subCatBody = String.format(
                    "{\"name\":\"Sub%s\",\"parent\":{\"id\":%d,\"name\":\"%s\"}}",
                    suffix, parentId, parentName);

            io.restassured.response.Response createSubResponse = getAuthenticatedRequest()
                    .contentType(io.restassured.http.ContentType.JSON)
                    .body(subCatBody)
                    .when()
                    .post(baseUrl + "/api/categories");

            if (createSubResponse.getStatusCode() != 200 && createSubResponse.getStatusCode() != 201) {
                throw new IllegalStateException(
                        "Failed to create sub-category. Status: " + createSubResponse.getStatusCode()
                                + " Body: " + createSubResponse.getBody().asString());
            }
            categoryId = createSubResponse.jsonPath().getInt("id");
            System.out.println("Created sub-category ID " + categoryId + " for low-stock plant.");
        }

        String categoryEndpoint = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.endpoints.plants.category");

        Map<String, Object> plantData = new java.util.HashMap<>();
        plantData.put("name", "LowStock_" + String.valueOf(System.currentTimeMillis()).substring(8));
        plantData.put("price", 10.00);
        plantData.put("quantity", 2);

        io.restassured.response.Response createResponse = getAuthenticatedRequest()
                .contentType(io.restassured.http.ContentType.JSON)
                .body(plantData)
                .when()
                .post(baseUrl + categoryEndpoint + categoryId);

        int status = createResponse.getStatusCode();
        if (status != 200 && status != 201) {
            throw new IllegalStateException(
                    "Failed to create low-quantity plant. Status: " + status
                            + " Body: " + createResponse.getBody().asString());
        }
        try {
            int plantId = createResponse.jsonPath().getInt("id");
            Serenity.setSessionVariable("lowStockPlantId").to(plantId);
            System.out.println("Created low-quantity plant ID " + plantId + ". Status: " + status);
        } catch (Exception e) {
            System.out.println("Created low-quantity plant but could not extract ID. Status: " + status);
        }
    }

    @Step("Clean up the low-stock test plant")
    public void cleanupLowStockPlant() {
        Integer plantId = Serenity.sessionVariableCalled("lowStockPlantId");
        if (plantId == null) {
            System.out.println("No low-stock plant to clean up (was pre-existing or ID not stored).");
            return;
        }
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        // Delete inventory records for this plant first (BUG-007 workaround)
        utils.DatabaseCleanupUtil.deleteInventoryForPlant(plantId);

        io.restassured.response.Response response = getAuthenticatedRequest()
                .when()
                .delete(baseUrl + "/api/plants/" + plantId);
        if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
            System.out.println("Cleaned up low-stock plant ID " + plantId);
        } else {
            System.out.println("Warning: Failed to clean up plant ID " + plantId
                    + " - Status: " + response.getStatusCode());
        }
    }

    @Step("Delete all plants via API")
    public void deleteAllPlants() {
        String token = getAuthToken();
        if (token == null) {
            throw new IllegalStateException("Auth token missing; cannot delete plants.");
        }

        // --- Delete all sales first (sales reference plants via FK) ---
        int salesDeletePass = 0;
        final int maxSalesDeletePasses = 10;
        java.util.List<Integer> previousSalesIds = null;
        while (salesDeletePass < maxSalesDeletePasses) {
            salesDeletePass++;
            io.restassured.response.Response salesResponse = SerenityRest.given()
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get(getBaseUrl() + "/api/sales");

            if (salesResponse.getStatusCode() != 200) {
                throw new IllegalStateException("Failed to fetch sales. Status: " + salesResponse.getStatusCode());
            }

            java.util.List<Integer> salesIds = salesResponse.jsonPath().getList("id", Integer.class);
            if (salesIds == null || salesIds.isEmpty()) {
                System.out.println(salesDeletePass > 1 ? "All sales deleted after " + salesDeletePass + " passes." : "No sales to delete.");
                break;
            }
            if (previousSalesIds != null && previousSalesIds.equals(salesIds)) {
                throw new IllegalStateException("No progress deleting sales after pass " + salesDeletePass);
            }
            previousSalesIds = new java.util.ArrayList<>(salesIds);

            for (Integer saleId : salesIds) {
                io.restassured.response.Response deleteSaleResponse = SerenityRest.given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .delete(getBaseUrl() + "/api/sales/" + saleId);
                if (deleteSaleResponse.getStatusCode() < 200 || deleteSaleResponse.getStatusCode() >= 300) {
                    System.out.println("Failed to delete sale ID " + saleId + " - Status: " + deleteSaleResponse.getStatusCode());
                } else {
                    System.out.println("Deleted sale ID " + saleId);
                }
            }
        }
        if (salesDeletePass >= maxSalesDeletePasses) {
            throw new IllegalStateException("Sales deletion exceeded " + maxSalesDeletePasses + " passes");
        }

        // --- BUG-007 Workaround: Delete all inventory records via JDBC ---
        // No /api/inventory endpoint exists, so direct DB cleanup is required
        // to avoid FK constraint violation (inventory.plant_id -> plants.id)
        utils.DatabaseCleanupUtil.deleteAllInventory();

        // --- Delete all plants ---
        int plantDeletePass = 0;
        final int maxPlantDeletePasses = 10;
        java.util.List<Integer> previousPlantIds = null;
        while (plantDeletePass < maxPlantDeletePasses) {
            plantDeletePass++;
            io.restassured.response.Response plantsResponse = SerenityRest.given()
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get(getBaseUrl() + "/api/plants");

            if (plantsResponse.getStatusCode() != 200) {
                throw new IllegalStateException("Failed to fetch plants. Status: " + plantsResponse.getStatusCode());
            }

            java.util.List<Integer> plantIds = plantsResponse.jsonPath().getList("id", Integer.class);
            if (plantIds == null || plantIds.isEmpty()) {
                System.out.println(plantDeletePass > 1 ? "All plants deleted after " + plantDeletePass + " passes." : "No plants to delete.");
                break;
            }
            if (previousPlantIds != null && previousPlantIds.equals(plantIds)) {
                throw new IllegalStateException("No progress deleting plants after pass " + plantDeletePass);
            }
            previousPlantIds = new java.util.ArrayList<>(plantIds);

            for (Integer plantId : plantIds) {
                io.restassured.response.Response deletePlantResponse = SerenityRest.given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .delete(getBaseUrl() + "/api/plants/" + plantId);
                if (deletePlantResponse.getStatusCode() < 200 || deletePlantResponse.getStatusCode() >= 300) {
                    System.out.println("Failed to delete plant ID " + plantId + " - Status: " + deletePlantResponse.getStatusCode());
                } else {
                    System.out.println("Deleted plant ID " + plantId);
                }
            }
        }
        if (plantDeletePass >= maxPlantDeletePasses) {
            throw new IllegalStateException("Plant deletion exceeded " + maxPlantDeletePasses + " passes");
        }

        System.out.println("All plants delete attempt complete.");
    }

    private String getBaseUrl() {
        return EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
    }

    @Step("Verify read-only format")
    public void verifyReadOnlyFormat() {
        net.serenitybdd.rest.SerenityRest.restAssuredThat(response
                -> response.body("$", org.hamcrest.Matchers.not(org.hamcrest.Matchers.hasKey("content"))));

        net.serenitybdd.rest.SerenityRest.restAssuredThat(response
                -> response.body("$", org.hamcrest.Matchers.not(org.hamcrest.Matchers.hasKey("plants"))));

        net.serenitybdd.rest.SerenityRest.restAssuredThat(response
                -> response.body("$", org.hamcrest.Matchers.not(org.hamcrest.Matchers.hasKey("id"))));

        net.serenitybdd.rest.SerenityRest.restAssuredThat(response
                -> response.body("$", org.hamcrest.Matchers.not(org.hamcrest.Matchers.hasKey("name"))));

        net.serenitybdd.rest.SerenityRest.restAssuredThat(response
                -> response.body("$", org.hamcrest.Matchers.hasKey("totalPlants")));
    }

}
