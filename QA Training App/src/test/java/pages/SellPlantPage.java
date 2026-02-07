package pages;

import org.openqa.selenium.By;
import net.serenitybdd.core.pages.PageObject;

public class SellPlantPage extends PageObject {

    // Page heading
    private static final By PAGE_TITLE =
            By.xpath("//h3[text()='Sell Plant']");

    // Plant dropdown
   
    private static final By PLANT_DROPDOWN = By.id("plantId");


    // Quantity input
    private static final By QUANTITY_FIELD = By.id("quantity");

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
}
