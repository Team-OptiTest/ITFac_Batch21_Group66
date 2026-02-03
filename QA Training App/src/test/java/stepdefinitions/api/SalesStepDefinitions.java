package stepdefinitions.api;

import actions.AuthenticationActions;
import actions.CategoryActions;
import actions.PlantAction;
import actions.SalesAction;
import io.cucumber.java.en.*;
import io.restassured.path.json.JsonPath;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class SalesStepDefinitions {

    @Steps
    PlantAction plantAction;

    @Steps
    SalesAction salesAction;

    @Steps
    CategoryActions categoryActions;
    
    @Steps
    AuthenticationActions authenticationActions;

    private EnvironmentVariables environmentVariables;
    private int plantId;
    private int quantitySold;
    private int initialStock;
    private int categoryId;

    @Given("admin is authenticated")
    public void admin_is_authenticated() {
        // Use central authentication action
        authenticationActions.authenticateAsAdmin();
        
        // Retrieve token from session (AuthenticationActions puts it there)
        String token = Serenity.sessionVariableCalled("authToken");
        
        // Propagate token to other actions
        plantAction.setToken(token);
        salesAction.setToken(token);
    }

    @Given("plant exists with sufficient stock")
    public void plant_exists_with_sufficient_stock() {
        // Use existing sub-category "Flowering" (ID: 5)
        categoryId = 5;

        initialStock = 50;
        Map<String, Object> body = new HashMap<>();
        // Plant name must be 3-25 chars.
        body.put("name", "Plant " + (System.currentTimeMillis() % 10000));
        body.put("price", 20.0);
        body.put("quantity", initialStock);
        
        plantAction.createPlant(categoryId, body);
        
        plantId = plantAction.getLastCreatedPlantId();
        
        if (plantId == 0) {
             throw new RuntimeException("Failed to create plant in category " + categoryId 
                 + ". Status: " + plantAction.getLastResponseStatusCode());
        }
    }

    @When("admin creates a sale with valid quantity")
    public void admin_creates_sale() {
        quantitySold = 5;
        // Token already set in Given step
        salesAction.createSale(plantId, quantitySold);
    }

    @Then("sale should be created successfully")
    public void sale_created_successfully() {
        salesAction.verifySaleCreatedSuccessfully(quantitySold);
    }

    @Then("plant stock should be reduced accordingly")
    public void plant_stock_reduced() {
        plantAction.getPlant(plantId);
        plantAction.verifyStatusCode(200);
        
        int expectedStock = initialStock - quantitySold;
        net.serenitybdd.rest.SerenityRest.then().body("quantity", equalTo(expectedStock));
        
        // Cleanup
        plantAction.deletePlant(plantId);
        // categoryActions.deleteCategoryById(categoryId); // Do not delete the shared category
    }

    @When("admin creates a sale with quantity {int}")
    public void admin_creates_sale_with_quantity(int quantity) {
        quantitySold = quantity;
        salesAction.createSale(plantId, quantity);
    }

    @Then("sale creation should fail with status {int}")
    public void sale_creation_should_fail_with_status(int statusCode) {
        salesAction.verifyStatusCode(statusCode);
    }

    @Then("error message should be {string}")
    public void error_message_should_be(String message) {
        salesAction.verifyErrorMessage(message);
        
        // Cleanup plant ensures we don't leave data behind even on negative tests
        if (plantId != 0) {
            plantAction.deletePlant(plantId);
        }
    }
    @When("admin creates a sale for plant {int} with quantity {int}")
    public void admin_creates_sale_for_plant_with_quantity(int plantId, int quantity) {
        // Set local fields just in case they are used elsewhere, though not strictly needed for this specific test
        this.plantId = plantId;
        this.quantitySold = quantity;
        
        salesAction.createSale(plantId, quantity);
    }

    @Given("at least one sale exists in the system")
    public void at_least_one_sale_exists_in_the_system() {
        plant_exists_with_sufficient_stock();
        admin_creates_sale();
        sale_created_successfully();
    }

    @When("admin retrieves all sales")
    public void admin_retrieves_all_sales() {
        salesAction.getAllSales();
    }

    @Then("all sales should be returned successfully")
    public void all_sales_should_be_returned_successfully() {
        salesAction.verifySalesListReturned();
        
        // Cleanup the plant created in at_least_one_sale_exists_in_the_system
        if (plantId != 0) {
            plantAction.deletePlant(plantId);
            plantId = 0; // Reset to avoid double deletion
        }
    }
}
