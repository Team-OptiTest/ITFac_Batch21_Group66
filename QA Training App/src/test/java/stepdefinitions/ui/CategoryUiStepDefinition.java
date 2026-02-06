package stepdefinitions.ui;

import pages.AddCategoryPage;
import pages.CategoryPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;
import pages.LoginPage;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryUiStepDefinition {
    
    @Steps
    LoginPage loginPage;
    
    @Steps
    CategoryPage categoryPage;

    @Steps
    AddCategoryPage addCategoryPage;
    
    @Given("the user is logged in as an admin user")
    public void theUserIsLoggedInAsAnAdminUser() {
        loginPage.loginAsAdmin();
    }

    @Given("the user is logged in as a user")
    public void theUserIsLoggedInAsAUser() {
        loginPage.loginAsUser();
    }
    
    @When("the user navigates to the categories page")
    public void theUserNavigatesToTheCategoriesPage() {
        categoryPage.navigateToCategoriesPage();
    }
    
    @Then("the user should see the {string} button")
    public void theUserShouldSeeTheButton(String buttonText) {
        assertThat(categoryPage.isAddCategoryButtonVisible())
            .as("\"Add a category\" button should be visible to admin user")
            .isTrue();
    }

    @Then("the user should not see the {string} button")
    public void theUserShouldNotSeeTheAddCategoryButton(String buttonText) {
        assertThat(categoryPage.isAddCategoryButtonNotVisible())
            .as("\"Add a category\" button should not be visible to regular user")
            .isTrue();
    }

    @When("the user clicks the Add a category button")
    public void theUserClicksTheAddCategoryButton() {
        categoryPage.clickAddCategoryButton();
    }

    @When("the user fills in the category name with {string}")
    public void theUserFillsInTheCategoryNameWith(String categoryName) {
        addCategoryPage.fillCategoryName(categoryName);
    }

    @When("the user leaves the category name field empty")
    public void theUserLeavesTheCategoryNameFieldEmpty() {
        addCategoryPage.fillCategoryName("");
    }

    @When("the user clicks on the Save button")
    public void theUserClicksTheSaveButton() {
        addCategoryPage.clickSaveButton();
    }

    @Then("the user should see a success message confirming the category was created")
    public void theUserShouldSeeASuccessMessageConfirmingTheCategoryWasCreated() {
        assertThat(categoryPage.isSuccessMessageDisplayed())
            .as("Success message should be displayed after creating a category")
            .isTrue();
    }

    @Then("the new category {string} should be listed on the categories page")
    public void theNewCategoryShouldBeListedOnTheCategoriesPage(String categoryName) {
        assertThat(categoryPage.isCategoryVisibleInList(categoryName))
            .as("Newly created category should be visible in the categories list")
            .isTrue();
    }

    @Then("the user should see a validation error message indicating that the category name is required")
    public void theUserShouldSeeTheCategoryNameIsRequiredMessage() {
        assertThat(addCategoryPage.isValidationErrorMessageDisplayed())
            .as("Validation error message should be displayed when category name is empty")
            .isTrue();
    }

    @Then("the user should see a list of categories displayed")
    public void theUserShouldSeeAListOfCategoriesDisplayed() {
        assertThat(categoryPage.isCategoryListDisplayed())
            .as("Categories list should be displayed on the page")
            .isTrue();
    }

}