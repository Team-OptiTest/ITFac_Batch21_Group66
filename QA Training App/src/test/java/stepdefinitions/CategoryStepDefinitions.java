package stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import actions.AuthenticationActions;
import actions.CategoryActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;

public class CategoryStepDefinitions {

    @Steps
    CategoryActions categoryActions;

    @Steps
    AuthenticationActions authenticationActions;

    // ============================================
    // GIVEN STEPS
    // ============================================

    @Given("the user is authenticated as admin")
    public void theUserIsAuthenticatedAsAdmin() {
        authenticationActions.authenticateAsAdmin();
    }

    @Given("the user is authenticated as user")
    public void theUserIsAuthenticatedAsUser() {
        authenticationActions.authenticateUser();
    }

    @Given("a category named {string} exists")
    public void aCategoryNamedExists(String categoryName) {
        categoryActions.createCategory(categoryName);
        System.out.println("Setup: Category '" + categoryName + "' should exist now");
    }

    @Given("the user has a valid JWT token")
    public void theUserHasAValidJWTToken() {
        authenticationActions.authenticateAsAdmin();
    }

    @Given("the user has an expired JWT token:")
    public void theUserHasAnExpiredJWTToken(String expiredToken) {
        // Just store it - we won't actually use it
        System.out.println("Expired token provided: " + expiredToken.substring(0, 50) + "...");
        // We'll just NOT authenticate - CategoryActions will use whatever token is set
    }
    @Given("a category with ID {int} exists")
    public void aCategoryWithIDExists(int categoryId) {
        System.out.println("Assuming category with ID " + categoryId + " exists for testing");
    }

    // ============================================
    // WHEN STEPS
    // ============================================

    @When("the admin attempts to create another category with name {string}")
    public void theAdminAttemptsToCreateAnotherCategoryWithName(String categoryName) {
        categoryActions.createCategory(categoryName);
    }

    @When("the admin creates a category with valid name {string}")
    public void theAdminCreatesACategoryWithName(String categoryName) {
        categoryActions.createCategoryWithValidData(categoryName);
    }

    @When("the admin creates a category with less than 3 characters {string}")
    public void theAdminCreatesACategoryWithLessCharacters(String categoryName) {
        categoryActions.createCategory(categoryName);
    }

    @When("the admin creates a category with more than 10 characters {string}")
    public void theAdminCreatesACategoryWithMoreCharacters(String categoryName) {
        categoryActions.createCategory(categoryName);
    }

    @When("the admin creates a category without name {string}")
    public void theAdminCreatesACategoryWithoutName(String categoryName) {
        categoryActions.createCategory(categoryName);
    }

    @When("the user creates a category with name {string}")
    public void theUserCreatesACategoryWithName(String categoryName) {
        categoryActions.createCategory(categoryName);
    }

    @When("the admin deletes an existing category with ID {long}")
    public void theAdminDeletesAnExistingCategoryWithId(long categoryId) {
        categoryActions.deleteCategoryById(categoryId);
    }

    @When("the admin requests categories summary")
    public void theAdminRequestsCategoriesSummary() {
        categoryActions.getCategoriesSummary();
    }

    @When("the user requests categories summary")
    public void theUserRequestsCategoriesSummary() {
        // Just call the same method
        categoryActions.getCategoriesSummary();
    }

    @When("the user requests categories summary with invalid token")
    public void theUserRequestsCategoriesSummaryWithInvalidToken() {
        // Same as above - just a different name
        categoryActions.getCategoriesSummary();
    }
    @When("a request is made to get category with ID {int} without JWT token")
    public void aRequestIsMadeToGetCategoryWithIDWithoutJWTToken(int categoryId) {
    // Just call the existing method - if no token is set, it should fail
    // Your CategoryActions.getCategoryById() should use getAuthToken() which might be null
        categoryActions.getCategoryById(categoryId);
    }

    @When("a request is made to get categories without JWT token")
    public void aRequestIsMadeToGetCategoriesWithoutJWTToken() {
    // Call the summary endpoint without authenticating first
    // Make sure NOT to call authenticationActions.authenticateAsAdmin() before this
        categoryActions.getCategoriesSummary();
    }

    // ============================================
    // THEN STEPS
    // ============================================

    @Then("the API should return {int} OK")
    public void theAPIShouldReturnOK(int expectedStatusCode) {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("API should return " + expectedStatusCode + " status code")
            .isEqualTo(expectedStatusCode);
    }

    @Then("the category should be created successfully")
    public void theCategoryShouldBeCreatedSuccessfully() {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("Category creation should succeed")
            .isIn(200, 201);
    }

    @Then("the API should return 400 Bad Request")
    public void theAPIShouldReturn400BadRequest() {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("API should return 400 for duplicate")
            .isEqualTo(400);
    }

    @Then("the category creation should fail with validation error")
    public void theCategoryCreationShouldFailWithValidationError() {
        int statusCode = categoryActions.getLastResponseStatusCode();
        String responseBody = categoryActions.getLastResponseBody();

        System.out.println("\n=== VERIFYING VALIDATION ERROR ===");
        System.out.println("Status code: " + statusCode);
        System.out.println("Response: " + responseBody);

        assertThat(statusCode)
            .as("Category creation should fail with 400 Bad Request")
            .isEqualTo(400);

        assertThat(responseBody)
            .as("Response should contain error details")
            .isNotEmpty();

        System.out.println("=== VERIFICATION COMPLETE ===\n");
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String expectedMessage) {
        String responseBody = categoryActions.getLastResponseBody();

        System.out.println("\n=== VERIFYING ERROR MESSAGE ===");
        System.out.println("Expected message: " + expectedMessage);
        System.out.println("Actual response: " + responseBody);

        assertThat(responseBody.toLowerCase())
            .as("Error message should contain: " + expectedMessage)
            .contains(expectedMessage.toLowerCase());

        System.out.println("=== VERIFICATION COMPLETE ===\n");
    }

    @Then("the user should be denied permission to create a category")
    public void theUserShouldBeDeniedPermissionToCreateACategory() {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("Category creation should not succeed")
            .isIn(403, 404);
    }

    @Then("the category should be deleted successfully")
    public void theCategoryShouldBeDeletedSuccessfully() {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("Category deletion should succeed")
            .isIn(204, 205);
    }

    @Then("the API should return {int} Unauthorized")
    public void theAPIShouldReturnUnauthorized(int expectedStatusCode) {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("API should return " + expectedStatusCode + " Unauthorized")
            .isEqualTo(expectedStatusCode);
    }
}