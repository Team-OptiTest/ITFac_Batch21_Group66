package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import net.serenitybdd.core.pages.PageObject;

public class EditCategoryPage extends PageObject {
    
    public void isUserInEditPage() {
        String currentUrl = getDriver().getCurrentUrl();
        if (!currentUrl.contains("edit")) {
            throw new RuntimeException("Not on the Edit Category page. Current URL: " + currentUrl);
        }
    }

    public void clickEditButtonForCategory(String categoryName) {
        try {
            // Locate the edit button/link
            WebElement editButton = getDriver().findElement(
                By.xpath("//table//tr[td[normalize-space()='" + categoryName + "']]//a[contains(@href,'/ui/categories/edit')]")
            );
            
            // Scroll the element into view
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", editButton);
            
            // Wait a bit for any animations
            waitABit(500);
            
            // Try normal click first
            try {
                editButton.click();
            } catch (ElementClickInterceptedException e) {
                // If intercepted, use JavaScript click
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", editButton);
            }
            
            // Wait for navigation
            waitABit(1000);
            
        } catch (Exception e) {
            throw new RuntimeException("Edit button for category '" + categoryName + "' not found or not clickable", e);
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