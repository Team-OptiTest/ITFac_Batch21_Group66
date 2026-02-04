package stepdefinitions.api;

import java.util.Map;

import actions.PlantAction;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;

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
    @Given("the user is authenticated with ROLE_USER")
public void theUserIsAuthenticatedWithROLE_USER() {
    aRegularUserIsAuthenticated(); 
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
        String[] parts = endpoint.split("/");
        int id = Integer.parseInt(parts[parts.length - 1]);
        plantAction.deletePlant(id);
    }

@When("I GET to {string} with query params {string}")
public void iGETToWithQueryParams(String endpoint, String queryParams) {
    plantAction.getPlantsWithPagination(endpoint, queryParams);
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
    @When("I GET to {string}")
public void iGETTo(String endpoint) {
    plantAction.getRequest(endpoint);
}

@Then("the response should contain inventory statistics")
public void theResponseShouldContainInventoryStatistics() {
    plantAction.verifyInventoryStatistics();
}
@Then("the response should contain a content array")
public void theResponseShouldContainAContentArray() {
    plantAction.verifyResponseHasContentArray();
}

@Then("the response page number should be {int}")
public void theResponsePageNumberShouldBe(int expectedPage) {
    plantAction.verifyPageNumber(expectedPage);
}

@Then("the response page size should be {int}")
public void theResponsePageSizeShouldBe(int expectedSize) {
    plantAction.verifyPageSize(expectedSize);
}
}
