package pages;

import org.openqa.selenium.By;
import net.serenitybdd.core.pages.PageObject;

public class AddCategoryPage extends PageObject {
    private static final By CATEGORY_NAME_FIELD = By.xpath("/html/body/div/div/div[2]/div[2]/form/div[1]/input");
    private static final By SAVE_BUTTON = By.xpath("/html/body/div/div/div[2]/div[2]/form/button");
    private static final By VALIDATION_ERROR_MESSAGE = By.xpath("/html/body/div/div/div[2]/div[2]/form/div[1]/div");

     public void fillCategoryName(String categoryName) {
        getDriver().findElement(CATEGORY_NAME_FIELD).clear();
        getDriver().findElement(CATEGORY_NAME_FIELD).sendKeys(categoryName);
    }

    public void clickSaveButton() {
        getDriver().findElement(SAVE_BUTTON).click();
    }

    public boolean isValidationErrorMessageDisplayed() {
        try {
            return getDriver().findElement(VALIDATION_ERROR_MESSAGE).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
