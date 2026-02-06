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
        System.out.println("Setup: Category '" + categoryName + "' should exist now");
    }

    @Given("the user has a valid JWT token")
    public void theUserHasAValidJWTToken() {
        authenticationActions.authenticateAsAdmin();
    }

    @Given("the user has an expired JWT token:")
    public void theUserHasAnExpiredJWTToken(String expiredToken) {
        System.out.println("Expired token provided: " + expiredToken.substring(0, 50) + "...");
    }

    @Given("a category with ID {int} exists")
    public void aCategoryWithIDExists(int categoryId) {
        System.out.println("Assuming category with ID " + categoryId + " exists for testing");
    }

    @When("the admin attempts to create another category with name {string}")
    public void theAdminAttemptsToCreateAnotherCategoryWithName(String categoryName) {
        categoryActions.createCategory(categoryName);
    }

    @When("the admin creates a category with valid name {string}")
    public void theAdminCreatesACategoryWithName(String categoryName) {
        categoryActions.createCategory(categoryName);
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
        categoryActions.deleteCategoryById((int) categoryId);
    }

    @When("the admin requests categories summary")
    public void theAdminRequestsCategoriesSummary() {
        categoryActions.getCategoriesSummary();
    }

    @When("the user requests categories summary")
    public void theUserRequestsCategoriesSummary() {
        categoryActions.getCategoriesSummary();
    }

    @When("the user requests categories summary with invalid token")
    public void theUserRequestsCategoriesSummaryWithInvalidToken() {
        categoryActions.getCategoriesSummary();
    }

    @When("a request is made to get category with ID {int} without JWT token")
    public void aRequestIsMadeToGetCategoryWithIDWithoutJWTToken(int categoryId) {
        categoryActions.getCategoryById(categoryId);
    }

    @When("a request is made to get categories without JWT token")
    public void aRequestIsMadeToGetCategoriesWithoutJWTToken() {
        categoryActions.getCategoriesSummary();
    }

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
        // Use SerenityRest.lastResponse() to support both Category and Plant API calls
        String responseBody = net.serenitybdd.rest.SerenityRest.lastResponse().getBody().asString();

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

    @When("the user deletes that category")
    public void theUserDeletesThatCategory() {
        categoryActions.deleteCategoryById(categoryActions.getLastCreatedCategoryId());
    }

    @When("the admin deletes a category with non-existent ID")
    public void theAdminDeletesACategoryWithNonExistentId() {
        categoryActions.deleteCategoryWithNonExistentId();
    }

    @Then("the category deletion should fail")
    public void theCategoryDeletionShouldFail() {
        assertThat(categoryActions.getLastResponseStatusCode())
                .as("Category deletion should not succeed")
                .isIn(403, 404);
    }

    @When("the admin fetches the category list")
    public void theAdminFetchesTheCategoryList() {
        categoryActions.getCategoryList();
    }

    @When("the user fetches the category list")
    public void theUserFetchesTheCategoryList() {
        categoryActions.getCategoryList();
    }

    @Then("the category list should be retrieved successfully")
    public void theCategoryListShouldBeRetrievedSuccessfully() {
        assertThat(categoryActions.getLastResponseStatusCode())
                .as("the category list should be retrieved successfully")
                .isEqualTo(200);
    }

    @When("the user searches for categories with name {string}")
    public void theUserSearchesForCategoriesWithName(String categoryName) {
        categoryActions.searchCategories(categoryName, null);
    }

    @When("the admin searches for categories with name {string}")
    public void theAdminSearchesForCategoriesWithName(String categoryName) {
        categoryActions.searchCategories(categoryName, null);
    }

    @Then("the search results should be returned successfully")
    public void theSearchResultsShouldBeReturnedSuccessfully() {
        assertThat(categoryActions.getLastResponseStatusCode())
                .as("Search categories should return HTTP 200")
                .isEqualTo(200);
    }

    @When("the user filters categories by parent ID {string}")
    public void theUserFiltersCategoriesByParentId(String parentId) {
        categoryActions.searchCategories(null, parentId);
    }

    @When("the admin filters categories by parent ID {string}")
    public void theAdminFiltersCategoriesByParentId(String parentId) {
        categoryActions.searchCategories(null, parentId);
    }

    @Then("the filtered categories should be retrieved successfully")
    public void theFilteredCategoriesShouldBeRetrievedSuccessfully() {
        assertThat(categoryActions.getLastResponseStatusCode())
                .as("Filtered categories should be retrieved successfully with HTTP 200")
                .isEqualTo(200);
    }

    @When("the admin updates that category with name {string}")
    public void theAdminUpdatesThatCategoryWithName(String updatedName) {
        categoryActions.updateCategory(categoryActions.getLastCreatedCategoryId(), updatedName);
    }

    @Then("the category should be updated successfully")
    public void theCategoryShouldBeUpdatedSuccessfully() {
        assertThat(categoryActions.getLastResponseStatusCode())
                .as("Category update should succeed with HTTP 200")
                .isEqualTo(200);
    }

    @When("the user updates that category with name {string}")
    public void theUserUpdatesThatCategoryWithName(String updatedName) {
        categoryActions.updateCategory(categoryActions.getLastCreatedCategoryId(), updatedName);
    }

    @When("the admin updates a category with non-existent ID")
    public void theAdminUpdatesACategoryWithNonExistentId() {
        categoryActions.updateCategoryWithNonExistentId();
    }

    @Then("the category update should fail")
    public void theCategoryUpdateShouldFail() {
        assertThat(categoryActions.getLastResponseStatusCode())
                .as("Category update should not succeed")
                .isIn(403, 404);
    }

    @When("the user attempts to view a category with a non-existent ID")
    public void theUserAttemptsToViewCategoryWithNonExistentId() {
        categoryActions.getCategoryWithNonExistentId();
    }

    @When("the admin creates a category with a non-existent parent category ID")
    public void theAdminCreatesACategoryWithNonExistentParentCategoryId() {
        categoryActions.createCategoryWithNonExistentParentId();
    }

    @Then("the API should return {int} Not Found")
    public void theApiShouldReturnNotFound(int expectedStatusCode) {
        // Use SerenityRest.lastResponse() to support both Category and Plant API calls
        assertThat(net.serenitybdd.rest.SerenityRest.lastResponse().getStatusCode())
                .as("API should return " + expectedStatusCode + " status code")
                .isEqualTo(expectedStatusCode);
    }

    @When("the user requests the main categories")
    public void theUserRequestsTheMainCategories() {
        categoryActions.getMainCategories();
    }

    @Then("the response should contain a list of main categories")
    public void theResponseShouldContainAListOfMainCategories() {
        assertThat(categoryActions.responseContainsMainCategoriesList())
                .as("Response should contain a list of main categories")
                .isTrue();
    }
}
