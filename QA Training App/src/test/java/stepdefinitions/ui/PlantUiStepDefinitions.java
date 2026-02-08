package stepdefinitions.ui;

import actions.AuthenticationActions;
import actions.CategoryActions;
import actions.PlantActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.PlantsPage;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

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
                loginPage.loginAsAdmin();
                plantActions.ensureAtLeastOnePlantExists();
        }

        @Given("the user is on the Plants page")
        public void theUserIsOnThePlantsPage() {
                plantsPage.navigateToPlantsPage();
        }

        @When("the user navigates to the Plants page")
        public void theUserNavigatesToThePlantsPage() {
                plantsPage.navigateToPlantsPage();
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
        }

        @When("the user enters {string} as the Quantity")
        public void theUserEntersAsTheQuantity(String quantity) {
                plantsPage.enterQuantity(quantity);
        }

        @When("the user clicks the Save button")
        public void theUserClicksTheSaveButton() {
                plantsPage.clickSaveButton();
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
                assertThat(plantsPage.isOnPlantsListPage())
                                .as("User should be on plants list page")
                                .isTrue();
        }

        @Then("the new plant {string} appears in the table")
        public void theNewPlantAppearsInTheTable(String plantName) {
                // Use the stored unique plant name
                String nameToCheck = (uniquePlantName != null) ? uniquePlantName : plantName;
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
        }

        @When("the user clicks the Edit button for the target plant")
        public void theUserClicksTheEditButtonForTheTargetPlant() {
                if (targetPlantName == null) {
                        throw new IllegalStateException("No target plant identified.");
                }
                plantsPage.clickEditButtonForPlant(targetPlantName);
        }

        @Then("the target plant shows price {string} and quantity {string} in the table")
        public void theTargetPlantShowsPriceAndQuantityInTheTable(String price, String quantity) {
                if (targetPlantName == null) {
                        throw new IllegalStateException("No target plant identified.");
                }
                assertThat(plantsPage.plantShowsPriceAndQuantity(targetPlantName, price, quantity))
                                .as("Target plant '" + targetPlantName + "' should show price " + price
                                                + " and quantity " + quantity)
                                .isTrue();
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
                                        .as("Main category '" + mainCategoryName + "' should not appear in the category dropdown")
                                        .doesNotContain(mainCategoryName);
                }
        }

        @Given("no plants exist in the database")
        public void noPlantsExistInTheDatabase() {
                authenticationActions.authenticateAsAdmin();
                plantActions.deleteAllPlants();
        }

        @Then("the message {string} should be displayed")
        public void theMessageShouldBeDisplayed(String expectedMessage) {
                assertThat(plantsPage.isMessageDisplayedInTableBody(expectedMessage))
                                .as("Expected message should be displayed: " + expectedMessage)
                                .isTrue();
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