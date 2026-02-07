package stepdefinitions.ui;

import pages.SalesPage;
import pages.LoginPage;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesUiStepDefinitions {

    @Steps
    LoginPage loginPage;

    @Steps
    SalesPage salesPage;

    @Given("the user is logged in as an admin")
    public void theUserIsLoggedInAsAnAdmin() {
        loginPage.loginAsAdmin();
    }

    @When("the user navigates to the sales page")
    public void theUserNavigatesToTheSalesPage() {
        salesPage.navigateToSalesPage();
    }

    @Then("the user should be able to see the {string} button")
    public void theUserShouldSeeTheSellPlantButton(String buttonText) {
        assertThat(salesPage.isSellPlantButtonVisible())
            .as("Sell Plant button should be visible for admin")
            .isTrue();
    }
}
