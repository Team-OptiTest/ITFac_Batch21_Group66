package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class SalesPage extends PageObject {

    private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    //SALES LIST PAGE ELEMENTS
    private static final By SELL_PLANT_BUTTON_CSS = By.cssSelector("body > div > div > div.main-content > div:nth-child(2) > a");
    private static final By SELL_PLANT_BUTTON_XPATH = By.xpath("//a[contains(text(), 'Sell Plant')]");
    private static final By SALES_TABLE_SELECTOR = By.cssSelector("table.table-bordered");
    private static final By SUCCESS_MESSAGE_SELECTOR = By.cssSelector(".alert-success");
    private static final By ERROR_MESSAGE_SELECTOR = By.cssSelector(".alert-danger");

    public void navigateToSalesPage() {
        String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                .from(environmentVariables)
                .getOptionalProperty("webdriver.base.url")
                .orElse("http://localhost:8080");
        getDriver().get(baseUrl + "/ui/sales");
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void navigateToSellPlantPage() {
        String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                .from(environmentVariables)
                .getOptionalProperty("webdriver.base.url")
                .orElse("http://localhost:8080");
        getDriver().get(baseUrl + "/ui/sales/new");
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public boolean isSellPlantButtonVisible() {
        try {
            // Try friend's CSS selector first
            return getDriver().findElement(SELL_PLANT_BUTTON_CSS).isDisplayed();
        } catch (Exception e1) {
            try {
                // Try your XPath selector as fallback
                return getDriver().findElement(SELL_PLANT_BUTTON_XPATH).isDisplayed();
            } catch (Exception e2) {
                return false;
            }
        }
    }

    //Check if Sell Plant button is NOT visible (for regular users)
    public boolean isSellPlantButtonNotVisible() {
        try {
            // Try both selectors - if either is found, button is visible
            boolean cssVisible = getDriver().findElement(SELL_PLANT_BUTTON_CSS).isDisplayed();
            boolean xpathVisible = getDriver().findElement(SELL_PLANT_BUTTON_XPATH).isDisplayed();
            return !(cssVisible || xpathVisible);
        } catch (Exception e) {
            // Element not found means it's not visible
            return true;
        }
    }

    public boolean isRedirectedFromSellPlantPage() {
        try {
            String currentUrl = getDriver().getCurrentUrl();
            return !currentUrl.contains("/ui/sales/new");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessDeniedMessageDisplayed() {
        try {
            WebDriver driver = getDriver();
            String pageSource = driver.getPageSource();
            String pageTitle = driver.getTitle();

            return pageSource.contains("Forbidden")
                    || pageSource.contains("Access Denied")
                    || pageSource.contains("403")
                    || pageTitle.contains("403")
                    || pageTitle.contains("Forbidden");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSalesTableDisplayed() {
        try {
            return getDriver().findElement(SALES_TABLE_SELECTOR).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return getDriver().findElement(SUCCESS_MESSAGE_SELECTOR).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return getDriver().findElement(ERROR_MESSAGE_SELECTOR).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
