package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class DashboardPage extends PageObject {

    private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    // Dashboard title
    private static final By DASHBOARD_TITLE = By.cssSelector("h3.mb-4");
    // Plants card specific elements
    private static final By PLANTS_CARD = By.xpath("//div[contains(@class, 'card')][.//h6[contains(text(), 'Plants')]]");
    private static final By PLANTS_CARD_TITLE = By.xpath("//h6[contains(text(), 'Plants')]");

    // Total plants count - Using the exact element from HTML
    private static final By TOTAL_PLANTS_COUNT = By.id("plants-total-count");


    // Alternative simpler XPath
    private static final By TOTAL_PLANTS_COUNT_SIMPLE = By.xpath(
            "//h6[contains(text(), 'Plants')]/following::div[contains(@class, 'fw-bold fs-5')][1]"
    );

    // Low stock plants count
    private static final By LOW_STOCK_PLANTS_COUNT = By.id("plants-low-stock");


    private static final By CATEGORIES_CARD = By.xpath("//h6[contains(text(), 'Categories')]");
    private static final By SALES_CARD = By.xpath("//h6[contains(text(), 'Sales')]");
    private static final By INVENTORY_CARD = By.xpath("//h6[contains(text(), 'Inventory')]");

    
    public void navigateToDashboard() {
        String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                .from(environmentVariables)
                .getOptionalProperty("webdriver.base.url")
                .orElse("http://localhost:8080");
        getDriver().get(baseUrl + "/ui/dashboard");
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(DASHBOARD_TITLE));
    }
    public boolean isDashboardLoadedImmediately() {
    try {
        // Check if we're already on dashboard URL
        String currentUrl = getDriver().getCurrentUrl();
        boolean isOnDashboard = currentUrl.contains("/ui/dashboard");
        
        // Check if dashboard elements are visible
        boolean hasTitle = getDriver().findElement(DASHBOARD_TITLE).isDisplayed();
        boolean hasCards = isCardVisible("Plants") && isCardVisible("Categories");
        
        return isOnDashboard && hasTitle && hasCards;
    } catch (Exception e) {
        return false;
    }
    }
    public String getCurrentUrl() {
    return getDriver().getCurrentUrl();
    }

    /**
     * Get the total plant count from the Plants card
     */
    public String getPlantsCardTotalCount() {
        try {
            // Wait for the plants count to be visible
            waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(TOTAL_PLANTS_COUNT_SIMPLE));

            // Get the text
            String countText = getDriver().findElement(TOTAL_PLANTS_COUNT_SIMPLE).getText();

            // Clean the text - remove any whitespace
            return countText.trim();
        } catch (Exception e) {
            // Try the more specific XPath if simple one fails
            try {
                waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(TOTAL_PLANTS_COUNT));
                String countText = getDriver().findElement(TOTAL_PLANTS_COUNT).getText();
                return countText.trim();
            } catch (Exception ex) {
                // Last resort: search in page source
                return findPlantsCountInPageSource();
            }
        }
    }

    /**
     * Fallback method to find plants count in page source
     */
    private String findPlantsCountInPageSource() {
        try {
            String pageSource = getDriver().getPageSource();

            // Look for the pattern: th:text="${plantSummary.totalPlants}"
            // The actual rendered value will be a number
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                    "<div class=\"fw-bold fs-5\"[^>]*>\\s*(\\d+)\\s*</div>\\s*<div class=\"small text-muted\">Total</div>"
            );
            java.util.regex.Matcher matcher = pattern.matcher(pageSource);

            if (matcher.find()) {
                return matcher.group(1).trim();
            }

            // Alternative pattern
            pattern = java.util.regex.Pattern.compile("plantSummary.totalPlants[^>]*>\\s*(\\d+)");
            matcher = pattern.matcher(pageSource);

            if (matcher.find()) {
                return matcher.group(1).trim();
            }

            return "0";
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * Get low stock plants count
     */
   public String getPlantsCardLowStockCount() {
    try {
        // First try the specific XPath
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(LOW_STOCK_PLANTS_COUNT));
        String count = getDriver().findElement(LOW_STOCK_PLANTS_COUNT).getText().trim();
        
        // Debug output
        System.out.println("Found low stock count using XPath: '" + count + "'");
        return count;
    } catch (Exception e) {
        System.out.println("XPath method failed, trying alternative...");
        
        // Alternative: Look for "Low Stock" text and get the preceding number
        try {
            String pageSource = getDriver().getPageSource();
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "<div class=\"fw-bold fs-5\"[^>]*>\\s*(\\d+)\\s*</div>\\s*<div class=\"small text-muted\">Low Stock</div>"
            );
            java.util.regex.Matcher matcher = pattern.matcher(pageSource);
            
            if (matcher.find()) {
                String count = matcher.group(1).trim();
                System.out.println("Found low stock count using regex: '" + count + "'");
                return count;
            }
        } catch (Exception ex) {
            System.out.println("Alternative method also failed: " + ex.getMessage());
        }
        
        return "0";
    }
}

    /**
     * Check if Plants card is visible
     */
    public boolean isPlantsCardVisible() {
        try {
            return getDriver().findElement(PLANTS_CARD_TITLE).isDisplayed()
                    && getDriver().findElement(PLANTS_CARD).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if dashboard is loaded
     */
    public boolean isDashboardLoaded() {
        try {
            boolean hasTitle = getDriver().findElement(DASHBOARD_TITLE).isDisplayed();
            boolean hasPlantsCard = isPlantsCardVisible();
            return hasTitle && hasPlantsCard;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if specific card is visible
     */
    public boolean isCardVisible(String cardName) {
        try {
            By cardSelector;
            switch (cardName.toLowerCase()) {
                case "plants":
                    cardSelector = PLANTS_CARD_TITLE;
                    break;
                case "categories":
                    cardSelector = CATEGORIES_CARD;
                    break;
                case "sales":
                    cardSelector = SALES_CARD;
                    break;
                case "inventory":
                    cardSelector = INVENTORY_CARD;
                    break;
                default:
                    return false;
            }
            return getDriver().findElement(cardSelector).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get count from any card
     */
    public String getCardCount(String cardName, String countType) {
        if ("Plants".equalsIgnoreCase(cardName)) {
            if ("Total".equalsIgnoreCase(countType)) {
                return getPlantsCardTotalCount();
            } else if ("Low Stock".equalsIgnoreCase(countType)) {
                return getPlantsCardLowStockCount();
            }
        }
        // Add other cards as needed
        return "0";
    }
}
