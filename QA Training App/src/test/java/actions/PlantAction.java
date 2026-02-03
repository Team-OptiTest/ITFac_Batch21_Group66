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
}
