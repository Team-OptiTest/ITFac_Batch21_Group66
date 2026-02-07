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
    private static final By PLANT_DROPDOWN =
            By.id("plantId");

    // Quantity input
    private static final By QUANTITY_FIELD =
            By.cssSelector("input[type='number']");

    // Cancel button
    private static final By CANCEL_BUTTON =
            By.cssSelector("a.btn-secondary, a.btn-outline-secondary, a[href*='/ui/sales']");

    /**
     * Determines whether the Sell Plant page title is currently visible.
     *
     * @return true if the Sell Plant page title is visible, false otherwise (including when an exception occurs)
     */
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

    /**
     * Checks whether the quantity input field is visible on the page.
     *
     * @return `true` if the quantity input is visible, `false` otherwise (returns `false` if an error occurs while checking visibility).
     */
    public boolean isQuantityFieldDisplayed() {
        try {
            return $(QUANTITY_FIELD).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Selects the first available plant option from the plant dropdown.
     *
     * Waits for the plant dropdown to be present; if there is at least one selectable option
     * besides the placeholder, selects that option by index, otherwise leaves the selection unchanged.
     */
    public void selectFirstAvailablePlant() {
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(PLANT_DROPDOWN));
        WebElement dropdown = getDriver().findElement(PLANT_DROPDOWN);
        Select select = new Select(dropdown);
        // Select the first non-placeholder option
        if (select.getOptions().size() > 1) {
            select.selectByIndex(1);
        }
    }

    /**
     * Set the quantity value in the page's quantity input field.
     *
     * Waits for the quantity field to be present, clears any existing value, and types the provided quantity.
     *
     * @param quantity the value to enter into the quantity input (provided as a string)
     */
    public void enterQuantity(String quantity) {
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(QUANTITY_FIELD));
        $(QUANTITY_FIELD).clear();
        $(QUANTITY_FIELD).type(quantity);
    }

    /**
     * Waits for the page's cancel button to become clickable and clicks it.
     */
    public void clickCancelButton() {
        waitForCondition().until(ExpectedConditions.elementToBeClickable(CANCEL_BUTTON));
        $(CANCEL_BUTTON).click();
    }

    /**
     * Checks whether the current browser URL corresponds to the sales listing page (excluding the "/ui/sales/new" path).
     *
     * @return true if the current URL contains "/ui/sales" and does not contain "/ui/sales/new", false otherwise.
     */
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