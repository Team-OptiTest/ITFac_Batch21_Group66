package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import net.serenitybdd.core.pages.PageObject;

public class SellPlantPage extends PageObject {

    // Page heading
    private static final By PAGE_TITLE =
            By.xpath("//h3[text()='Sell Plant']");

    // Plant dropdown
   
    private static final By PLANT_DROPDOWN = By.id("plantId");

    private static final By PLANT_DROPDOWN =
            By.id("plantId");

    // Quantity input
    private static final By QUANTITY_FIELD =
            By.cssSelector("input[type='number']");

    // Cancel button
    private static final By CANCEL_BUTTON =
            By.cssSelector("a.btn-secondary[href$='/ui/sales'], a.btn-outline-secondary[href$='/ui/sales']");

    public boolean isSellPlantPageDisplayed() {
        try {
            return $(PAGE_TITLE).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantDropdownDisplayed() {
        try {
            return $(PLANT_DROPDOWN).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isQuantityFieldDisplayed() {
        try {
            return $(QUANTITY_FIELD).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectFirstAvailablePlant() {
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(PLANT_DROPDOWN));
        WebElement dropdown = getDriver().findElement(PLANT_DROPDOWN);
        Select select = new Select(dropdown);
        // Select the first non-placeholder option
        if (select.getOptions().size() > 1) {
            select.selectByIndex(1);
        } else {
            throw new IllegalStateException("No plants available in dropdown to select");
        }
    }

    public void enterQuantity(String quantity) {
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(QUANTITY_FIELD));
        $(QUANTITY_FIELD).clear();
        $(QUANTITY_FIELD).type(quantity);
    }

    public void clickCancelButton() {
        waitForCondition().until(ExpectedConditions.elementToBeClickable(CANCEL_BUTTON));
        $(CANCEL_BUTTON).click();
    }

    public boolean isOnSalesPage() {
        try {
            waitForCondition().until(ExpectedConditions.urlContains("/ui/sales"));
            String currentUrl = getDriver().getCurrentUrl();
            return currentUrl.contains("/ui/sales") && !currentUrl.contains("/ui/sales/new");
        } catch (Exception e) {
            return false;
        }
    }
}
