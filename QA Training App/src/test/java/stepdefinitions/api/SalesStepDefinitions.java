package stepdefinitions.api;

import actions.AuthenticationActions;
import actions.CategoryActions;
import actions.PlantActions;
import actions.SalesAction;
import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;
import net.thucydides.model.util.EnvironmentVariables;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class SalesStepDefinitions {

    @Steps
    PlantActions plantActions;

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
    private int saleId;

    @Given("admin is authenticated")
    public void admin_is_authenticated() {
        // Use central authentication action
        authenticationActions.authenticateAsAdmin();
        
        // Retrieve token from session (AuthenticationActions puts it there)
        String token = Serenity.sessionVariableCalled("authToken");
        
        // Propagate token to other actions
        plantActions.setToken(token);
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
        
        plantActions.createPlant(categoryId, body);
        
        plantId = plantActions.getLastCreatedPlantId();
        
        if (plantId == 0) {
             throw new RuntimeException("Failed to create plant in category " + categoryId 
                 + ". Status: " + plantActions.getLastResponseStatusCode());
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
        plantActions.getPlant(plantId);
        plantActions.verifyStatusCode(200);
        
        int expectedStock = initialStock - quantitySold;
        net.serenitybdd.rest.SerenityRest.then().body("quantity", equalTo(expectedStock));
        
        // Cleanup
        plantActions.deletePlant(plantId);
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
            plantActions.deletePlant(plantId);
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
            plantActions.deletePlant(plantId);
            plantId = 0; // Reset to avoid double deletion
        }
    }

    @Given("a sale exists with a known valid saleId")
    public void a_sale_exists_with_a_known_valid_sale_id() {
        plant_exists_with_sufficient_stock();
        admin_creates_sale();
        sale_created_successfully();
        saleId = salesAction.getLastCreatedSaleId();
    }

    @When("admin deletes the sale with valid saleId")
    public void admin_deletes_the_sale_with_valid_sale_id() {
        salesAction.deleteSale(saleId);
    }

    @Then("the sale should be deleted successfully with status {int}")
    public void the_sale_should_be_deleted_successfully_with_status_custom(int statusCode) {
        salesAction.verifyStatusCode(statusCode);
    }

    @Then("the deleted sale should not be retrievable")
    public void the_deleted_sale_should_not_be_retrievable() {
        salesAction.getSaleById(saleId);
        salesAction.verifyStatusCode(404);
        
        // Cleanup plant
        if (plantId != 0) {
            plantActions.deletePlant(plantId);
            plantId = 0;
        }
    }

    @Given("user is authenticated")
    public void user_is_authenticated() {
        authenticationActions.authenticateUser();
        String token = Serenity.sessionVariableCalled("authToken");
        plantActions.setToken(token);
        salesAction.setToken(token);
    }

    @When("user retrieves all sales")
    public void user_retrieves_all_sales() {
        salesAction.getAllSales();
    }

    @When("user retrieves the sale with valid saleId")
    public void user_retrieves_the_sale_with_valid_sale_id() {
        salesAction.getSaleById(saleId);
    }

    @Then("the sale details should be returned successfully")
    public void the_sale_details_should_be_returned_successfully() {
        salesAction.verifySaleReturned(saleId);
        
        // Cleanup plant
        if (plantId != 0) {
            plantActions.deletePlant(plantId);
            plantId = 0;
        }
    }
}
