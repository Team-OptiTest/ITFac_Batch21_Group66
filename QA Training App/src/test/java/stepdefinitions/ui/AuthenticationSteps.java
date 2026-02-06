package stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;
import pages.LoginPage;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationSteps {
    
    @Steps
    LoginPage loginPage;
    
    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        loginPage.navigateToLoginPage();
    }
    
    @When("the user enters username {string}")
    public void theUserEntersUsername(String username) {
        loginPage.enterUsername(username);
    }
    
    @When("the user enters password {string}")
    public void theUserEntersPassword(String password) {
        loginPage.enterPassword(password);
    }
    
    @When("the user clicks the login button")
    public void theUserClicksTheLoginButton() {
        loginPage.clickLogin();
    }
    
    @Then("the user should be logged in successfully")
    public void theUserShouldBeLoggedInSuccessfully() {
        assertThat(loginPage.isLoggedIn())
            .as("User should be logged in")
            .isTrue();
    }
}