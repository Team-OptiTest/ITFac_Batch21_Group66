package pages;

import org.openqa.selenium.By;

import net.serenitybdd.core.pages.PageObject;

public class CategoryPage extends PageObject {
    
    private static final By ADD_CATEGORY_BUTTON_SELECTOR = By.xpath("/html/body/div[1]/div/div[2]/div[2]/form/div[3]/a[2]");
    private static final By SUCCESS_MESSAGE = By.xpath("//*[contains(@class, 'alert-success')]");
    private static final By SEARCH_INPUT_FIELD = By.xpath("/html/body/div[1]/div/div[2]/div[2]/form/div[1]/input");
    private static final By SEARCH_BUTTON = By.xpath("/html/body/div[1]/div/div[2]/div[2]/form/div[3]/button");
    private static final By PARENT_CATEGORY_DROPDOWN = By.name("parentId");
    
    public void navigateToCategoriesPage() {
        getDriver().get("http://localhost:8080/ui/categories");
    }

    public void navigateToAddCategoryPage() {
        getDriver().get("http://localhost:8080/ui/categories/add");
    }
    
    public boolean isAddCategoryButtonVisible() {
        return getDriver().findElement(ADD_CATEGORY_BUTTON_SELECTOR).isDisplayed();
    }

    public boolean isAddCategoryButtonNotVisible() {
        try {
            return !getDriver().findElement(ADD_CATEGORY_BUTTON_SELECTOR).isDisplayed();
        } catch (Exception e) {
            // Element not found means it's not visible
            return true;
        }
    }

    public void clickAddCategoryButton() {
        try {
            getDriver().findElement(ADD_CATEGORY_BUTTON_SELECTOR).click();
        } catch (Exception e) {
            throw new RuntimeException("Add Category button not found", e);
        }
    }

    public void clickParentCategoryFilterDropdown() {
        try {
            getDriver().findElement(PARENT_CATEGORY_DROPDOWN).click();
        } catch (Exception e) {
            throw new RuntimeException("Parent category dropdown not found", e);
        }
    }

    public void selectFromDropdown(String optionText) {
        try {
            getDriver().findElement(PARENT_CATEGORY_DROPDOWN).sendKeys(optionText);
        } catch (Exception e) {
            throw new RuntimeException("Parent category dropdown not found", e);
        }
    }
    
    public boolean isSuccessMessageDisplayed() {
        try {
            return getDriver().findElement(SUCCESS_MESSAGE).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCategoryVisibleInList(String categoryName) {
        try {
            return getDriver().findElement(By.xpath("//table//td[contains(text(), '" + categoryName + "')]")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCategoryListDisplayed() {
        try {
            return getDriver().findElement(By.xpath("//table | //div[contains(@class, 'category')]")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCategoryNotVisibleInList(String categoryName) {
        try {
            return !getDriver().findElement(By.xpath("//table//td[contains(text(), '" + categoryName + "')]")).isDisplayed();
        } catch (Exception e) {
            // Element not found means it's not visible
            return true;
        }
    }

    public void searchCategory(String searchTerm) {
        try {
            getDriver().findElement(SEARCH_INPUT_FIELD).clear();
            getDriver().findElement(SEARCH_INPUT_FIELD).sendKeys(searchTerm);
            getDriver().findElement(SEARCH_BUTTON).click();
        } catch (Exception e) {
            throw new RuntimeException("Search input or button not found", e);
        }
    }

    public void clickSearchInputField() {
        try {
            getDriver().findElement(SEARCH_INPUT_FIELD).click();
        } catch (Exception e) {
            throw new RuntimeException("Search input field not found", e);
        }
    }

    public void enterSearchTerm(String searchTerm) {
        try {
            getDriver().findElement(SEARCH_INPUT_FIELD).clear();
            getDriver().findElement(SEARCH_INPUT_FIELD).sendKeys(searchTerm);
        } catch (Exception e) {
            throw new RuntimeException("Search input field not found", e);
        }
    }

    public void clickSearchButton() {
        try {
            getDriver().findElement(SEARCH_BUTTON).click();
        } catch (Exception e) {
            throw new RuntimeException("Search button not found", e);
        }
    }

}

