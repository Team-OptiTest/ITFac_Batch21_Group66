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
}
