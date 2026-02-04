package stepdefinitions.api;

import java.util.HashMap;
import java.util.Map;

import actions.AuthenticationActions;
import actions.CategoryActions;
import actions.PlantAction;
import actions.SalesAction;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;
import net.thucydides.model.util.EnvironmentVariables;

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
    private int initialStock;
    private int currentQuantityBeforeSale;

    @Given("admin is authenticated")
    public void admin_is_authenticated() {
        authenticationActions.authenticateAsAdmin();
        String token = Serenity.sessionVariableCalled("authToken");
        plantAction.setToken(token);
        salesAction.setToken(token);
    }

    @Given("a plant exists with quantity ≥ {int}")
public void a_plant_exists_with_quantity_greater_than_or_equal(int minQuantity) {
    // Try different category IDs until you find a sub-category
    int[] possibleCategoryIds = {1, 2, 3, 4, 6, 7, 8, 9, 10};
    boolean plantCreated = false;
    
    for (int categoryId : possibleCategoryIds) {
        initialStock = minQuantity + 10;
        
        Map<String, Object> body = new HashMap<>();
        body.put("name", "SalePlant" + (System.currentTimeMillis() % 1000));
        body.put("price", 25.0);
        body.put("quantity", initialStock);
        
        plantAction.createPlant(categoryId, body);
        
        if (plantAction.getLastResponse() != null && 
            plantAction.getLastResponse().getStatusCode() == 201) {
            plantId = plantAction.getLastCreatedPlantId();
            plantCreated = true;
            break;
        }
    }
    
    if (!plantCreated) {
        throw new RuntimeException("Failed to create plant. No valid sub-category found.");
    }
}
    @When("admin gets the plant to note current quantity")
    public void admin_gets_the_plant_to_note_current_quantity() {
        plantAction.getPlant(plantId);
        if (plantAction.getLastResponse() != null) {
            currentQuantityBeforeSale = plantAction.getLastResponse().jsonPath().getInt("quantity");
        }
    }

    @When("admin creates a sale with quantity {int}")
    public void admin_creates_a_sale_with_quantity(int quantity) {
        salesAction.createSaleForPlant(plantId, quantity);
    }

    @Then("the sale should be created with status {int}")
    public void the_sale_should_be_created_with_status(int statusCode) {
        salesAction.verifyStatusCode(statusCode);
    }

    @When("admin gets the plant again")
    public void admin_gets_the_plant_again() {
        plantAction.getPlant(plantId);
    }

    @Then("the plant quantity should be reduced by {int}")
    public void the_plant_quantity_should_be_reduced_by(int reductionAmount) {
        int currentQuantity = plantAction.getLastResponse().jsonPath().getInt("quantity");
        int expectedQuantity = currentQuantityBeforeSale - reductionAmount;
        
        if (currentQuantity != expectedQuantity) {
            throw new AssertionError(
                "Expected plant quantity: " + expectedQuantity + 
                ", but got: " + currentQuantity
            );
        }
        
        cleanupTestData();
    }

    private void cleanupTestData() {
        if (plantId != 0) {
            try {
                plantAction.deletePlant(plantId);
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
    }
}