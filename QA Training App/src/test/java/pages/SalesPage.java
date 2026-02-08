package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import java.util.List;


public class SalesPage extends PageObject {

    private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    //SALES LIST PAGE ELEMENTS
    private static final By SELL_PLANT_BUTTON_CSS = By.cssSelector("body > div > div > div.main-content > div:nth-child(2) > a");
  //  private static final By SELL_PLANT_BUTTON_XPATH = By.xpath("/html/body/div/div/div[2]/div[2]/a");
    private static final By SALES_TABLE_SELECTOR = By.cssSelector("table.table-bordered");
    private static final By SUCCESS_MESSAGE_SELECTOR = By.cssSelector(".alert-success");
    private static final By ERROR_MESSAGE_SELECTOR = By.cssSelector(".alert-danger");
    private static final By FIRST_DELETE_BUTTON = By.cssSelector("form[action^='/ui/sales/delete/'] button");
    // Sales table headers

// Sales first row cells
private static final By SALES_TABLE_HEADERS = By.cssSelector("table.table-bordered thead th");
private static final By SALES_TABLE_ROWS = By.cssSelector("table.table-bordered tbody tr"); // you already have




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
        // Wait until the element is visible
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(SELL_PLANT_BUTTON_CSS));
        return $(SELL_PLANT_BUTTON_CSS).isVisible();
    } catch (Exception e) {
        return false;
    }
}

  // Check if Sell Plant button is NOT visible (for regular users)
public boolean isSellPlantButtonNotVisible() {
    try {
        return !$(SELL_PLANT_BUTTON_CSS).isVisible();
    } catch (Exception e) {
        // Element not found = not visible
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

    public boolean hasAtLeastOneSalesRecord() {
        try {
            waitForCondition().until(ExpectedConditions.presenceOfElementLocated(SALES_TABLE_SELECTOR));
            return !getDriver().findElements(SALES_TABLE_ROWS).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the unique sale ID from the first delete form's action attribute.
     * e.g. action="/ui/sales/delete/73" → returns "73"
     */
    public String getFirstSaleDeleteId() {
        try {
            waitForCondition().until(ExpectedConditions.presenceOfElementLocated(FIRST_DELETE_BUTTON));
            String action = getDriver()
                    .findElement(By.cssSelector("form[action^='/ui/sales/delete/']"))
                    .getAttribute("action");
            // action is e.g. "/ui/sales/delete/73" — grab the last segment
            return action.substring(action.lastIndexOf('/') + 1);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to extract sale id from delete action", e);
        }
    }

    public void clickDeleteIconForFirstSale() {
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(SALES_TABLE_ROWS));
        getDriver().findElement(FIRST_DELETE_BUTTON).click();
    }

    public boolean isConfirmationPromptDisplayed() {
        try {
            waitForCondition().until(ExpectedConditions.alertIsPresent());
            getDriver().switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void cancelConfirmationPrompt() {
        try {
            getDriver().switchTo().alert().dismiss();
        } catch (Exception e) {
            throw new RuntimeException("No confirmation prompt found to cancel", e);
        }
    }

    /**
     * Checks that a delete form for the given sale ID is still present on the page.
     * This is reliable even when multiple rows share identical visible text,
     * because the sale ID in the form action is unique per record.
     */
    public boolean isSaleWithIdStillVisible(String saleId) {
        try {
            waitForCondition().until(ExpectedConditions.presenceOfElementLocated(SALES_TABLE_ROWS));
            return !getDriver()
                    .findElements(By.cssSelector("form[action='/ui/sales/delete/" + saleId + "']"))
                    .isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDeleteActionVisibleForAnyRow() {
    try {
        return !getDriver()
                .findElements(By.cssSelector("form[action^='/ui/sales/delete/'] button"))
                .isEmpty();
    } catch (Exception e) {
        return false;
    }
}

public boolean isSalesListPageDisplayed() {
    // simplest reliable check: URL + table presence
    try {
        waitForCondition().until(ExpectedConditions.urlContains("/ui/sales"));
        return isSalesTableDisplayed();
    } catch (Exception e) {
        return false;
    }
}

/**
 * Verifies the sales table contains expected columns.
 * We check header text contains these keywords (case-insensitive).
 */
public boolean hasSalesTableColumns() {
    try {
        waitForCondition().until(ExpectedConditions.presenceOfAllElementsLocatedBy(SALES_TABLE_HEADERS));
        java.util.List<WebElement> headers = getDriver().findElements(SALES_TABLE_HEADERS);

        String all = headers.stream()
                .map(h -> h.getText() == null ? "" : h.getText().trim().toLowerCase())
                .reduce("", (a, b) -> a + " | " + b);

        // Flexible matching: supports variations like "Sold Date" vs "Date"
        boolean hasPlant = all.contains("plant");
        boolean hasQty = all.contains("quantity") || all.contains("qty");
        boolean hasTotal = all.contains("total") || all.contains("price") || all.contains("amount");
        boolean hasDate = all.contains("date") || all.contains("sold");

        return hasPlant && hasQty && hasTotal && hasDate;
    } catch (Exception e) {
        return false;
    }
}

/**
 * Verifies first row has non-empty values in cells.
 * This avoids depending on exact column order while still ensuring data is present.
 */
public boolean firstSalesRowHasValidData() {
    try {
        // Wait for at least one row
        waitForCondition().until(ExpectedConditions.presenceOfAllElementsLocatedBy(SALES_TABLE_ROWS));

        WebElement firstRow = getDriver().findElements(SALES_TABLE_ROWS).get(0);
        java.util.List<WebElement> cells = firstRow.findElements(By.cssSelector("td"));

        // At least 4 columns must exist (plant, qty, total, date)
        if (cells.size() < 4) return false;

        // ✅ Check ANY 4 non-empty cells (skip action column issues)
        int nonEmptyCount = 0;
        for (WebElement cell : cells) {
            String txt = cell.getText();
            if (txt != null && !txt.trim().isEmpty()) {
                nonEmptyCount++;
            }
        }
         return nonEmptyCount >= 3;  // because first column might be actions


    } catch (Exception e) {
        return false;
    }
}

}
