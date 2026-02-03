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
    
    // If creation fails (maybe duplicate), that's OK for our test setup
    System.out.println("Setup: Category '" + categoryName + "' should exist now");
}
@When("the admin attempts to create another category with name {string}")
public void theAdminAttemptsToCreateAnotherCategoryWithName(String categoryName) {
    // Try to create the same category again
    categoryActions.createCategory(categoryName);
}
    @When("the admin creates a category with valid name {string}")
    public void theAdminCreatesACategoryWithName(String categoryName) {
        categoryActions.createCategoryWithValidData(categoryName);
    }
    @When("the admin requests categories summary")
public void theAdminRequestsCategoriesSummary() {
    categoryActions.getCategoriesSummary();
}

    @Then("the API should return {int} OK")
    public void theAPIShouldReturnOK(int expectedStatusCode) {
        int actualStatusCode = categoryActions.getLastResponseStatusCode();
        
        assertThat(actualStatusCode)
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

    @Then("the category creation should fail with validation error")
    public void theCategoryCreationShouldFailWithValidationError() {
        int statusCode = categoryActions.getLastResponseStatusCode();
            String responseBody = categoryActions.getLastResponseBody();
    
            System.out.println("\n=== VERIFYING VALIDATION ERROR ===");
            System.out.println("Status code: " + statusCode);
            System.out.println("Response: " + responseBody);
    
    // @API_Category_Create_004 - Verify that creating a category with invalid data fails
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
    //@API_Category_Create_005 - Verify that the error message contains specific text
    }

    @Then("the user should be denied permission to create a category")
    public void theUserShouldBeDeniedPermissionToCreateACategory() {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("Category creation should not succeed")
            .isIn(403, 404);
    }

    @When("the admin deletes an existing category with ID {long}")
    public void theAdminDeletesAnExistingCategoryWithId(long categoryId) {
        categoryActions.deleteCategoryById(categoryId);
    }

    @Then("the category should be deleted successfully")
    public void theCategoryShouldBeDeletedSuccessfully() {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("Category deletion should succeed")
            .isIn(204, 205);
    }
    
}