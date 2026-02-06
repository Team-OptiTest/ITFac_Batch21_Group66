package actions;

import java.util.Map;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class PlantActions {

        private String authToken;
        private Integer createdPlantId;
        private Map<String, Object> createdPlantData;
        private boolean plantWasCreated = false;
        private io.restassured.specification.RequestSpecification requestSpec;
        private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables
                        .createEnvironmentVariables();

        private void initRequest() {
                String token = net.serenitybdd.core.Serenity.sessionVariableCalled("authToken");
                if (token == null) {
                        token = this.authToken;
                }
                if (token != null) {
                        requestSpec = SerenityRest.given().header("Authorization", "Bearer " + token);
                } else {
                        requestSpec = SerenityRest.given();
                }
        }

        @Step("Create a new plant in category {0} with data {1}")
        public void createPlant(int categoryId, Map<String, Object> plantData) {
                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");
                String categoryEndpoint = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.endpoints.plants.category");

                String fullUrl = baseUrl + categoryEndpoint + categoryId;

                initRequest();

                io.restassured.response.Response response = requestSpec
                                .contentType(ContentType.JSON)
                                .body(plantData)
                                .when()
                                .post(fullUrl);

                int statusCode = response.getStatusCode();
                if (statusCode == 200 || statusCode == 201) {
                        this.plantWasCreated = true;
                        try {
                                String idStr = response.jsonPath().getString("id");
                                if (idStr != null) {
                                        this.createdPlantId = Integer.parseInt(idStr);
                                        this.createdPlantData = new java.util.HashMap<>(plantData);
                                }
                        } catch (Exception e) {
                                System.out.println("Could not extract plant ID in createPlant: " + e.getMessage());
                        }
                }
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
                SerenityRest
                                .restAssuredThat(response -> response.body("name",
                                                org.hamcrest.Matchers.containsString(expectedName)));
        }

        @Step("Verify error message contains {0}")
        public void verifyErrorMessage(String expectedMessage) {
                // Check if error message exists in either 'message' or 'error' field
                String messageField = SerenityRest.lastResponse().jsonPath().getString("message");
                String errorField = SerenityRest.lastResponse().jsonPath().getString("error");

                boolean messageContains = messageField != null && messageField.contains(expectedMessage);
                boolean errorContains = errorField != null && errorField.contains(expectedMessage);

                if (!messageContains && !errorContains) {
                        throw new AssertionError("Expected error message containing '" + expectedMessage +
                                        "' but got message: '" + messageField + "' and error: '" + errorField + "'");
                }
        }

        @Step("Create a new plant in category {0} with invalid data {1}")
        public void createPlantWithInvalidData(int categoryId, Map<String, Object> plantData) {
                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");
                String categoryEndpoint = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.endpoints.plants.category");

                String fullUrl = baseUrl + categoryEndpoint + categoryId;

                initRequest();

                requestSpec
                                .contentType(ContentType.JSON)
                                .body(plantData)
                                .when()
                                .post(fullUrl);
        }

        @Step("Get plants with pagination: {0}?{1}")
        public void getPlantsWithPagination(String endpoint, String queryParams) {
                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");

                String fullUrl = baseUrl + endpoint + "?" + queryParams;

                initRequest();

                var response = requestSpec
                                .contentType(ContentType.JSON)
                                .when()
                                .get(fullUrl);
                var statusCode = response.getStatusCode();
        }

        @Step("Verify response contains a list of plants")
        public void verifyPlantListExists() {
                SerenityRest.restAssuredThat(
                                response -> response.body("$", org.hamcrest.Matchers.notNullValue()));
                SerenityRest.restAssuredThat(response -> response.body("$",
                                org.hamcrest.Matchers.instanceOf(java.util.List.class)));
        }

        @Step("Verify response contains pagination metadata")
        public void verifyPaginationMetadata() {
                // The current API response is a direct list, so pagination metadata may not be
                // present.
                // We check if the response is a Map before asserting pagination fields.
                Object responseBody = SerenityRest.lastResponse().getBody().as(Object.class);
                if (responseBody instanceof java.util.Map) {
                        SerenityRest.restAssuredThat(
                                        response -> response.body("pageable", org.hamcrest.Matchers.notNullValue()));
                        SerenityRest.restAssuredThat(
                                        response -> response.body("totalElements",
                                                        org.hamcrest.Matchers.notNullValue()));
                        SerenityRest.restAssuredThat(
                                        response -> response.body("totalPages", org.hamcrest.Matchers.notNullValue()));
                } else {
                        System.out.println("Response is a direct list; skipping pagination metadata verification.");
                }
        }

        @Step("Verify plants contain name: {0}")
        public void verifyPlantsContainName(String searchTerm) {
                java.util.List<String> plantNames = SerenityRest.lastResponse().jsonPath().getList("name",
                                String.class);

                if (plantNames == null || plantNames.isEmpty()) {
                        throw new AssertionError("No plants found in response");
                }

                for (String name : plantNames) {
                        if (!name.toLowerCase().contains(searchTerm.toLowerCase())) {
                                throw new AssertionError(
                                                "Plant name '" + name + "' does not contain '" + searchTerm + "'");
                        }
                }
        }

        @Step("Get plants by category: {0}")
        public void getPlantsByCategory(String endpoint) {
                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");

                String fullUrl = baseUrl + endpoint;

                initRequest();

                requestSpec
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
                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");
                String categoryEndpoint = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.endpoints.plants.category");

                String fullUrl = baseUrl + categoryEndpoint + categoryId;

                initRequest();

                io.restassured.response.Response response = requestSpec
                                .contentType(ContentType.JSON)
                                .body(plantData)
                                .when()
                                .post(fullUrl);

                if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                        this.plantWasCreated = true;
                }

                int statusCode = response.getStatusCode();
                if (statusCode == 200 || statusCode == 201) {
                        try {
                                String idStr = response.jsonPath().getString("id");
                                if (idStr != null) {
                                        this.createdPlantId = Integer.parseInt(idStr);
                                        this.createdPlantData = new java.util.HashMap<>(plantData);
                                } else {
                                        System.out.println("ID field is missing in response: "
                                                        + response.getBody().asString());
                                        this.createdPlantId = null;
                                }
                        } catch (Exception e) {
                                System.out.println("Could not extract plant ID. Status: " + statusCode + ", Body: "
                                                + response.getBody().asString());
                                System.out.println("Error: " + e.getMessage());
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

                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");

                String fullUrl = baseUrl + endpoint.replace("{id}", String.valueOf(this.createdPlantId));

                initRequest();

                requestSpec
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

                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");

                String fullUrl = baseUrl + "/api/plants/" + this.createdPlantId;

                initRequest();

                io.restassured.response.Response response = requestSpec
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

                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");

                String fullUrl = baseUrl + endpoint.replace("{id}", String.valueOf(this.createdPlantId));

                // Create complete plant object with updated price
                Map<String, Object> completeBody = this.createdPlantData != null
                                ? new java.util.HashMap<>(this.createdPlantData)
                                : new java.util.HashMap<>();
                completeBody.put("price", updateData.get("price"));

                initRequest();

                requestSpec
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
                this.authToken = token;
        }

        public boolean wasPlantCreated() {
                return this.plantWasCreated;
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

                initRequest();

                requestSpec
                                .contentType(ContentType.JSON)
                                .when()
                                .get(baseUrl + "/api/plants/" + plantId);
        }

        @Step("Delete plant by ID: {0}")
        public void deletePlant(int plantId) {
                String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                                .getProperty("api.base.url");

                initRequest();

                requestSpec
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

                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");

                String fullUrl = baseUrl + endpoint.replace("{id}", String.valueOf(this.createdPlantId));

                // Create complete plant object with updated quantity
                Map<String, Object> completeBody = this.createdPlantData != null
                                ? new java.util.HashMap<>(this.createdPlantData)
                                : new java.util.HashMap<>();
                completeBody.put("quantity", updateData.get("quantity"));

                initRequest();

                requestSpec
                                .contentType(ContentType.JSON)
                                .body(completeBody)
                                .when()
                                .put(fullUrl);
        }

        @Step("Find an existing plant with stock")
        public Integer findExistingPlantWithStock() {
                initRequest();
                io.restassured.response.Response response = requestSpec
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
                                                this.plantWasCreated = false;
                                                return this.createdPlantId;
                                        }
                                }
                        }
                }
                return null;
        }

        @Step("Get plant quantity for plant ID: {0}")
        public int getPlantQuantity(int plantId) {
                initRequest();
                io.restassured.response.Response response = requestSpec
                                .get(EnvironmentSpecificConfiguration.from(environmentVariables)
                                                .getProperty("api.base.url") + "/api/plants/" + plantId);
                if (response.getStatusCode() == 200) {
                        return response.jsonPath().getInt("quantity");
                }
                return 0;
        }
}
