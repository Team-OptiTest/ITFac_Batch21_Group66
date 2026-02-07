package pages;

import org.openqa.selenium.By;

import net.serenitybdd.core.pages.PageObject;

public class CategoryPage extends PageObject {
    
    private static final By ADD_CATEGORY_BUTTON_SELECTOR = By.xpath("/html/body/div[1]/div/div[2]/div[2]/form/div[3]/a[2]");
    private static final By SUCCESS_MESSAGE = By.xpath("//*[contains(@class, 'alert-success')]");
    
    public void navigateToCategoriesPage() {
        getDriver().get("http://localhost:8080/ui/categories");
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
        } catch (Exception e1) {
            try {
                By altSelector1 = By.xpath("//a[contains(text(), 'Add a category')]");
                getDriver().findElement(altSelector1).click();
            } catch (Exception e2) {
                try {
                    By altSelector2 = By.xpath("//button[contains(text(), 'Add a category')]");
                    getDriver().findElement(altSelector2).click();
                } catch (Exception e3) {
                    try {
                        By altSelector3 = By.xpath("//*[@class='btn btn-primary']");
                        getDriver().findElement(altSelector3).click();
                    } catch (Exception e4) {
                        throw new RuntimeException("Could not find 'Add Category' button with any selector", e1);
                    }
                }
            }
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

}
