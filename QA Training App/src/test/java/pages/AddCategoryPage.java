package pages;

import org.openqa.selenium.By;
import net.serenitybdd.core.pages.PageObject;

public class AddCategoryPage extends PageObject {
    // XPath locators for Add Category form
    private static final By CATEGORY_NAME_FIELD = By.xpath("//input[@id='name'] | //input[@name='name'] | //label[contains(text(),'Category Name') or contains(text(),'Name')]/../input | //form//input[@type='text'][1]");
    private static final By PARENT_CATEGORY_SELECT_DROPDOWN = By.cssSelector("select#parentId, select[name='parentId'], select[name='parent'], form select");
    private static final By SAVE_BUTTON = By.cssSelector("button[type='submit'], button.btn-primary, form button");
    private static final By VALIDATION_ERROR_MESSAGE = By.cssSelector(".invalid-feedback, .alert-danger");

    public void fillCategoryName(String categoryName) {
        getDriver().findElement(CATEGORY_NAME_FIELD).clear();
        getDriver().findElement(CATEGORY_NAME_FIELD).sendKeys(categoryName);
    }

    public void selectParentCategory(String parentCategory) {
        getDriver().findElement(PARENT_CATEGORY_SELECT_DROPDOWN).sendKeys(parentCategory);
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
