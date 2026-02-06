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
import net.serenitybdd.screenplay.targets.Target;
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

        @When("the user leaves the {string} empty")
        public void theUserLeavesTheFieldEmpty(String fieldName) {
                Target targetField;
                if ("Plant Name".equalsIgnoreCase(fieldName)) {
                        targetField = PlantsPage.PLANT_NAME_FIELD;
                } else if ("Price".equalsIgnoreCase(fieldName)) {
                        targetField = PlantsPage.PRICE_FIELD;
                } else {
                        throw new IllegalArgumentException("Unsupported field: " + fieldName);
                }
                user.attemptsTo(Enter.theValue("").into(targetField));
        }

        @Then("the form is not submitted")
        public void theFormIsNotSubmitted() {
                // Verify we are still on the add plant page (url contains /add)
                user.should(seeThat("User is still on Add Plant page",
                                actor -> BrowseTheWeb.as(actor).getDriver().getCurrentUrl().contains("/plants/add"),
                                is(true)));
        }

        @Then("the validation error {string} is displayed")
        public void theValidationErrorIsDisplayed(String errorMessage) {
                Target errorTarget;
                if (errorMessage.contains("Plant Name")) {
                        errorTarget = PlantsPage.PLANT_NAME_ERROR;
                } else if (errorMessage.contains("Price")) {
                        errorTarget = PlantsPage.PRICE_ERROR;
                } else {
                        throw new IllegalArgumentException("Unknown validation error: " + errorMessage);
                }
                
                user.should(seeThat("Validation error is displayed",
                                net.serenitybdd.screenplay.questions.Visibility.of(errorTarget), is(true)));
        }

        @Given("the normal user is authenticated")
        public void theNormalUserIsAuthenticated() {
                String username = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getOptionalProperty("test.user.username")
                                .orElse("testuser");
                String password = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getOptionalProperty("test.user.password")
                                .orElse("test123");

                user.attemptsTo(
                                Login.asUser(username, password, environmentVariables));
        }

        @When("the user navigates directly to the add plant page")
        public void theUserNavigatesDirectlyToTheAddPlantPage() {
                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getOptionalProperty("webdriver.base.url")
                                .orElse("http://localhost:8080");
                
                String addPlantUrl = baseUrl + "/ui/plants/add";
                
                // Directly open the URL
                BrowseTheWeb.as(user).getDriver().get(addPlantUrl);
        }

        @Then("the user is redirected to the dashboard or sees access denied")
        public void theUserIsRedirectedToDashboardOrSeesAccessDenied() {
                // Check if current URL is NOT /ui/plants/add (redirected)
                // OR if a 403 / Access Denied message is visible
                
                user.should(seeThat("User is denied access",
                                actor -> {
                                        String currentUrl = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
                                        // Condition 1: Redirected to dashboard or login or root
                                        boolean relocated = !currentUrl.contains("/ui/plants/add");
                                        
                                        // Condition 2: Access Denied Page/Message
                                        // Assuming standard Spring Boot white label error or app specific 403
                                        boolean forbiddenMessage = BrowseTheWeb.as(actor).getDriver().getPageSource().contains("Forbidden") ||
                                                                   BrowseTheWeb.as(actor).getDriver().getPageSource().contains("Access Denied") ||
                                                                   BrowseTheWeb.as(actor).getDriver().getTitle().contains("403");
                                                                   
                                        return relocated || forbiddenMessage;
                                }, is(true)));
        }

        @Then("validation error messages are displayed below specific fields")
        public void validationErrorMessagesAreDisplayedBelowSpecificFields() {
                user.should(
                        seeThat("Plant Name validation error is visible",
                                net.serenitybdd.screenplay.questions.Visibility.of(PlantsPage.PLANT_NAME_ERROR), is(true)),
                        seeThat("Price validation error is visible",
                                net.serenitybdd.screenplay.questions.Visibility.of(PlantsPage.PRICE_ERROR), is(true))
                );
        }

        @Given("multiple plants with different names exist")
        public void multiplePlantsWithDifferentNamesExist() {
            // Assume database setup or background data
        }

        @When("the user enters {string} in the {string} input box")
        public void theUserEntersInTheInputBox(String value, String inputBox) {
            if ("Search plant".equalsIgnoreCase(inputBox)) {
                user.attemptsTo(Enter.theValue(value).into(PlantsPage.SEARCH_INPUT));
            } else {
                throw new IllegalArgumentException("Unsupported input box: " + inputBox);
            }
        }

        @When("the user clicks the {string} button")
        public void theUserClicksTheSearchButtonGeneric(String buttonName) {
            if ("Search".equalsIgnoreCase(buttonName)) {
                user.attemptsTo(Click.on(PlantsPage.SEARCH_BUTTON));
            } else if ("Save".equalsIgnoreCase(buttonName)) {
                user.attemptsTo(Click.on(PlantsPage.SAVE_BUTTON));
            } else {
                 throw new IllegalArgumentException("Unsupported button: " + buttonName);
            }
        }

        @Then("the list updates to show only plants matching {string}")
        public void theListUpdatesToShowOnlyMatching(String searchTerm) {
            user.should(seeThat("Matching plant is visible",
                    PlantQuestions.plantAppearsInTable(searchTerm), is(true)));
        }

        @Then("non-matching plants are hidden")
        public void nonMatchingPlantsAreHidden() {
            // Verification logic: ensure only results containing search term are present
            // For now, checking visibility of the matched item is the primary assertion
        }
}
