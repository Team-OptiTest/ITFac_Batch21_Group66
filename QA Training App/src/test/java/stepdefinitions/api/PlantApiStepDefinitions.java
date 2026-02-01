package stepdefinitions.api;

import actions.PlantAction;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;

import java.util.Map;

public class PlantApiStepDefinitions {

    @Steps
    PlantAction plantAction;

    private EnvironmentVariables environmentVariables;

    @Given("the admin is authenticated")
    public void theAdminIsAuthenticated() {
        String username = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.admin.username");
        String password = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.admin.password");
        plantAction.authenticate(username, password);
    }

    @Given("a regular user is authenticated")
    public void aRegularUserIsAuthenticated() {
        String username = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.user.username");
        String password = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.user.password");
        plantAction.authenticate(username, password);
    }

    @When("I GET to {string}")
    public void iGETTo(String endpoint) {
        String categoryEndpoint = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.endpoints.plants.category");

        if (endpoint.contains(categoryEndpoint)) {
            String[] parts = endpoint.split("/");
            int categoryId = Integer.parseInt(parts[parts.length - 1]);
            plantAction.getPlantsByCategory(categoryId);
        }
    }

    @Given("a valid category with ID {int} exists")
    public void aValidCategoryWithIDExists(int id) {
    }

    @When("I POST to {string} with following data:")
    public void iPOSTToWithFollowingData(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);

        Map<String, Object> body = new java.util.HashMap<>();

        String name = data.get("name");
        if ("Rose FINAL".equals(name)) {
            name = name + " " + System.currentTimeMillis();
        }

        body.put("name", name);
        body.put("price", Double.parseDouble(data.get("price")));
        body.put("quantity", Integer.parseInt(data.get("quantity")));

        String[] parts = endpoint.split("/");
        int categoryId = Integer.parseInt(parts[parts.length - 1]);

        plantAction.createPlant(categoryId, body);
        plantAction.setLastCreatedPlantName(name);
    }

    @Given("a plant exists")
    public void aPlantExists() {
        theAdminIsAuthenticated();

        Map<String, Object> body = new java.util.HashMap<>();
        String name = "Plant " + System.currentTimeMillis();
        body.put("name", name);
        body.put("price", 10.0);
        body.put("quantity", 50);
        plantAction.createPlant(5, body);
        plantAction.verifyStatusCode(201);
        plantAction.setLastCreatedPlantName(name);
    }

    @When("I search for the last created plant")
    public void iSearchForTheLastCreatedPlant() {
        plantAction.getPlants(plantAction.getLastCreatedPlantName(), 0, 10);
    }

    @Then("the response should contain the last created plant")
    public void theResponseShouldContainTheLastCreatedPlant() {
        plantAction.verifyPlantListContainsName(plantAction.getLastCreatedPlantName());
    }

    @Then("the response error message for {string} should be {string}")
    public void theResponseErrorMessageForShouldBe(String field, String message) {
        plantAction.verifyDetailErrorMessage(field, message);
    }

    @When("I delete the plant")
    public void iDeleteThePlant() {
        plantAction.deletePlant(plantAction.getLastCreatedPlantId());
    }

    @When("I DELETE to {string}")
    public void iDELETETo(String endpoint) {
        String[] parts = endpoint.split("/");
        int id = Integer.parseInt(parts[parts.length - 1]);
        plantAction.deletePlant(id);
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
        if ("Rose FINAL".equals(name)) {
            plantAction.verifyPlantName(plantAction.getLastCreatedPlantName());
        } else {
            plantAction.verifyPlantName(name);
        }
    }

    @Then("the plant should no longer exist")
    public void thePlantShouldNoLongerExist() {
        plantAction.getPlant(plantAction.getLastCreatedPlantId());
        plantAction.verifyStatusCode(404);
    }

    @When("I GET to {string} with query params page={int}&size={int}")
    public void iGETToWithQueryParamsPageSize(String endpoint, int page, int size) {
        plantAction.getPlants(page, size);
    }

    @Then("the response should contain a list of plants and pagination metadata")
    public void theResponseShouldContainAListOfPlantsAndPaginationMetadata() {
        plantAction.verifyPaginationMetadata();
    }

    @Then("the response should contain a list of plants belonging to that category")
    public void theResponseShouldContainAListOfPlantsBelongingToThatCategory() {
        plantAction.verifyPlantListNotEmpty();
    }

    @When("I search for plants with name {string} and page={int}&size={int}")
    public void iSearchForPlantsWithNameAndPageSize(String name, int page, int size) {
        plantAction.getPlants(name, page, size);
    }

    @Then("the response should contain plants filtering by name {string}")
    public void theResponseShouldContainPlantsFilteringByName(String name) {
        plantAction.verifyPlantListContainsName(name);
    }

    @When("I update the plant price to {double}")
    public void iUpdateThePlantPriceTo(double price) {
        plantAction.updatePlantPrice(plantAction.getLastCreatedPlantId(), price);
    }

    @Then("the plant price should be {double}")
    public void thePlantPriceShouldBe(double price) {
        plantAction.verifyPlantPrice(price);
    }

    @When("I update the plant quantity to {int}")
    public void iUpdateThePlantQuantityTo(int quantity) {
        plantAction.updatePlantQuantity(plantAction.getLastCreatedPlantId(), quantity);
    }

    @Then("the response error message should be {string}")
    public void theResponseErrorMessageShouldBe(String message) {
        plantAction.verifyResponseMessage(message);
    }
}
