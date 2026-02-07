package stepdefinitions.ui;

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

public class PlantUIStepDefinitions {

        @Managed(driver = "chrome")
        private WebDriver driver;

        @Steps
        LoginPage loginPage;

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
                loginPage.loginAsUser();
        }

        @Given("the user is on the Plants page")
        public void theUserIsOnThePlantsPage() {

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
                org.openqa.selenium.WebDriver driver = BrowseTheWeb.as(user).getDriver();

                // Wait a moment for the dialog to appear
                try {
                        Thread.sleep(500);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                System.out.println("\n\n\n Alert Appeared\n\n\n");

                // Accept the browser confirmation dialog (alert) IMMEDIATELY
                // We must do this before any other driver operations
                try {
                        driver.switchTo().alert().accept();
                        System.out.println("\n\n\n Alert Accepted - Waiting for page refresh\n\n\n");
                } catch (Exception e) {
                        System.out.println("\n\n\n Failed to accept alert: " + e.getMessage() + "\n\n\n");
                }

                // Wait for the page to be fully loaded (document ready)
                try {
                        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(
                                        driver, java.time.Duration.ofSeconds(10));
                        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                                        .executeScript("return document.readyState").equals("complete"));
                        System.out.println("\n\n\n Page Fully Loaded\n\n\n");
                } catch (Exception e) {
                        System.out.println("\n\n\n Page load check failed: " + e.getMessage() + "\n\n\n");
                }

                // Additional wait for the page refresh and success message to appear
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                System.out.println("\n\n\n Plant Deleted - Ready to verify\n\n\n");

                // DEBUG: Print page source to see what's actually there
                try {
                        String pageSource = driver.getPageSource();
                        System.out.println("\n\n\n=== PAGE SOURCE AFTER DELETE ===\n");
                        // Print first 2000 characters to see the content
                        System.out.println(pageSource.substring(0, Math.min(2000, pageSource.length())));
                        System.out.println("\n\n\n=== END PAGE SOURCE ===\n");

                        // Check if success message text exists anywhere
                        if (pageSource.contains("deleted successfully") || pageSource.contains("Plant deleted")) {
                                System.out.println("\n\n\n SUCCESS MESSAGE FOUND IN PAGE SOURCE! \n\n\n");
                        } else {
                                System.out.println("\n\n\n SUCCESS MESSAGE NOT FOUND IN PAGE SOURCE! \n\n\n");
                        }
                } catch (Exception e) {
                        System.out.println("\n\n\n Failed to get page source: " + e.getMessage() + "\n\n\n");
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
}
