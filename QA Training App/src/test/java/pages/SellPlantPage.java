package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;


import net.serenitybdd.core.pages.PageObject;

public class SellPlantPage extends PageObject {

    // Page heading
    private static final By PAGE_TITLE =
            By.xpath("//h3[text()='Sell Plant']");

    // Plant dropdown
   
    private static final By PLANT_DROPDOWN = By.id("plantId");

    // Quantity input
     private static final By QUANTITY_FIELD = By.id("quantity");

    // Cancel button
    private static final By CANCEL_BUTTON =
            By.cssSelector("a.btn-secondary[href$='/ui/sales'], a.btn-outline-secondary[href$='/ui/sales']");
     // Submit button
    private static final By SELL_BUTTON =
            By.cssSelector("form[action='/ui/sales'] button.btn.btn-primary");
    
    // Plant error message: select + next sibling div.text-danger
    private static final By PLANT_ERROR_MESSAGE = By.cssSelector("select#plantId + div.text-danger");

    private static final By QUANTITY_ERROR_MESSAGE =
        By.xpath("//input[@id='quantity']/following-sibling::div[contains(@class,'text-danger')]");



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

    public void clickSellButton() {
    waitForCondition().until(ExpectedConditions.elementToBeClickable(SELL_BUTTON));
    $(SELL_BUTTON).click();
}

public boolean isPlantRequiredMessageDisplayed() {
        try {
            waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(PLANT_ERROR_MESSAGE));
            String msg = $(PLANT_ERROR_MESSAGE).getText().trim();
            return msg.equalsIgnoreCase("Plant is required");
        } catch (Exception e) {
            return false;
        }

    }
public void leavePlantDropdownEmpty() {
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(PLANT_DROPDOWN));
        WebElement dropdown = getDriver().findElement(PLANT_DROPDOWN);
        Select select = new Select(dropdown);
        select.selectByIndex(0); // "-- Select Plant --" (value="")
    }

   public boolean isQuantityGreaterThanZeroMessageDisplayed() {
    try {
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(QUANTITY_ERROR_MESSAGE));
        String message = $(QUANTITY_ERROR_MESSAGE).getText().trim().toLowerCase();
        return message.contains("quantity") && message.contains("greater") && message.contains("0");
    } catch (Exception e) {
        return false;
    }
}

//  Set quantity using JS (so browser won't auto-correct)
public void setQuantityUsingJs(String value) {
    waitForCondition().until(ExpectedConditions.presenceOfElementLocated(QUANTITY_FIELD));
    JavascriptExecutor js = (JavascriptExecutor) getDriver();
    js.executeScript("arguments[0].value = arguments[1];", getDriver().findElement(QUANTITY_FIELD), value);
}

//  Submit form using JS (bypasses HTML5 validation)
public void submitSaleFormBypassingHtmlValidation() {
    JavascriptExecutor js = (JavascriptExecutor) getDriver();
    js.executeScript("document.querySelector(\"form[action='/ui/sales']\").submit();");
}



}
