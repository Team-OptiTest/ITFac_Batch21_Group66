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
        body.put("name", data.get("name"));
        body.put("price", Double.parseDouble(data.get("price")));
        body.put("quantity", Integer.parseInt(data.get("quantity")));

        // Extracting category ID from endpoint /api/plants/category/{id}
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
}
