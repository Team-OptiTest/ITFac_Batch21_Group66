package stepdefinitions.ui;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import actions.AuthenticationActions;
import actions.CategoryActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import pages.AddCategoryPage;
import pages.CategoryPage;
import pages.LoginPage;

public class CategoryUiStepDefinitions {

    @Steps
    LoginPage loginPage;

    @Steps
    CategoryPage categoryPage;

    @Steps
    AddCategoryPage addCategoryPage;

    @Steps
    AuthenticationActions authenticationActions;

    @Steps
    CategoryActions categoryActions;

    @Given("the user is logged in as an admin user")
    public void theUserIsLoggedInAsAnAdminUser() {
        loginPage.loginAsAdmin();
    }

    @Given("the user is logged in as a user")
    public void theUserIsLoggedInAsAUser() {
        loginPage.loginAsUser();
    }

    @Given("no categories exist in the database")
    public void noCategoriesExistInTheDatabase() {
        authenticationActions.authenticateAsAdmin();
        categoryActions.deleteAllCategories();
    }

    @When("the user navigates to the categories page")
    public void theUserNavigatesToTheCategoriesPage() {
        categoryPage.navigateToCategoriesPage();
    }

    @When("the user navigates to the add categories page")
    public void theUserNavigatesToTheAddCategoriesPage() {
        categoryPage.navigateToAddCategoryPage();
    }

    @Then("the user searches for {string} in the categories page")
    public void theUserSearchesForInTheCategoriesPage(String searchTerm) {
        categoryPage.searchCategory(searchTerm);
    }

    @Then("the user should see {string} in the search results")
    public void theUserShouldSeeInTheSearchResults(String expectedText) {
        assertThat(categoryPage.isCategoryVisibleInList(expectedText))
                .as("Expected text should be visible in the search results")
                .isTrue();
    }

    @Then("the user should not see {string} in the search results")
    public void theUserShouldNotSeeInTheSearchResults(String unexpectedText) {
        assertThat(categoryPage.isCategoryNotVisibleInList(unexpectedText))
                .as("Unexpected text should not be visible in the search results")
                .isTrue();
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

    @When("the user selects {string} as parent category")
    public void theUserSelectsParentCategoryAsTheParentCategory(String parentCategory) {
        addCategoryPage.selectParentCategory(parentCategory);
    }

    @When("the user clicks on the parent category filter dropdown")
    public void theUserClicksOnTheParentCategoryFilterDropdown() {
        categoryPage.clickParentCategoryFilterDropdown();
    }

    @When("the user selects {string} from the dropdown")
    public void theUserSelectsFromTheDropdown(String option) {
        categoryPage.selectFromDropdown(option);
    }

    @Then("the user should see {string} in the filtered results")
    public void theUserShouldSeeInTheFilteredResults(String expectedText) {
        assertThat(categoryPage.isCategoryVisibleInList(expectedText))
                .as("Expected text should be visible in the filtered results")
                .isTrue();
    }

    @When("the user leaves the category name field empty")
    public void theUserLeavesTheCategoryNameFieldEmpty() {
        addCategoryPage.fillCategoryName("");
    }

    @When("the user selects {string} as the parent category")
    public void theUserSelectsParentCategory(String parentName) {
        addCategoryPage.selectParentCategory(parentName);
    }

    @When("the user clicks on the Save button")
    public void theUserClicksTheSaveButton() {
        addCategoryPage.clickSaveButton();
    }

    @When("the user clicks on the search input field")
    public void theUserClicksOnTheSearchInputField() {
        categoryPage.clickSearchInputField();
    }

    @When("the user enters {string} into the search input field")
    public void theUserEntersIntoTheSearchInputField(String searchTerm) {
        categoryPage.enterSearchTerm(searchTerm);
    }

    @When("the user clicks the search button")
    public void theUserClicksTheSearchButton() {
        categoryPage.clickSearchButton();
    }

    @When("the user searches for a category that does not exist")
    public void theUserSearchesForACategoryThatDoesNotExist() {
        String searchTerm = "NoMatch_" + UUID.randomUUID().toString().substring(0, 8);
        categoryPage.searchCategory(searchTerm);
    }

    @When("the user navigates directly to the add category page")
    public void navigateToAddCategoryPageDirectly() {
        categoryPage.navigateToAddCategoryPageDirectly();
    }

    @Then("the message {string} should be displayed in the table body")
    public void theMessageShouldBeDisplayedInTheTableBody(String expectedMessage) {
        assertThat(categoryPage.isMessageDisplayedInTableBody(expectedMessage))
                .as("Expected table body message should be displayed: " + expectedMessage)
                .isTrue();
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

    @Then("the user is redirected from the category page")
    public void userIsRedirectedFromCategoryPage() {
        assertThat(categoryPage.isRedirectedFromAddCategoryPage()).isTrue();
    }

    @Then("category access denied message is displayed")
    public void categoryAccessDeniedMessageIsDisplayed() {
        assertThat(categoryPage.isAccessDeniedMessageDisplayed()).isTrue();
    }

}
