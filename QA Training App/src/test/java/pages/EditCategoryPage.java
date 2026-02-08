package pages;

import org.openqa.selenium.By;
import net.serenitybdd.core.pages.PageObject;

public class EditCategoryPage extends PageObject {
    
    public void isUserInEditPage() {
        String currentUrl = getDriver().getCurrentUrl();
        if (!currentUrl.contains("edit")) {
            throw new RuntimeException("Not on the Edit Category page. Current URL: " + currentUrl);
        }
    }

    public boolean clickEditButtonForCategory(String optionText) {
        try {
            getDriver().findElement(By.xpath("//table//tr[td[contains(text(), '" + optionText + "')]]//a[contains(@href, 'edit')]")).click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void fillCategoryName(String categoryName) {
        try {
            getDriver().findElement(By.xpath("//input[@type='text' or not(@type)][@name or @id or @placeholder]")).clear();
            getDriver().findElement(By.xpath("//input[@type='text' or not(@type)][@name or @id or @placeholder]")).sendKeys(categoryName);
         } catch (Exception e) {
            throw new RuntimeException("Category name input field not found", e);
         }
    }
}