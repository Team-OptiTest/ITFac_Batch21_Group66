package stepdefinitions.api;

import actions.PlantAction;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;

import java.util.Map;

public class PlantApiStepDefinitions {

    private net.thucydides.model.util.EnvironmentVariables environmentVariables;

    @Steps
    PlantAction plantAction;

    @Given("the admin is authenticated")
    public void theAdminIsAuthenticated() {
        String username = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.admin.username");
        String password = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.admin.password");

        plantAction.authenticateAsAdmin(username, password);
    }

    @Given("a valid category with ID {int} exists")
    public void aValidCategoryWithIDExists(int id) {
    }

    @Given("the user is authenticated with ROLE_USER")
    public void theUserIsAuthenticatedWithROLE_USER() {
        String username = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.user.username");
        String password = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.user.password");
        plantAction.authenticateAsUser(username, password);
    }

    @When("I POST to {string} with following data:")
    public void iPOSTToWithFollowingData(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);

        Map<String, Object> body = new java.util.HashMap<>();
        String plantName = data.get("name") + "_" + System.currentTimeMillis();
        body.put("name", plantName);
        body.put("price", Double.parseDouble(data.get("price")));
        body.put("quantity", Integer.parseInt(data.get("quantity")));
        String[] parts = endpoint.split("/");
        int categoryId = Integer.parseInt(parts[parts.length - 1]);

        plantAction.createPlant(categoryId, body);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        plantAction.verifyStatusCode(statusCode);
    }

    @Then("the response should contain a plant object with an assigned ID")
    public void theResponseShouldContainAPlantObjectWithAnAssignedID() {
        plantAction.verifyAssignedId();
    }

    @Then("the plant name should be {string}")
    public void thePlantNameShouldBe(String name) {
        plantAction.verifyPlantName(name);
    }

    @Then("the response error message should contain {string}")
    public void theResponseErrorMessageShouldContain(String expectedMessage) {
        plantAction.verifyErrorMessage(expectedMessage);
    }

    @When("I POST to {string} with invalid data:")
    public void iPOSTToWithInvalidData(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);

        Map<String, Object> body = new java.util.HashMap<>();
        body.put("name", data.get("name"));
        body.put("price", Double.parseDouble(data.get("price")));
        body.put("quantity", Integer.parseInt(data.get("quantity")));
        String[] parts = endpoint.split("/");
        int categoryId = Integer.parseInt(parts[parts.length - 1]);

        plantAction.createPlantWithInvalidData(categoryId, body);
    }

    @When("I GET to {string} with query params {string}")
    public void iGETToWithQueryParams(String endpoint, String queryParams) {
        plantAction.getPlantsWithPagination(endpoint, queryParams);
    }

    @Then("the response should contain a list of plants")
    public void theResponseShouldContainAListOfPlants() {
        plantAction.verifyPlantListExists();
    }

    @Then("the response should contain pagination metadata")
    public void theResponseShouldContainPaginationMetadata() {
        plantAction.verifyPaginationMetadata();
    }

    @Then("the response should contain plants with name containing {string}")
    public void theResponseShouldContainPlantsWithNameContaining(String searchTerm) {
        plantAction.verifyPlantsContainName(searchTerm);
    }

    @When("I GET to {string}")
    public void iGETTo(String endpoint) {
        plantAction.getPlantsByCategory(endpoint);
    }

    @Then("the response should contain an array of plants")
    public void theResponseShouldContainAnArrayOfPlants() {
        plantAction.verifyPlantsArrayExists();
    }

    @Given("a plant with ID exists in the system")
    public void aPlantWithIDExistsInTheSystem() {
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("name", "TestPlant_" + System.currentTimeMillis());
        body.put("price", 25.00);
        body.put("quantity", 100);
        plantAction.createPlantAndStoreId(5, body);
    }

    @When("I DELETE to {string}")
    public void iDELETETo(String endpoint) {
        plantAction.deletePlant(endpoint);
    }

    @Then("the plant should no longer exist when retrieved")
    public void thePlantShouldNoLongerExistWhenRetrieved() {
        plantAction.verifyPlantNoLongerExists();
    }
}
