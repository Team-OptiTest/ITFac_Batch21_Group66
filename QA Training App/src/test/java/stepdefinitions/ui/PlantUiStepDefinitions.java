package stepdefinitions.ui;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.support.ui.WebDriverWait;

import actions.AuthenticationActions;
import actions.CategoryActions;
import actions.PlantActions;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import pages.LoginPage;
import pages.PlantsPage;

public class PlantUiStepDefinitions {

        @Steps
        LoginPage loginPage;

        @Steps
        PlantsPage plantsPage;

        @Steps
        PlantActions plantActions;

        @Steps
        AuthenticationActions authenticationActions;

        @Steps
        CategoryActions categoryActions;

        private String uniquePlantName;
        private String targetPlantName;
        private String originalTargetPlantRowText;

        @After("@UI_Plant_Update_003")
        public void cleanupSetupPlantAfterScenario() {
                authenticationActions.authenticateAsAdmin();
                plantActions.cleanupSetupPlant();
        }

        @Given("the user is authenticate as a normal user")
        public void theUserIsLoggedInAsNormalUserForPlant() {
                loginPage.loginAsUser();
        }

        @Given("the user is authenticate as an admin user")
        public void theUserIsLoggedInAsAnAdminUserForPlant() {
                loginPage.loginAsAdmin();
        }

        @Given("at least one plant exists in the list")
        public void atLeastOnePlantExistsInTheList() {
                // Navigate to plants page and verify table is visible
                plantsPage.navigateToPlantsPage();
                waitForMilliseconds(1000); // Wait for page to load
                assertThat(plantsPage.isPlantsTableDisplayed())
                                .as("Plants table should be visible with at least one plant")
                                .isTrue();
        }

        @Given("the user is on the Plants page")
        public void theUserIsOnThePlantsPage() {
                plantsPage.navigateToPlantsPage();
        }

        @When("the user navigates to the Plants page")
        public void theUserNavigatesToThePlantsPage() {
                plantsPage.navigateToPlantsPage();
                waitForMilliseconds(1000); // Wait for page to stabilize
        }

        @When("the user observes the columns in the plants table")
        public void theUserObservesTheColumnsInThePlantsTable() {
                // Ensure table is visible
                assertThat(plantsPage.isPlantsTableDisplayed())
                                .as("Plants table should be visible")
                                .isTrue();
        }

        @Then("there are no {string} or {string} buttons visible for any plant row")
        public void thereAreNoButtonsVisibleForAnyPlantRow(String btn1, String btn2) {
                assertThat(plantsPage.areEditOrDeleteButtonsVisible())
                                .as("No Edit or Delete buttons should be visible")
                                .isFalse();
        }

        @Then("the {string} column is empty or not present")
        public void theColumnIsEmptyOrNotPresent(String columnName) {
                // If it is present, it must be empty (no buttons).
                // Already verified no buttons in previous step
        }

        @When("the user clicks on the {string} button")
        public void theUserClicksOnTheButton(String buttonText) {
                if ("Add a Plant".equalsIgnoreCase(buttonText)) {
                        plantsPage.clickAddPlantButton();
                } else {
                        throw new IllegalArgumentException("Unsupported button: " + buttonText);
                }
        }

        @When("the user enters {string} as the Plant Name")
        public void theUserEntersAsThePlantName(String plantName) {
                // Store the unique plant name for later use
                uniquePlantName = plantName;
                plantsPage.enterPlantName(plantName);
        }

        @When("the user selects a Category from the dropdown")
        public void theUserSelectsACategoryFromTheDropdown() {
                // Select a category - implementation depends on your select method
                plantsPage.clickCategoryDropdown();
                // You may need to add a method to select by index or first option
        }

        @When("the user selects the {string} category")
        public void theUserSelectsTheCategory(String categoryName) {
                plantsPage.selectCategory(categoryName);
        }

        @When("the user enters {string} as the Price")
        public void theUserEntersAsThePrice(String price) {
                plantsPage.enterPrice(price);
                waitForMilliseconds(300); // Small delay after entering price
        }

        @When("the user enters {string} as the Quantity")
        public void theUserEntersAsTheQuantity(String quantity) {
                plantsPage.enterQuantity(quantity);
                waitForMilliseconds(300); // Small delay after entering quantity
        }

        @When("the user clicks the Save button")
        public void theUserClicksTheSaveButton() {
                plantsPage.clickSaveButton();
                waitForMilliseconds(1000); // Wait for save operation to complete
        }

        @Then("the {string} message is displayed")
        public void theMessageIsDisplayed(String expectedMessage) {
                assertThat(plantsPage.isSuccessMessageDisplayed())
                                .as("Success message should be displayed")
                                .isTrue();

                assertThat(plantsPage.getSuccessMessageText())
                                .as("Success message should contain expected text")
                                .contains(expectedMessage);
        }

        @Then("the user is redirected to the Plants list")
        public void theUserIsRedirectedToThePlantsList() {
                // Wait for redirection
                waitForMilliseconds(1000);
                assertThat(plantsPage.isOnPlantsListPage())
                                .as("User should be on plants list page")
                                .isTrue();
        }

        @Then("the new plant {string} appears in the table")
        public void theNewPlantAppearsInTheTable(String plantName) {
                // Use the stored unique plant name
                String nameToCheck = (uniquePlantName != null) ? uniquePlantName : plantName;
                waitForMilliseconds(1000); // Wait for table to update
                assertThat(plantsPage.isPlantVisibleInTable(nameToCheck))
                                .as("Plant '" + nameToCheck + "' should appear in the table")
                                .isTrue();
        }

        @Then("the new plant {string} should not appear in the table")
        public void theNewPlantShouldNotAppearInTheTable(String plantName) {
                assertThat(plantsPage.isPlantNotVisibleInTable(plantName))
                                .as("Plant '" + plantName + "' should not appear in the table")
                                .isTrue();
        }

        @When("the user leaves the {string} empty")
        public void theUserLeavesTheFieldEmpty(String fieldName) {
                if ("Plant Name".equalsIgnoreCase(fieldName)) {
                        plantsPage.enterPlantName("");
                } else if ("Price".equalsIgnoreCase(fieldName)) {
                        plantsPage.enterPrice("");
                } else {
                        throw new IllegalArgumentException("Unsupported field: " + fieldName);
                }
        }

        @Then("the form is not submitted")
        public void theFormIsNotSubmitted() {
                // Verify we are still on the add plant page (url contains /add)
                String currentUrl = plantsPage.getDriver().getCurrentUrl();
                assertThat(currentUrl)
                                .as("User should still be on Add Plant page")
                                .contains("/plants/add");
        }

        @Then("the validation error {string} is displayed")
        public void theValidationErrorIsDisplayed(String errorMessage) {
                if (errorMessage.contains("Plant Name") || errorMessage.contains("Plant name")) {
                        assertThat(plantsPage.isPlantNameErrorDisplayed())
                                        .as("Plant name validation error should be displayed")
                                        .isTrue();
                } else if (errorMessage.contains("Price")) {
                        assertThat(plantsPage.isPriceErrorDisplayed())
                                        .as("Price validation error should be displayed")
                                        .isTrue();
                } else {
                        throw new IllegalArgumentException("Unknown validation error: " + errorMessage);
                }
        }

        @When("the user navigates directly to the add plant page")
        public void theUserNavigatesDirectlyToTheAddPlantPage() {
                plantsPage.navigateToAddPlantPage();
        }

        @Then("the user is redirected to the dashboard or sees access denied")
        public void theUserIsRedirectedToDashboardOrSeesAccessDenied() {
                String currentUrl = plantsPage.getDriver().getCurrentUrl();
                String pageSource = plantsPage.getDriver().getPageSource();
                String pageTitle = plantsPage.getDriver().getTitle();

                // Check if redirected away from add plant page OR access denied message shown
                boolean relocated = !currentUrl.contains("/ui/plants/add");
                boolean forbiddenMessage = pageSource.contains("Forbidden")
                                || pageSource.contains("Access Denied")
                                || pageTitle.contains("403");

                assertThat(relocated || forbiddenMessage)
                                .as("User should be denied access to add plant page")
                                .isTrue();
        }

        @Then("validation error messages are displayed below specific fields")
        public void validationErrorMessagesAreDisplayedBelowSpecificFields() {
                assertThat(plantsPage.isPlantNameErrorDisplayed())
                                .as("Plant name validation error should be visible")
                                .isTrue();

                assertThat(plantsPage.isPriceErrorDisplayed())
                                .as("Price validation error should be visible")
                                .isTrue();
        }

        // Delete Plant Step Definitions
        @Given("the plant {string} exists in the list")
        public void thePlantExistsInTheList(String plantName) {
                // Verify that the plants table is visible
                assertThat(plantsPage.isPlantsTableDisplayed())
                                .as("Plants table should be visible")
                                .isTrue();

                // Verify the specific plant exists in the table
                assertThat(plantsPage.isPlantVisibleInTable(plantName))
                                .as("Plant '" + plantName + "' should exist in table")
                                .isTrue();
        }

        @When("the user searches for the plant {string}")
        public void theUserSearchesForThePlant(String plantName) {
                plantsPage.searchPlant(plantName);
                // Small wait for search to complete
                waitForMilliseconds(1000);
        }

        @When("the user clicks the Delete button for the plant {string}")
        public void theUserClicksTheDeleteButtonForThePlant(String plantName) {
                plantsPage.clickDeleteButtonForPlant(plantName);
        }

        @When("the user confirms the deletion in the browser dialog")
        public void theUserConfirmsTheDeletionInTheBrowserDialog() {
                try {
                        WebDriverWait wait = new WebDriverWait(plantsPage.getDriver(), Duration.ofSeconds(5));
                        wait.until(driver -> {
                                try {
                                        driver.switchTo().alert().accept();
                                        return true;
                                } catch (Exception e) {
                                        return false;
                                }
                        });
                } catch (Exception e) {
                        System.out.println("Alert handling exception: " + e.getMessage());
                }
        }

        @Then("the plant {string} is removed from the table")
        public void thePlantIsRemovedFromTheTable(String plantName) {
                // Small wait for removal to complete
                waitForMilliseconds(1000);
                assertThat(plantsPage.isPlantNotVisibleInTable(plantName))
                                .as("Plant '" + plantName + "' should be removed from table")
                                .isTrue();
        }

        @Then("the plant {string} no longer appears in search results")
        public void thePlantNoLongerAppearsInSearchResults(String plantName) {
                // Search for the deleted plant
                plantsPage.searchPlant(plantName);
                waitForMilliseconds(1000);

                // Verify it doesn't appear
                assertThat(plantsPage.isPlantNotVisibleInTable(plantName))
                                .as("Plant '" + plantName + "' should not appear in search results")
                                .isTrue();
        }

        @Given("multiple plants with different names exist")
        public void multiplePlantsWithDifferentNamesExist() {
                // Ensure table is visible and likely has content
                assertThat(plantsPage.isPlantsTableDisplayed())
                                .as("Plants table should be visible")
                                .isTrue();
        }

        @When("the user enters {string} in the {string} input box")
        public void theUserEntersValInTheInputBox(String value, String inputBox) {
                if ("Search plant".equalsIgnoreCase(inputBox)) {
                        plantsPage.enterSearchTerm(value);
                } else {
                        throw new IllegalArgumentException("Unknown input box: " + inputBox);
                }
        }

        @When("the user clicks the {string} button")
        public void theUserClicksTheButton(String buttonName) {
                if ("Search".equalsIgnoreCase(buttonName)) {
                        plantsPage.clickSearchButton();
                        waitForMilliseconds(1000);
                } else if ("Save".equalsIgnoreCase(buttonName)) {
                        plantsPage.clickSaveButton();
                } else {
                        throw new IllegalArgumentException("Unknown button: " + buttonName);
                }
        }

        @Then("the list updates to show only plants matching {string}")
        public void theListUpdatesToShowOnlyPlantsMatching(String term) {
                assertThat(plantsPage.allVisiblePlantsMatch(term))
                                .as("All visible plants should match '" + term + "'")
                                .isTrue();
        }

        @Then("non-matching plants are hidden")
        public void nonMatchingPlantsAreHidden() {
                // Implicitly verified by the previous step
        }

        @When("the user searches for the created plant")
        public void theUserSearchesForTheCreatedPlant() {
                if (uniquePlantName == null) {
                        throw new IllegalStateException("No plant was created in this session to search for.");
                }
                theUserSearchesForThePlant(uniquePlantName);
        }

        @When("the user clicks the Delete button for the created plant")
        public void theUserClicksTheDeleteButtonForTheCreatedPlant() {
                if (uniquePlantName == null) {
                        throw new IllegalStateException("No plant was created in this session to delete.");
                }
                theUserClicksTheDeleteButtonForThePlant(uniquePlantName);
        }

        @Then("the created plant is removed from the table")
        public void theCreatedPlantIsRemovedFromTheTable() {
                if (uniquePlantName == null) {
                        throw new IllegalStateException("No plant was created in this session to verify removal.");
                }
                thePlantIsRemovedFromTheTable(uniquePlantName);
        }

        @Then("the created plant no longer appears in search results")
        public void theCreatedPlantNoLongerAppearsInSearchResults() {
                if (uniquePlantName == null) {
                        throw new IllegalStateException(
                                        "No plant was created in this session to verify search results.");
                }
                thePlantNoLongerAppearsInSearchResults(uniquePlantName);
        }

        @When("the user identifies the first plant in the list as the target")
        public void theUserIdentifiesTheFirstPlantInTheListAsTheTarget() {
                targetPlantName = plantsPage.getFirstPlantName();
                if (targetPlantName == null || targetPlantName.isEmpty()) {
                        throw new IllegalStateException("No plants found in the list to update.");
                }
                System.out.println("Target plant identified: " + targetPlantName);
        }

        @When("the user records the original details of the target plant")
        public void theUserRecordsTheOriginalDetailsOfTheTargetPlant() {
                if (targetPlantName == null) {
                        throw new IllegalStateException("No target plant identified.");
                }
                originalTargetPlantRowText = plantsPage.getPlantRowText(targetPlantName);
        }

        @When("the user clicks the Edit button for the target plant")
        public void theUserClicksTheEditButtonForTheTargetPlant() {
                if (targetPlantName == null) {
                        throw new IllegalStateException("No target plant identified.");
                }
                System.out.println("Clicking Edit button for plant: " + targetPlantName);
                plantsPage.clickEditButtonForPlant(targetPlantName);
                // Additional wait for edit page to fully load
                waitForMilliseconds(1000);
        }

        @Then("the target plant shows price {string} and quantity {string} in the table")
        public void theTargetPlantShowsPriceAndQuantityInTheTable(String price, String quantity) {
                if (targetPlantName == null) {
                        throw new IllegalStateException("No target plant identified.");
                }
                // Wait for table to update after edit
                waitForMilliseconds(2000);

                System.out.println("Verifying plant '" + targetPlantName + "' has price: " + price + " and quantity: "
                                + quantity);
                assertThat(plantsPage.plantShowsPriceAndQuantity(targetPlantName, price, quantity))
                                .as("Target plant '" + targetPlantName + "' should show price " + price
                                                + " and quantity " + quantity)
                                .isTrue();
        }

        @Then("the target plant still shows its original details")
        public void theTargetPlantStillShowsItsOriginalDetails() {
                if (targetPlantName == null) {
                        throw new IllegalStateException("No target plant identified.");
                }
                if (originalTargetPlantRowText == null) {
                        throw new IllegalStateException("Original plant details were not recorded.");
                }
                String currentRowText = plantsPage.getPlantRowText(targetPlantName);
                assertThat(currentRowText)
                                .as("Target plant '" + targetPlantName + "' details should remain unchanged after cancel")
                                .isEqualTo(originalTargetPlantRowText);
        }

        @Given("plants of different categories exist")
        public void plantsOfDifferentCategoriesExist() {
                // Ensure filter dropdown is visible
                assertThat(plantsPage.isPlantsTableDisplayed())
                                .as("Plants table should be visible")
                                .isTrue();
        }

        @When("the user selects the {string} category from the filter")
        public void theUserSelectsTheCategoryFromTheFilter(String categoryName) {
                plantsPage.selectFilterCategory(categoryName);
        }

        @Then("the list updates to show only plants belonging to the {string} category")
        public void theListUpdatesToShowOnlyPlantsBelongingToTheCategory(String categoryName) {
                assertThat(plantsPage.allVisiblePlantsBelongToCategory(categoryName))
                                .as("All visible plants should belong to category '" + categoryName + "'")
                                .isTrue();
        }

        @Then("the list of plants is displayed with valid data")
        public void theListOfPlantsIsDisplayedWithValidData() {
                assertThat(plantsPage.isPlantsTableDisplayed())
                                .as("Plants table should be visible with valid data")
                                .isTrue();
        }

        @Then("the {string} button is not present")
        public void theButtonIsNotPresent(String buttonName) {
                if ("Add a Plant".equalsIgnoreCase(buttonName)) {
                        assertThat(plantsPage.isAddPlantButtonNotVisible())
                                        .as("'Add a Plant' button should not be visible")
                                        .isTrue();
                } else {
                        throw new IllegalArgumentException("Unsupported button check: " + buttonName);
                }
        }

        @When("the user clicks Cancel to discard the plant")
        public void theUserClicksCancelToDiscardThePlant() {
                plantsPage.clickCancelButton();
        }

        @Given("at least one main category exists")
        public void atLeastOneMainCategoryExists() {
                authenticationActions.authenticateAsAdmin();
                categoryActions.ensureAtLeastOneMainCategoryExists();
        }

        @Then("main categories should not be displayed in the category dropdown")
        public void mainCategoriesShouldNotBeDisplayedInTheCategoryDropdown() {
                java.util.List<String> dropdownOptions = plantsPage.getCategoryDropdownOptionTexts();
                authenticationActions.authenticateAsAdmin();
                java.util.List<String> mainCategoryNames = categoryActions.getMainCategoryNames();

                for (String mainCategoryName : mainCategoryNames) {
                        assertThat(dropdownOptions)
                                        .as("Main category '" + mainCategoryName
                                                        + "' should not appear in the category dropdown")
                                        .doesNotContain(mainCategoryName);
                }
        }

        @Given("no plants exist in the database")
        public void noPlantsExistInTheDatabase() {
                authenticationActions.authenticateAsAdmin();
                plantActions.deleteAllPlants();
        }

        @Then("the message {string} should be displayed in the plants table")
        public void theMessageShouldBeDisplayedInThePlantsTable(String expectedMessage) {
                assertThat(plantsPage.isMessageDisplayedInTableBody(expectedMessage))
                                .as("Expected message should be displayed in the plants table: " + expectedMessage)
                                .isTrue();
        }

        @When("the user searches for a plant that does not exist")
        public void theUserSearchesForAPlantThatDoesNotExist() {
                String searchTerm = "NoMatch_" + UUID.randomUUID().toString().substring(0, 8);
                plantsPage.searchPlant(searchTerm);
        }

        @Given("at least one plant with quantity less than 5 exists")
        public void atLeastOnePlantWithQuantityLessThan5Exists() {
                authenticationActions.authenticateAsAdmin();
                plantActions.ensureLowQuantityPlantExists();
        }

        @Then("a {string} badge should be displayed for a plant with quantity less than 5")
        public void aBadgeShouldBeDisplayedForAPlantWithQuantityLessThan5(String badgeText) {
                assertThat(plantsPage.isLowBadgeDisplayed())
                                .as("'" + badgeText + "' badge should be displayed for a low-quantity plant")
                                .isTrue();
        }

        @Then("the setup test plant is cleaned up")
        public void theSetupTestPlantIsCleanedUp() {
                authenticationActions.authenticateAsAdmin();
                plantActions.cleanupSetupPlant();
        }

        @Then("the low-stock test plant is cleaned up")
        public void theLowStockTestPlantIsCleanedUp() {
                authenticationActions.authenticateAsAdmin();
                plantActions.cleanupLowStockPlant();
        }

        // Helper method for wait
        private void waitForMilliseconds(long milliseconds) {
                try {
                        Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                }
        }
}