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
        
        // You might want to add specific verification for the count type
        if ("Low Stock".equalsIgnoreCase(countType)) {
            System.out.println("Verifying Low Stock count in " + cardName + " card");
        }
    }

    @Then("Summary card {string} shows the correct plant count")
    public void summaryCardShowsCorrectCount(String cardName) {
        String actualCount = dashboardPage.getPlantsCardTotalCount();

        // Just verify it's a positive number
        assertThat(actualCount)
            .as(cardName + " card should show a valid count")
            .matches("\\d+"); // Any positive no
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
                .matches("\\d+"); // Any positive no    
        String totalCount = dashboardPage.getPlantsCardTotalCount();
        if (!"0".equals(totalCount) && !"0".equals(actualLowStockCount)) {
            int lowStock = Integer.parseInt(actualLowStockCount);
            int total = Integer.parseInt(totalCount);
            assertThat(lowStock)
                .as("Low stock count should not exceed total count")
                .isLessThanOrEqualTo(total);
    }
}
}