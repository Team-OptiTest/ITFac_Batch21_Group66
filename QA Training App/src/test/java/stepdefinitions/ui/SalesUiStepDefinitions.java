package stepdefinitions.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import pages.LoginPage;
import pages.SalesPage;
import pages.SellPlantPage;

public class SalesUiStepDefinitions {

    @Steps
    LoginPage loginPage;

    @Steps
    SalesPage salesPage;

    @Steps
    SellPlantPage sellPlantPage;


    @Given("the user is logged in as an admin")
    public void theUserIsLoggedInAsAnAdmin() {
        loginPage.loginAsAdmin();
    }

    @Given("the user is logged in as regular user")
    public void theUserIsLoggedInAsARegularUser() {
        loginPage.loginAsUser();
    }

    @When("the user navigates to the sales page")
    public void theUserNavigatesToTheSalesPage() {
        salesPage.navigateToSalesPage();
    }

    //Sales page navigation
    @Given("the user is on the Sales page")
    public void theUserIsOnTheSalesPage() {
        salesPage.navigateToSalesPage();
    }

    //Direct navigation to sell plant page
    @When("the user navigates directly to the sell plant page")
    public void theUserNavigatesDirectlyToTheSellPlantPage() {
        salesPage.navigateToSellPlantPage();
    }

    @Then("the user should be able to see the {string} button")
    public void theUserShouldSeeTheSellPlantButton(String buttonText) {
        assertThat(salesPage.isSellPlantButtonVisible())
                .as(buttonText + " button should be visible for admin")
                .isTrue();
    }

    //Check Sell Plant button is NOT visible
    @Then("the Sell Plant button should not be visible")
    public void theSellPlantButtonShouldNotBeVisible() {
        assertThat(salesPage.isSellPlantButtonNotVisible())
                .as("Sell Plant button should not be visible to regular user")
                .isTrue();
    }

    //Check redirect from sell plant page
    @Then("the user is redirected to 403 page or dashboard")
    public void theUserIsRedirectedTo403PageOrDashboard() {
        assertThat(salesPage.isRedirectedFromSellPlantPage())
                .as("User should be redirected from /ui/sales/new page")
                .isTrue();
    }

    //Check access denied message
    @Then("access denied message is displayed")
    public void accessDeniedMessageIsDisplayed() {
        assertThat(salesPage.isAccessDeniedMessageDisplayed())
                .as("Access denied message should be displayed")
                .isTrue();
    }

    //Check sales table is displayed
    @Then("the sales table should be displayed")
    public void theSalesTableShouldBeDisplayed() {
        assertThat(salesPage.isSalesTableDisplayed())
                .as("Sales table should be displayed")
                .isTrue();
    }

        @Then("the Sell Plant page should be displayed")
    public void theSellPlantPageShouldBeDisplayed() {
        assertThat(sellPlantPage.isSellPlantPageDisplayed())
                .as("Sell Plant page should be displayed for admin")
                .isTrue();
    }

    @Then("the plant dropdown should be visible")
    public void thePlantDropdownShouldBeVisible() {
        assertThat(sellPlantPage.isPlantDropdownDisplayed())
                .as("Plant dropdown should be visible")
                .isTrue();
    }

    @Then("the quantity field should be visible")
    public void theQuantityFieldShouldBeVisible() {
        assertThat(sellPlantPage.isQuantityFieldDisplayed())
                .as("Quantity field should be visible")
                .isTrue();
    }

    @When("the user selects a plant from the dropdown")
    public void theUserSelectsAPlantFromTheDropdown() {
        sellPlantPage.selectFirstAvailablePlant();
    }

    @When("the user enters a valid quantity")
    public void theUserEntersAValidQuantity() {
        sellPlantPage.enterQuantity("1");
    }

    @When("the user clicks the Cancel button")
    public void theUserClicksTheCancelButton() {
        sellPlantPage.clickCancelButton();
    }

    @Then("the user should be redirected to the sales page")
    public void theUserShouldBeRedirectedToTheSalesPage() {
        assertThat(sellPlantPage.isOnSalesPage())
                .as("User should be redirected to the sales page")
                .isTrue();
    }

    // ---- Delete / Cancel Delete Steps ----

    private String targetSaleId;

    @Given("at least one sales record exists")
    public void atLeastOneSalesRecordExists() {
        assertThat(salesPage.hasAtLeastOneSalesRecord())
                .as("At least one sales record should exist in the table")
                .isTrue();
    }

    @When("the admin clicks the delete icon for a sales record")
    public void theAdminClicksTheDeleteIconForASalesRecord() {

        targetSaleId = salesPage.getFirstSaleDeleteId();
        assertThat(targetSaleId)
                .as("Sale ID should be captured before attempting delete")
                .isNotBlank();
        salesPage.clickDeleteIconForFirstSale();
    }

    @Then("a confirmation prompt should be displayed")
    public void aConfirmationPromptShouldBeDisplayed() {
        assertThat(salesPage.isConfirmationPromptDisplayed())
                .as("A confirmation prompt (browser dialog) should be displayed")
                .isTrue();
    }

    @When("the admin clicks cancel on the confirmation prompt")
    public void theAdminClicksCancelOnTheConfirmationPrompt() {
        salesPage.cancelConfirmationPrompt();
    }

    @Then("the sales record should still be visible on the page")
    public void theSalesRecordShouldStillBeVisibleOnThePage() {
        assertThat(salesPage.isSaleWithIdStillVisible(targetSaleId))
                .as("Sale with ID " + targetSaleId + " should still be visible after cancelling deletion")
                .isTrue();
    }

}
