package pages;

import org.openqa.selenium.By;

import net.serenitybdd.core.pages.PageObject;

public class CategoryPage extends PageObject {
    
    private static final By ADD_CATEGORY_BUTTON_SELECTOR = By.xpath("/html/body/div[1]/div/div[2]/div[2]/form/div[3]/a[2]");
    
    public void navigateToCategoriesPage() {
        getDriver().get("http://localhost:8080/ui/categories");
    }
    
    public boolean isAddCategoryButtonVisible() {
        return getDriver().findElement(ADD_CATEGORY_BUTTON_SELECTOR).isDisplayed();
    }
    
}
