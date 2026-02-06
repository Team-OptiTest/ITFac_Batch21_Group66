package stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import pages.PlantsPage;
import questions.PlantQuestions;
import pages.LoginPage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class PlantUIStepDefinitions {

    private Actor user;

    @Steps
    private LoginPage loginPage;

    @Given("the user is logged in as Admin")
    public void theUserIsLoggedInAsAdmin() {
        loginPage.loginAsAdmin();
    }

    @Given("the user is on the Plants page")
    public void theUserIsOnThePlantsPage() {
        user.attemptsTo(Click.on(PlantsPage.PAGE_TITLE));
    }

    @When("the user clicks on the {string} button")
    public void theUserClicksOnTheButton(String buttonText) {
        if (!"Add a Plant".equalsIgnoreCase(buttonText)) {
            throw new IllegalArgumentException("Unsupported button: " + buttonText);
        }
        user.attemptsTo(Click.on(PlantsPage.ADD_PLANT_BUTTON));
    }

    @When("the user enters {string} as the Plant Name")
    public void theUserEntersAsThePlantName(String plantName) {
        user.attemptsTo(
                Enter.theValue(plantName).into(PlantsPage.PLANT_NAME_FIELD));
    }

    @When("the user selects a Category from the dropdown")
    public void theUserSelectsACategoryFromTheDropdown() {
        // Select the first available category (index 1, as 0 is usually a placeholder)
        user.attemptsTo(
                SelectFromOptions.byIndex(1).from(PlantsPage.CATEGORY_DROPDOWN));
    }

    @When("the user enters {string} as the Price")
    public void theUserEntersAsThePrice(String price) {
        user.attemptsTo(
                Enter.theValue(price).into(PlantsPage.PRICE_FIELD));
    }

    @When("the user enters {string} as the Quantity")
    public void theUserEntersAsTheQuantity(String quantity) {
        user.attemptsTo(
                Enter.theValue(quantity).into(PlantsPage.QUANTITY_FIELD));
    }

    @When("the user clicks the Save button")
    public void theUserClicksTheSaveButton() {
        user.attemptsTo(
                Click.on(PlantsPage.SAVE_BUTTON));
    }

    @Then("the {string} message is displayed")
    public void theMessageIsDisplayed(String expectedMessage) {
        user.should(
                seeThat("Success message is visible",
                        PlantQuestions.successMessageIsDisplayed(), is(true)));

        // Optionally verify the message text contains expected text
        user.should(
                seeThat("Success message text",
                        PlantQuestions.successMessageText(), containsString(expectedMessage)));
    }

    @Then("the user is redirected to the Plants list")
    public void theUserIsRedirectedToThePlantsList() {
        user.should(
                seeThat("User is on plants list page",
                        PlantQuestions.isOnPlantsListPage(), is(true)));
    }

    @Then("the new plant {string} appears in the table")
    public void theNewPlantAppearsInTheTable(String plantName) {
        user.should(
                seeThat("Plant appears in table",
                        PlantQuestions.plantAppearsInTable(plantName), is(true)));
    }
}
