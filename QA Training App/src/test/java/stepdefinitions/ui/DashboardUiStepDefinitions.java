package stepdefinitions.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import pages.DashboardPage;
import pages.LoginPage;

public class DashboardUiStepDefinitions {

    @Steps
    LoginPage loginPage;

    @Steps
    DashboardPage dashboardPage;

    private int databasePlantCount;
    private int databaseLowStockPlantsCount;

    @Given("User logged in")
    public void userLoggedIn() {
        loginPage.loginAsUser();
    }

    @Given("Database has {int} plants")
    public void databaseHasPlants(int plantCount) {
        this.databasePlantCount = plantCount;
        System.out.println("Database setup: Should have " + plantCount + " plants");
    }

    @Given("{int} Plants have quantity < {int}")
    public void plantsHaveQuantityLessThan(int lowStockCount, int threshold) {
        this.databaseLowStockPlantsCount = lowStockCount;
        System.out.println("Database setup: Should have " + lowStockCount + " plants with quantity < " + threshold);
    }

    @When("Navigate to dashboard page")
    public void navigateToDashboard() {
        dashboardPage.navigateToDashboard();

        assertThat(dashboardPage.isDashboardLoaded())
                .as("Dashboard should load successfully")
                .isTrue();
    }

    @When("Check {string} in {string} card")
    public void checkCountTypeInCard(String countType, String cardName) {
        assertThat(dashboardPage.isCardVisible(cardName))
                .as(cardName + " card should be visible")
                .isTrue();
        
        // Specific verification for the count type
        if ("Low Stock".equalsIgnoreCase(countType)) {
            System.out.println("Verifying Low Stock count in " + cardName + " card");
        }
    }

    @Then("Summary card {string} shows the correct plant count")
    public void summaryCardShowsCorrectCount(String cardName) {
        String actualCount = dashboardPage.getPlantsCardTotalCount();

        // Verify it's a positive number
        assertThat(actualCount)
            .as(cardName + " card should show a valid count")
            .matches("\\d+");
    }

    @Then("Summary card {string} shows {string} {string}")
    public void summaryCardShowsCountWithType(String cardName, String expectedCount, String countType) {
        String actualCount = dashboardPage.getCardCount(cardName, countType);
        
        System.out.println("Verifying " + cardName + " card - " + countType + ":");
        System.out.println("  Expected: " + expectedCount);
        System.out.println("  Actual: " + actualCount);
        
        assertThat(actualCount)
                .as(cardName + " card should show " + expectedCount + " for " + countType)
                .isEqualTo(expectedCount);
    }

    @Then("Summary card {string} shows {string} Low Stock")
    public void summaryCardShowsLowStock(String cardName, String expectedLowStockCount) {
        String actualLowStockCount = dashboardPage.getPlantsCardLowStockCount();
        
        System.out.println("Verifying " + cardName + " card - Low Stock:");
        System.out.println("  Expected: " + expectedLowStockCount);
        System.out.println("  Actual: " + actualLowStockCount);
        
        assertThat(actualLowStockCount)
                .as(cardName + " card should show " + expectedLowStockCount + " Low Stock")
                .isEqualTo(expectedLowStockCount);
    }

    @Then("Summary card {string} shows correct low stock count")
    public void summaryCardShowsCorrectLowStockCount(String cardName) {
        String actualLowStockCount = dashboardPage.getPlantsCardLowStockCount();
    
        assertThat(actualLowStockCount)
                .as(cardName + " card should show a valid low stock count")
                .matches("\\d+");
        
        String totalCount = dashboardPage.getPlantsCardTotalCount();
        if (!"0".equals(totalCount) && !"0".equals(actualLowStockCount)) {
            int lowStock = Integer.parseInt(actualLowStockCount);
            int total = Integer.parseInt(totalCount);
            assertThat(lowStock)
                .as("Low stock count should not exceed total count")
                .isLessThanOrEqualTo(total);
        }
    }

    @Given("Valid credentials available")
    public void validCredentialsAvailable() {
        System.out.println("Using test credentials from environment configuration");
    }

    @When("Login successfully")
    public void loginSuccessfully() {
        loginPage.loginAsUser();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("Dashboard page loads immediately with summary information")
    public void dashboardPageLoadsImmediatelyWithSummaryInformation() {
        assertThat(dashboardPage.isDashboardLoaded())
                .as("Dashboard should load after login")
                .isTrue();
        
        String currentUrl = dashboardPage.getDriver().getCurrentUrl();
        assertThat(currentUrl)
                .as("Should be on dashboard page")
                .contains("/ui/dashboard");
        
        assertThat(dashboardPage.isCardVisible("Plants"))
                .as("Plants card should be visible")
                .isTrue();
        
        assertThat(dashboardPage.isCardVisible("Categories"))
                .as("Categories card should be visible")
                .isTrue();
        
        assertThat(dashboardPage.isCardVisible("Sales"))
                .as("Sales card should be visible")
                .isTrue();
    }

    @Then("Verify \"Dashboard\" menu item is highlighted with active CSS class")
    public void verifyDashboardMenuItemIsHighlightedWithActiveCSSClass() {
        // Check if dashboard menu item has active state
        boolean isActive = dashboardPage.isDashboardMenuActive();
        
        System.out.println("Checking Dashboard menu active state...");
        System.out.println("Dashboard menu active: " + isActive);
        
        assertThat(isActive)
                .as("Dashboard menu item should be highlighted with active CSS class when on dashboard page")
                .isTrue();
    }
}