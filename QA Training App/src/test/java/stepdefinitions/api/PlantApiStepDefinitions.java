package stepdefinitions.api;

import actions.AuthenticationActions;
import actions.PlantActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;

import java.util.Map;

public class PlantApiStepDefinitions {

    private net.thucydides.model.util.EnvironmentVariables environmentVariables;

    @Steps
    PlantActions plantActions;

    @Steps
    AuthenticationActions authenticationActions;

    @Given("the admin is authenticated")
    public void theAdminIsAuthenticated() {
        authenticationActions.authenticateAsAdmin();
    }

    @Given("the user is authenticated with ROLE_USER")
    public void theUserIsAuthenticatedWithROLE_USER() {
        authenticationActions.authenticateUser();
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

        plantActions.createPlant(categoryId, body);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        plantActions.verifyStatusCode(statusCode);
    }

    @Then("the response should contain a plant object with an assigned ID")
    public void theResponseShouldContainAPlantObjectWithAnAssignedID() {
        plantActions.verifyAssignedId();
    }

    @Then("the plant name should be {string}")
    public void thePlantNameShouldBe(String name) {
        plantActions.verifyPlantName(name);
    }

    @Then("the response error message should contain {string}")
    public void theResponseErrorMessageShouldContain(String expectedMessage) {
        plantActions.verifyErrorMessage(expectedMessage);
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

        plantActions.createPlantWithInvalidData(categoryId, body);
    }

    @When("I GET to {string} with query params {string}")
    public void iGETToWithQueryParams(String endpoint, String queryParams) {
        plantActions.getPlantsWithPagination(endpoint, queryParams);
    }

    @Then("the response should contain a list of plants")
    public void theResponseShouldContainAListOfPlants() {
        plantActions.verifyPlantListExists();
    }

    @Then("the response should contain pagination metadata")
    public void theResponseShouldContainPaginationMetadata() {
        plantActions.verifyPaginationMetadata();
    }

    @Then("the response should contain plants with name containing {string}")
    public void theResponseShouldContainPlantsWithNameContaining(String searchTerm) {
        plantActions.verifyPlantsContainName(searchTerm);
    }

    @When("I GET to {string}")
    public void iGETTo(String endpoint) {
        plantActions.getPlantsByCategory(endpoint);
    }

    @Then("the response should contain an array of plants")
    public void theResponseShouldContainAnArrayOfPlants() {
        plantActions.verifyPlantsArrayExists();
    }

    @Given("a plant with ID exists in the system")
    public void aPlantWithIDExistsInTheSystem() {
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("name", "TestPlant_" + System.currentTimeMillis());
        body.put("price", 25.00);
        body.put("quantity", 100);
        plantActions.createPlantAndStoreId(5, body);
    }

    @When("I DELETE to {string}")
    public void iDELETETo(String endpoint) {
        plantActions.deletePlant(endpoint);
    }

    @Then("the plant should no longer exist when retrieved")
    public void thePlantShouldNoLongerExistWhenRetrieved() {
        plantActions.verifyPlantNoLongerExists();
    }

    @When("I PUT to {string} with new price {string}")
    public void iPUTToWithNewPrice(String endpoint, String newPrice) {
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("price", Double.parseDouble(newPrice));
        plantActions.updatePlantPrice(endpoint, body);
    }

    @Then("the response should show updated price {string}")
    public void theResponseShouldShowUpdatedPrice(String expectedPrice) {
        plantActions.verifyUpdatedPrice(Double.parseDouble(expectedPrice));
    }

    @When("I PUT to {string} with new quantity {string}")
    public void iPUTToWithNewQuantity(String endpoint, String newQuantity) {
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("quantity", Integer.parseInt(newQuantity));
        plantActions.updatePlantQuantity(endpoint, body);
    }
}
