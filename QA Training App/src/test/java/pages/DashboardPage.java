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

    // Total plants count
    private static final By TOTAL_PLANTS_COUNT = By.id("plants-total-count");
    private static final By TOTAL_PLANTS_COUNT_SIMPLE = By.xpath(
            "//h6[contains(text(), 'Plants')]/following::div[contains(@class, 'fw-bold fs-5')][1]"
    );

    // Low stock plants count
    private static final By LOW_STOCK_PLANTS_COUNT = By.id("plants-low-stock");

    // Cards
    private static final By CATEGORIES_CARD = By.xpath("//h6[contains(text(), 'Categories')]");
    private static final By SALES_CARD = By.xpath("//h6[contains(text(), 'Sales')]");
    private static final By INVENTORY_CARD = By.xpath("//h6[contains(text(), 'Inventory')]");

    // Navigation menu elements
    private static final By NAVIGATION_MENU = By.cssSelector("nav, .navbar, .sidebar, [role='navigation']");
    private static final By DASHBOARD_MENU_ITEM = By.xpath(
        "//a[contains(@href,'dashboard') or contains(text(),'Dashboard') or contains(@class,'dashboard')]"
    );
    
    // Active state indicators - common CSS classes for active menu items
    private static final By ACTIVE_MENU_ITEM = By.cssSelector(
        "a.active, li.active > a, .nav-item.active > a, [aria-current='page']"
    );
    
    // Specific dashboard active menu item
    private static final By ACTIVE_DASHBOARD_MENU = By.xpath(
        "//a[contains(@href,'dashboard') and (contains(@class,'active') or @aria-current='page' or parent::li[contains(@class,'active')])]"
    );

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
            String currentUrl = getDriver().getCurrentUrl();
            boolean isOnDashboard = currentUrl.contains("/ui/dashboard");
            
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

    public String getPlantsCardTotalCount() {
        try {
            waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(TOTAL_PLANTS_COUNT_SIMPLE));
            String countText = getDriver().findElement(TOTAL_PLANTS_COUNT_SIMPLE).getText();
            return countText.trim();
        } catch (Exception e) {
            try {
                waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(TOTAL_PLANTS_COUNT));
                String countText = getDriver().findElement(TOTAL_PLANTS_COUNT).getText();
                return countText.trim();
            } catch (Exception ex) {
                return findPlantsCountInPageSource();
            }
        }
    }

    private String findPlantsCountInPageSource() {
        try {
            String pageSource = getDriver().getPageSource();
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "<div class=\"fw-bold fs-5\"[^>]*>\\s*(\\d+)\\s*</div>\\s*<div class=\"small text-muted\">Total</div>"
            );
            java.util.regex.Matcher matcher = pattern.matcher(pageSource);

            if (matcher.find()) {
                return matcher.group(1).trim();
            }

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

    public String getPlantsCardLowStockCount() {
        try {
            waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(LOW_STOCK_PLANTS_COUNT));
            String count = getDriver().findElement(LOW_STOCK_PLANTS_COUNT).getText().trim();
            System.out.println("Found low stock count using XPath: '" + count + "'");
            return count;
        } catch (Exception e) {
            System.out.println("XPath method failed, trying alternative...");
            
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

    public boolean isPlantsCardVisible() {
        try {
            return getDriver().findElement(PLANTS_CARD_TITLE).isDisplayed()
                    && getDriver().findElement(PLANTS_CARD).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDashboardLoaded() {
        try {
            boolean hasTitle = getDriver().findElement(DASHBOARD_TITLE).isDisplayed();
            boolean hasPlantsCard = isPlantsCardVisible();
            return hasTitle && hasPlantsCard;
        } catch (Exception e) {
            return false;
        }
    }

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

    public String getCardCount(String cardName, String countType) {
        if ("Plants".equalsIgnoreCase(cardName)) {
            if ("Total".equalsIgnoreCase(countType)) {
                return getPlantsCardTotalCount();
            } else if ("Low Stock".equalsIgnoreCase(countType)) {
                return getPlantsCardLowStockCount();
            }
        }
        return "0";
    }

    // Navigation menu methods
    public boolean isDashboardMenuActive() {
        try {
            // Wait for navigation menu to be present
            waitForCondition().until(ExpectedConditions.presenceOfElementLocated(NAVIGATION_MENU));
            
            // Method 1: Check if dashboard menu item has active class
            try {
                String dashboardClass = getDriver().findElement(DASHBOARD_MENU_ITEM).getAttribute("class");
                boolean hasActiveClass = dashboardClass != null && 
                    (dashboardClass.contains("active") || 
                     dashboardClass.contains("selected") || 
                     dashboardClass.contains("current"));
                
                if (hasActiveClass) {
                    System.out.println("Dashboard menu has active class: " + dashboardClass);
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Could not find dashboard menu item class");
            }
            
            // Method 2: Check for aria-current attribute
            try {
                String ariaCurrent = getDriver().findElement(DASHBOARD_MENU_ITEM).getAttribute("aria-current");
                if ("page".equals(ariaCurrent)) {
                    System.out.println("Dashboard menu has aria-current='page'");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Could not find aria-current attribute");
            }
            
            // Method 3: Check parent element for active class
            try {
                String parentClass = getDriver().findElement(DASHBOARD_MENU_ITEM).findElement(By.xpath("..")).getAttribute("class");
                boolean parentActive = parentClass != null && parentClass.contains("active");
                
                if (parentActive) {
                    System.out.println("Dashboard menu parent has active class: " + parentClass);
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Could not check parent element");
            }
            
            // Method 4: Check for specific active dashboard menu selector
            try {
                boolean hasActiveDashboard = getDriver().findElement(ACTIVE_DASHBOARD_MENU).isDisplayed();
                if (hasActiveDashboard) {
                    System.out.println("Found active dashboard menu using specific selector");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Active dashboard menu not found with specific selector");
            }
            
            return false;
            
        } catch (Exception e) {
            System.out.println("Error checking dashboard menu active state: " + e.getMessage());
            return false;
        }
    }
}