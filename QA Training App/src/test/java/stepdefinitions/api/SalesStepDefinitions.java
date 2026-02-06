package stepdefinitions.api;

import java.util.HashMap;
import java.util.Map;

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
    private int currentQuantityBeforeSale;
    private int categoryId;
    private int saleId;
        private int initialQuantity;


    @Given("admin is authenticated")
    public void admin_is_authenticated() {
        // Use central authentication action
        authenticationActions.authenticateAsAdmin();

        // Token is automatically available via Serenity session
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

        // Cleanup - only delete if we created the plant (categoryId != 0)
        if (categoryId != 0) {
            plantActions.deletePlant(plantId);
            categoryActions.deleteCategoryById(categoryId);
            categoryId = 0;
        }
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
        if (categoryId != 0) {
            if (plantId != 0) {
                plantActions.deletePlant(plantId);
                plantId = 0;
            }
            categoryActions.deleteCategoryById(categoryId);
            categoryId = 0;
        }
    }

    @When("admin creates a sale for plant {int} with quantity {int}")
    public void admin_creates_sale_for_plant_with_quantity(int plantId, int quantity) {
        // Set local fields just in case they are used elsewhere, though not strictly
        // needed for this specific test
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
        if (categoryId != 0) {
            if (plantId != 0) {
                plantActions.deletePlant(plantId);
                plantId = 0; // Reset to avoid double deletion
            }
            categoryActions.deleteCategoryById(categoryId);
            categoryId = 0;
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

    @Then("the deleted sale should not be retrievable")
    public void the_deleted_sale_should_not_be_retrievable() {
        salesAction.getSaleById(saleId);
        salesAction.verifyStatusCode(404);
        cleanupTestData();
    }

    private void cleanupTestData() {
        if (plantId != 0) {
            try {
                // Cleanup plant
                if (categoryId != 0) {
                    if (plantId != 0) {
                        plantActions.deletePlant(plantId);
                        System.out.println("Cleaned up test plant ID: " + plantId);
                    }
                }
            } catch (Exception e) {
                System.out.println("Warning: Failed to cleanup plant " + plantId + ": " + e.getMessage());
                plantId = 0;
            }
            categoryActions.deleteCategoryById(categoryId);
            categoryId = 0;
        }
    }

    @Given("user is authenticated")
    public void user_is_authenticated() {
        authenticationActions.authenticateUser();
        String token = Serenity.sessionVariableCalled("authToken");
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
        if (categoryId != 0) {
            if (plantId != 0) {
                plantActions.deletePlant(plantId);
                plantId = 0;
            }
            categoryActions.deleteCategoryById(categoryId);
            categoryId = 0;
        }
    }

    @When("user attempts to retrieve a sale with non-existent ID")
    public void user_attempts_to_retrieve_sale_with_non_existent_id() {
        salesAction.getSaleWithNonExistentId();
    }

    @Then("the API should return {int} Not Found with message {string}")
    public void the_api_should_return_not_found_with_message(int expectedStatusCode, String expectedMessage) {
        salesAction.verifyStatusCode(expectedStatusCode);
        salesAction.verifyErrorMessage(expectedMessage);
    }

    @Given("a valid sale exists in the system")
    public void a_valid_sale_exists_in_the_system() {
        plant_exists_with_sufficient_stock();
        admin_creates_sale();
        sale_created_successfully();
        saleId = salesAction.getLastCreatedSaleId();
    }

    @When("an unauthenticated user attempts to retrieve the sale")
    public void an_unauthenticated_user_attempts_to_retrieve_the_sale() {
        // Clear any existing token to simulate unauthenticated request
        salesAction.setToken(null);
        salesAction.getSaleByIdWithoutAuth(saleId);
    }

    @Then("the API should return {int} Unauthorized")
    public void the_api_should_return_unauthorized(int expectedStatusCode) {
        salesAction.verifyStatusCode(expectedStatusCode);
        salesAction.verifyUnauthorizedError();

        // Cleanup plant
        if (categoryId != 0) {
            if (plantId != 0) {
                plantActions.deletePlant(plantId);
                plantId = 0;
            }
            categoryActions.deleteCategoryById(categoryId);
            categoryId = 0;
        }
    }

    @Given("multiple sales exist in the system")
    public void multiple_sales_exist_in_the_system() {
        // Authenticate as admin to create sales
        authenticationActions.authenticateAsAdmin();
        String adminToken = Serenity.sessionVariableCalled("authToken");
        salesAction.setToken(adminToken);

        // Ensure at least one plant exists (reuses existing action)
        plantActions.ensureAtLeastOnePlantExists();

        // Fetch available plants and build a list of IDs (handle paged and non-paged responses)
        String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                .from(net.thucydides.model.environment.SystemEnvironmentVariables.createEnvironmentVariables())
                .getProperty("api.base.url");

        io.restassured.response.Response response = net.serenitybdd.rest.SerenityRest.given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(baseUrl + "/api/plants");

        java.util.List<Integer> plantIds = response.jsonPath().getList("content.id", Integer.class);
        if (plantIds == null || plantIds.isEmpty()) {
            plantIds = response.jsonPath().getList("id", Integer.class);
        }
        if (plantIds == null || plantIds.isEmpty()) {
            // fallback: extract ids from root objects
            java.util.List<java.util.Map<String, Object>> plants = response.jsonPath().getList("$");
            plantIds = new java.util.ArrayList<>();
            if (plants != null) {
                for (java.util.Map<String, Object> p : plants) {
                    Object idObj = p.get("id");
                    if (idObj instanceof Number) {
                        plantIds.add(((Number) idObj).intValue());
                    } else if (idObj instanceof String) {
                        try {
                            plantIds.add(Integer.parseInt((String) idObj));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }

        if (plantIds == null || plantIds.isEmpty()) {
            throw new IllegalStateException("No plants available to create sales against");
        }

        // Create at least 8 sales using round-robin across available plants.
        // Create small quantity (1) per sale to avoid depleting stock.
        int required = 8;
        int created = 0;
        int idx = 0;
        int safety = 0;
        while (created < required && safety < plantIds.size() * 20) {
            int pid = plantIds.get(idx % plantIds.size());
            int available = plantActions.getPlantQuantity(pid);
            if (available >= 1) {
                salesAction.createSale(pid, 1);
                // Verify creation succeeded
                salesAction.verifyStatusCode(201);
                created++;
            }
            idx++;
            safety++;
        }

        if (created < required) {
            throw new IllegalStateException("Could not create required number of sales; created=" + created);
        }

        // Switch to user for retrieval actions
        authenticationActions.authenticateUser();
        String userToken = Serenity.sessionVariableCalled("authToken");
        salesAction.setToken(userToken);
    }

    @When("user retrieves sales page with params {string}")
    public void user_retrieves_sales_page_with_params(String queryParams) {
        salesAction.getSalesWithPagination("/api/sales/page", queryParams);
    }

    @Then("the response should contain pagination metadata for page {int} and size {int}")
    public void the_response_should_contain_pagination_metadata_for_page_and_size(int page, int size) {
        // Status 200
        salesAction.verifyStatusCode(200);

        // Reuse generic pagination verifications from PlantActions
        plantActions.verifyPaginationMetadata();
        plantActions.verifyPageNumber(page);
        plantActions.verifyPageSize(size);
        plantActions.verifyResponseHasContentArray();
    }

    @Then("the response content should be sorted by {string} {string}")
    public void the_response_content_should_be_sorted_by(String field, String direction) {
        java.util.List<String> values = net.serenitybdd.rest.SerenityRest.lastResponse()
                .jsonPath()
                .getList("content." + field, String.class);

        if (values == null || values.size() <= 1) {
            return; // nothing to verify
        }

        boolean descending = "desc".equalsIgnoreCase(direction);

        for (int i = 0; i < values.size() - 1; i++) {
            String a = values.get(i);
            String b = values.get(i + 1);

            // Try parse as ISO dates first, fallback to string compare
            try {
                java.time.Instant ia = java.time.Instant.parse(a);
                java.time.Instant ib = java.time.Instant.parse(b);
                if (descending && ia.isBefore(ib)) {
                    throw new AssertionError("List is not sorted desc by " + field);
                }
                if (!descending && ia.isAfter(ib)) {
                    throw new AssertionError("List is not sorted asc by " + field);
                }
            } catch (Exception ex) {
                int cmp = a.compareTo(b);
                if (descending && cmp < 0) {
                    throw new AssertionError("List is not sorted desc by " + field);
                }
                if (!descending && cmp > 0) {
                    throw new AssertionError("List is not sorted asc by " + field);
                }
            }
        }
    }
}
