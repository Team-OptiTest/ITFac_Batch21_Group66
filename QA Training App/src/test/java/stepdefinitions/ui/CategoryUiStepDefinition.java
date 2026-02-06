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
}