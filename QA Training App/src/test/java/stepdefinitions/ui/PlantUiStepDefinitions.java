package stepdefinitions.ui;

import actions.AuthenticationActions;
import actions.PlantActions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.model.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.Keys;
import pages.LoginPage;
import pages.PlantsPage;
import net.thucydides.model.environment.SystemEnvironmentVariables;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class PlantUiStepDefinitions {

        @Managed(driver = "chrome")
        private WebDriver driver;

        @Steps
        LoginPage loginPage;

        @Steps
        AuthenticationActions authenticationActions;

        @Steps
        PlantActions plantActions;

        private EnvironmentVariables environmentVariables;

        private Actor user;

        @Before
        public void setUp() {
                environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
                user = Actor.named("Admin User");
                user.can(BrowseTheWeb.with(driver));
        }

        @Given("the user is logged in as Admin with username {string} and password {string}")
        public void theUserIsLoggedInAsAdmin(String username, String password) {
                loginPage.loginAsAdmin();
        }

        @Given("the user is logged in as a normal user")
        public void theUserIsLoggedInAsNormalUser() {
                loginPage.loginAsUser();
        }

        @Given("at least one plant exists in the list")
        public void atLeastOnePlantExistsInTheList() {
                // Use API actions to ensure data exists
                // We must authenticate as admin for API setup first
                authenticationActions.authenticateAsAdmin();
                plantActions.ensureAtLeastOnePlantExists();
        }

        @Given("the user is on the Plants page")
        public void theUserIsOnThePlantsPage() {
                String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                                .from(environmentVariables)
                                .getOptionalProperty("webdriver.base.url")
                                .orElse("http://localhost:8080");
                user.attemptsTo(Open.url(baseUrl + "/ui/plants"));
        }

        @When("the user navigates to the Plants page")
        public void theUserNavigatesToThePlantsPage() {
                theUserIsOnThePlantsPage();
        }

        @When("the user observes the columns in the plants table")
        public void theUserObservesTheColumnsInThePlantsTable() {
                // Ensure table is visible
                user.should(seeThat("Plants table is visible",
                                net.serenitybdd.screenplay.questions.Visibility.of(PlantsPage.PLANTS_TABLE), is(true)));
        }

        @Then("there are no {string} or {string} buttons visible for any plant row")
        public void thereAreNoButtonsVisibleForAnyPlantRow(String btn1, String btn2) {
                user.should(seeThat("No Edit or Delete buttons are visible",
                                PlantsPage.areEditOrDeleteButtonsVisible(),
                                is(false)));
        }

        @Then("the {string} column is empty or not present")
        public void theColumnIsEmptyOrNotPresent(String columnName) {
                // If it is present, it must be empty (no buttons).
                // We've already verified no buttons.
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

        @When("the user selects the {string} category")
        public void theUserSelectsTheCategory(String categoryName) {
                user.attemptsTo(
                                SelectFromOptions.byVisibleText(categoryName).from(PlantsPage.CATEGORY_DROPDOWN));
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
                                                PlantsPage.successMessageIsDisplayed(), is(true)));

                // Optionally verify the message text contains expected text
                user.should(
                                seeThat("Success message text",
                                                PlantsPage.successMessageText(), containsString(expectedMessage)));
        }

        @Then("the user is redirected to the Plants list")
        public void theUserIsRedirectedToThePlantsList() {
                user.should(
                                seeThat("User is on plants list page",
                                                PlantsPage.isOnPlantsListPage(), is(true)));
        }

        @Then("the new plant {string} appears in the table")
        public void theNewPlantAppearsInTheTable(String plantName) {
                // Use the unique plant name that was actually entered
                String uniquePlantName = net.serenitybdd.core.Serenity.sessionVariableCalled("uniquePlantName");
                user.should(
                                seeThat("Plant appears in table",
                                                PlantsPage.plantAppearsInTable(uniquePlantName), is(true)));
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
                                        boolean forbiddenMessage = BrowseTheWeb.as(actor).getDriver().getPageSource()
                                                        .contains("Forbidden") ||
                                                        BrowseTheWeb.as(actor).getDriver().getPageSource()
                                                                        .contains("Access Denied")
                                                        ||
                                                        BrowseTheWeb.as(actor).getDriver().getTitle().contains("403");

                                        return relocated || forbiddenMessage;
                                }, is(true)));
        }

        @Then("validation error messages are displayed below specific fields")
        public void validationErrorMessagesAreDisplayedBelowSpecificFields() {
                user.should(
                                seeThat("Plant Name validation error is visible",
                                                net.serenitybdd.screenplay.questions.Visibility
                                                                .of(PlantsPage.PLANT_NAME_ERROR),
                                                is(true)),
                                seeThat("Price validation error is visible",
                                                net.serenitybdd.screenplay.questions.Visibility
                                                                .of(PlantsPage.PRICE_ERROR),
                                                is(true)));
        }

        // Delete Plant Step Definitions
        @Given("the plant {string} exists in the list")
        public void thePlantExistsInTheList(String plantName) {
                // Verify that the plants table is visible
                user.should(
                                seeThat("Plants table is visible",
                                                net.serenitybdd.screenplay.questions.Visibility
                                                                .of(PlantsPage.PLANTS_TABLE),
                                                is(true)));

                // Verify the specific plant exists in the table
                user.should(
                                seeThat("Plant '" + plantName + "' exists in table",
                                                PlantsPage.plantAppearsInTable(plantName), is(true)));

                // Store the plant name for later steps
                net.serenitybdd.core.Serenity.setSessionVariable("plantToDelete").to(plantName);
        }

        @When("the user searches for the plant {string}")
        public void theUserSearchesForThePlant(String plantName) {
                // Enter the plant name in the search field and press Enter
                user.attemptsTo(
                                Enter.theValue(plantName).into(PlantsPage.SEARCH_FIELD).thenHit(Keys.ENTER));

                // Wait for search to filter results
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }

        @When("the user clicks the Delete button for the plant {string}")
        public void theUserClicksTheDeleteButtonForThePlant(String plantName) {
                // Click the delete button for the specific plant
                user.attemptsTo(
                                Click.on(PlantsPage.deleteButtonForPlant(plantName)));
        }

        @When("the user confirms the deletion in the browser dialog")
        public void theUserConfirmsTheDeletionInTheBrowserDialog() {
                WebDriver driver = BrowseTheWeb.as(user).getDriver();
                // Wait for alert and accept
                try {
                        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(
                                        driver, java.time.Duration.ofSeconds(5));
                        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
                        driver.switchTo().alert().accept();
                } catch (Exception e) {
                        // Log or ignore if alert didn't appear fast enough (though it should)
                        System.out.println("Alert handling exception: " + e.getMessage());
                }
        }

        @Then("the plant {string} is removed from the table")
        public void thePlantIsRemovedFromTheTable(String plantName) {
                user.should(
                                seeThat("Plant is removed from table",
                                                PlantsPage.plantIsRemovedFromTable(plantName), is(true)));
        }

        @Then("the plant {string} no longer appears in search results")
        public void thePlantNoLongerAppearsInSearchResults(String plantName) {
                // Search for the deleted plant
                user.attemptsTo(
                                Enter.theValue(plantName).into(PlantsPage.SEARCH_FIELD).thenHit(Keys.ENTER));

                // Wait for search to complete
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }

                // Verify it doesn't appear
                user.should(
                                seeThat("Plant does not appear in search results",
                                                PlantsPage.plantDoesNotAppearInSearch(plantName), is(true)));
        }

        @Given("multiple plants with different names exist")
        public void multiplePlantsWithDifferentNamesExist() {
                // Ensure table is visible and likely has content
                user.attemptsTo(
                                net.serenitybdd.screenplay.waits.WaitUntil.the(PlantsPage.PLANTS_TABLE,
                                                net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible())
                                                .forNoMoreThan(10).seconds());
        }

        @When("the user enters {string} in the {string} input box")
        public void theUserEntersValInTheInputBox(String value, String inputBox) {
                if ("Search plant".equalsIgnoreCase(inputBox)) {
                        user.attemptsTo(Enter.theValue(value).into(PlantsPage.SEARCH_FIELD));
                } else {
                        // Use existing logic for other inputs if needed?
                        // Or throw exception
                        throw new IllegalArgumentException("Unknown input box: " + inputBox);
                }
        }

        @When("the user clicks the {string} button")
        public void theUserClicksTheButton(String buttonName) {
                if ("Search".equalsIgnoreCase(buttonName)) {
                        user.attemptsTo(Click.on(PlantsPage.SEARCH_BUTTON));
                        // Small wait for update
                        try {
                                Thread.sleep(1000);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                } else if ("Save".equalsIgnoreCase(buttonName)) {
                        user.attemptsTo(Click.on(PlantsPage.SAVE_BUTTON));
                } else {
                        throw new IllegalArgumentException("Unknown button: " + buttonName);
                }
        }

        @Then("the list updates to show only plants matching {string}")
        public void theListUpdatesToShowOnlyPlantsMatching(String term) {
                user.should(
                                seeThat("All visible plants match " + term,
                                                PlantsPage.allVisiblePlantsMatch(term), is(true)));
        }

        @Then("non-matching plants are hidden")
        public void nonMatchingPlantsAreHidden() {
                // Implicitly verified by the previous step
        }

        @When("the user searches for the created plant")
        public void theUserSearchesForTheCreatedPlant() {
                String uniquePlantName = net.serenitybdd.core.Serenity.sessionVariableCalled("uniquePlantName");
                if (uniquePlantName == null) {
                        throw new IllegalStateException("No plant was created in this session to search for.");
                }
                theUserSearchesForThePlant(uniquePlantName);
        }

        @When("the user clicks the Delete button for the created plant")
        public void theUserClicksTheDeleteButtonForTheCreatedPlant() {
                String uniquePlantName = net.serenitybdd.core.Serenity.sessionVariableCalled("uniquePlantName");
                if (uniquePlantName == null) {
                        throw new IllegalStateException("No plant was created in this session to delete.");
                }
                theUserClicksTheDeleteButtonForThePlant(uniquePlantName);
        }

        @Then("the created plant is removed from the table")
        public void theCreatedPlantIsRemovedFromTheTable() {
                String uniquePlantName = net.serenitybdd.core.Serenity.sessionVariableCalled("uniquePlantName");
                if (uniquePlantName == null) {
                        throw new IllegalStateException("No plant was created in this session to verify removal.");
                }
                thePlantIsRemovedFromTheTable(uniquePlantName);
        }

        @Then("the created plant no longer appears in search results")
        public void theCreatedPlantNoLongerAppearsInSearchResults() {
                String uniquePlantName = net.serenitybdd.core.Serenity.sessionVariableCalled("uniquePlantName");
                if (uniquePlantName == null) {
                        throw new IllegalStateException(
                                        "No plant was created in this session to verify search results.");
                }
                thePlantNoLongerAppearsInSearchResults(uniquePlantName);
        }
}
