package actions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import io.restassured.http.ContentType;
import java.util.Map;

public class PlantAction {

        private net.thucydides.model.util.EnvironmentVariables environmentVariables;
        private io.restassured.specification.RequestSpecification requestSpec = SerenityRest.given();

        @Step("Authenticate as admin")
        public void authenticateAsAdmin(String username, String password) {
                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");
                String loginEndpoint = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.endpoints.auth.login");

                Map<String, String> credentials = new java.util.HashMap<>();
                credentials.put("username", username);
                credentials.put("password", password);

                String token = SerenityRest.given()
                                .contentType(ContentType.JSON)
                                .body(credentials)
                                .post(baseUrl + loginEndpoint)
                                .jsonPath()
                                .getString("token");

                this.requestSpec = SerenityRest.given().header("Authorization", "Bearer " + token);
        }

        @Step("Authenticate as normal user")
        public void authenticateAsUser(String username, String password) {
                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.base.url");
                String loginEndpoint = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getProperty("api.endpoints.auth.login");

                Map<String, String> credentials = new java.util.HashMap<>();
                credentials.put("username", username);
                credentials.put("password", password);

                String token = SerenityRest.given()
                                .contentType(ContentType.JSON)
                                .body(credentials)
                                .post(baseUrl + loginEndpoint)
                                .jsonPath()
                                .getString("token");

                this.requestSpec = SerenityRest.given().header("Authorization", "Bearer " + token);
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

                requestSpec
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

                requestSpec
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
}
