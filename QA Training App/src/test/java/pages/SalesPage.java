package pages;

import org.openqa.selenium.By;
import net.serenitybdd.core.pages.PageObject;

public class SalesPage extends PageObject {

    private static final By SELL_PLANT_BUTTON =
        By.cssSelector("body > div > div > div.main-content > div:nth-child(2) > a");

    public void navigateToSalesPage() {
        getDriver().get("http://localhost:8080/ui/sales");
    }

    public boolean isSellPlantButtonVisible() {
        try {
            return getDriver().findElement(SELL_PLANT_BUTTON).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
