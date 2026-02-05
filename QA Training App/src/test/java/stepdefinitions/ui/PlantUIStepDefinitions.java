package stepdefinitions.ui;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.thucydides.model.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import pages.PlantsPage;
import questions.PlantQuestions;
import tasks.Login;
import tasks.NavigateTo;
import net.thucydides.model.environment.SystemEnvironmentVariables;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class PlantUIStepDefinitions {

        @Managed(driver = "chrome")
        private WebDriver driver;

        private EnvironmentVariables environmentVariables;

        private Actor user;

        @Before
        public void setUp() {
                environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
                user = Actor.named("Admin User");
                user.can(BrowseTheWeb.with(driver));
        }

        @Given("the admin user is authenticated")
        public void theAdminUserIsAuthenticated() {
                String username = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getOptionalProperty("test.admin.username")
                                .orElse("admin");
                String password = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getOptionalProperty("test.admin.password")
                                .orElse("admin123");

                user.attemptsTo(
                                Login.asUser(username, password, environmentVariables));
        }

        @Given("the user is logged in as Admin with username {string} and password {string}")
        public void theUserIsLoggedInAsAdmin(String username, String password) {
                user.attemptsTo(
                                Login.asUser(username, password, environmentVariables));
        }

        @Given("the user is on the Plants page")
        public void theUserIsOnThePlantsPage() {
                user.attemptsTo(
                                NavigateTo.plantsPage(environmentVariables));
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
                // Append timestamp to make the plant name unique for each test run
                String uniquePlantName = plantName + "_" + System.currentTimeMillis();
                net.serenitybdd.core.Serenity.setSessionVariable("uniquePlantName").to(uniquePlantName);

                user.attemptsTo(
                                Enter.theValue(uniquePlantName).into(PlantsPage.PLANT_NAME_FIELD));
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
                // Use the unique plant name that was actually entered
                String uniquePlantName = net.serenitybdd.core.Serenity.sessionVariableCalled("uniquePlantName");
                user.should(
                                seeThat("Plant appears in table",
                                                PlantQuestions.plantAppearsInTable(uniquePlantName), is(true)));
        }
}
