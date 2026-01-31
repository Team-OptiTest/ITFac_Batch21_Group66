package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;
import actions.CategoryActions;
import actions.AuthenticationActions;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryStepDefinitions {

    @Steps
    CategoryActions categoryActions;

    @Steps
    AuthenticationActions authenticationActions;

    @Given("the user is authenticated as admin")
    public void theUserIsAuthenticatedAsAdmin() {
        authenticationActions.authenticateAsAdmin();
    }

    @When("the admin creates a category with valid name {string}")
    public void theAdminCreatesACategoryWithName(String categoryName) {
        categoryActions.createCategoryWithValidData(categoryName);
    }

    @Then("the category should be created successfully")
    public void theCategoryShouldBeCreatedSuccessfully() {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("Category creation should succeed")
            .isIn(200, 201);
    }

    @When("the admin creates a category with less than 3 characters {string}")
    public void theAdminCreatesACategoryWithLessCharacters(String categoryName) {
        categoryActions.createCategory(categoryName);
    }

    @When("the admin creates a category with more than 10 characters {string}")
    public void theAdminCreatesACategoryWithMoreCharacters(String categoryName) {
        categoryActions.createCategory(categoryName);
    }

    @Then("the category should not be created")
    public void theCategoryShouldNotBeCreated() {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("Category creation should not succeed")
            .isIn(400, 499);
    }

}