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
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
        if (baseUrl != null) {
            plantAction.setBaseUrl(baseUrl);
        }

        String username = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.admin.username");
        String password = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.admin.password");
        plantAction.authenticate(username, password);
    }

    @Given("a regular user is authenticated")
    public void aRegularUserIsAuthenticated() {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
        if (baseUrl != null) {
            plantAction.setBaseUrl(baseUrl);
        }

        String username = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.user.username");
        String password = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.user.password");
        plantAction.authenticate(username, password);
    }

    @Given("a valid category with ID {int} exists")
    public void aValidCategoryWithIDExists(int id) {
        // Implementation for checking category existence if needed
    }

    @When("I POST to {string} with following data:")
    public void iPOSTToWithFollowingData(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);

        // Convert to Map<String, Object> to ensure proper JSON types (numbers vs
        // strings)
        Map<String, Object> body = new java.util.HashMap<>();

        String name = data.get("name");
        // Ensure unique name for "Rose FINAL" to avoid DUPLICATE errors
        if ("Rose FINAL".equals(name)) {
            name = name + " " + System.currentTimeMillis();
        }

        body.put("name", name);
        body.put("price", Double.parseDouble(data.get("price")));
        body.put("quantity", Integer.parseInt(data.get("quantity")));

        // Extracting category ID from endpoint /api/plants/category/{id}
        String[] parts = endpoint.split("/");
        int categoryId = Integer.parseInt(parts[parts.length - 1]);

        plantAction.createPlant(categoryId, body);
        plantAction.setLastCreatedPlantName(name);
    }

    @Given("a plant exists")
    public void aPlantExists() {
        // Create a plant to ensure one exists for deletion
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("name", "Plant to Delete");
        body.put("price", 10.0);
        body.put("quantity", 50);
        plantAction.createPlant(5, body); // Assuming category 5 exists as per previous tests
    }

    @When("I delete the plant")
    public void iDeleteThePlant() {
        plantAction.deletePlant(plantAction.getLastCreatedPlantId());
    }

    @When("I DELETE to {string}")
    public void iDELETETo(String endpoint) {
        // Handle generic /api/plants/{id}
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
        // If we randomized the name, verify against the actual name used
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
        // endpoint is currently hardcoded in action as /api/plants, but checking if it
        // matches expectation
        // In the future we might want to pass endpoint to action if we need flexibility
        plantAction.getPlants(page, size);
    }

    @Then("the response should contain a list of plants and pagination metadata")
    public void theResponseShouldContainAListOfPlantsAndPaginationMetadata() {
        plantAction.verifyPaginationMetadata();
    }

    @When("I search for plants with name {string} and page={int}&size={int}")
    public void iSearchForPlantsWithNameAndPageSize(String name, int page, int size) {
        plantAction.getPlants(name, page, size);
    }

    @Then("the response should contain plants filtering by name {string}")
    public void theResponseShouldContainPlantsFilteringByName(String name) {
        plantAction.verifyPlantListContainsName(name);
    }
}
