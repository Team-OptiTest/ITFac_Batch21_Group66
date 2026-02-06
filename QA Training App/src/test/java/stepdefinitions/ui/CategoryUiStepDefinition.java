package stepdefinitions.ui;

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
    
    @Given("the user is logged in as an admin user")
    public void theUserIsLoggedInAsAnAdminUser() {
        loginPage.loginAsAdmin();
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

    @When("the user clicks the Add a category button")
    public void theUserClicksTheAddCategoryButton() {
        categoryPage.clickAddCategoryButton();
    }

    @When("the user fills in the category name with {string}")
    public void theUserFillsInTheCategoryNameWith(String categoryName) {
        categoryPage.fillCategoryName(categoryName);
    }

    @When("the user clicks on the Save button")
    public void theUserClicksTheSaveButton() {
        categoryPage.clickSaveButton();
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

}