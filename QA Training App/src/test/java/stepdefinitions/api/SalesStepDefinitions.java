package stepdefinitions.api;

import java.util.HashMap;
import java.util.Map;

import actions.AuthenticationActions;
import actions.CategoryActions;
import actions.PlantActions;
import actions.SalesAction;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;
import net.thucydides.model.util.EnvironmentVariables;

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
    private int initialStock;
    private int currentQuantityBeforeSale;
    private int categoryId;
        private int initialQuantity;


    @Given("admin is authenticated")
    public void admin_is_authenticated() {
        authenticationActions.authenticateAsAdmin();
        String token = Serenity.sessionVariableCalled("authToken");
        salesAction.setToken(token);
    }
    @Given("plant exists with sufficient stock")
    public void plant_exists_with_sufficient_stock() {
        // First, look for an existing plant with at least 10 units
        Integer existingPlantId = plantActions.findExistingPlantWithStock();

        if (existingPlantId != null && existingPlantId > 0) {
            plantId = existingPlantId;
            initialStock = plantActions.getPlantQuantity(plantId);
            System.out.println("Reusing existing plant ID: " + plantId + " with stock: " + initialStock);
        } else {
            // Fallback: Create a category first since no suitable plant was found
            categoryActions.createCategory("Cat_" + (System.currentTimeMillis() % 10000));
            Integer lastId = categoryActions.getLastCreatedCategoryId();
            categoryId = (lastId != null) ? lastId : 0;

            if (categoryId == 0) {
                throw new RuntimeException("Fallback failed: Could not create category. Status: "
                        + categoryActions.getLastResponseStatusCode());
            }

            initialStock = 50;
            Map<String, Object> body = new HashMap<>();
            body.put("name", "Fallback_" + (System.currentTimeMillis() % 10000));
            body.put("price", 20.0);
            body.put("quantity", initialStock);

            
plantActions.createPlantAndStoreId(categoryId, body);
            plantId = plantActions.getLastCreatedPlantId();

            if (plantId == 0) {
                throw new RuntimeException("Fallback failed: Could not create plant in category " + categoryId
                        + ". Status: " + plantActions.getLastResponseStatusCode());
            }
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
        plantActions.getPlant(plantId);
    }

     @Then("the plant quantity should be reduced by {int}")
    public void the_plant_quantity_should_be_reduced_by(int reductionAmount) {
        // Get current plant quantity after sale
        int currentQuantity = plantActions.getPlantQuantity(plantId);
        int expectedQuantity = initialQuantity - reductionAmount;
        
        System.out.println("=== QUANTITY VERIFICATION ===");
        System.out.println("Initial quantity: " + initialQuantity);
        System.out.println("Reduction amount: " + reductionAmount);
        System.out.println("Expected quantity: " + expectedQuantity);
        System.out.println("Actual quantity: " + currentQuantity);
        System.out.println("=============================");
        
        if (currentQuantity != expectedQuantity) {
            throw new AssertionError(
                "Expected plant quantity: " + expectedQuantity + 
                ", but got: " + currentQuantity
            );
        }
        
        // Cleanup - delete the test plant
        cleanupTestData();
    }
    private void cleanupTestData() {
        if (plantId != 0) {
            try {
                plantActions.deletePlant(plantId);
                System.out.println("Cleaned up test plant ID: " + plantId);
            } catch (Exception e) {
                System.out.println("Warning: Failed to cleanup plant " + plantId + ": " + e.getMessage());
            }
        }
    }
}