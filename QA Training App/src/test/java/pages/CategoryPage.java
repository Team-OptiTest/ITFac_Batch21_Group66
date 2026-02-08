package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import net.serenitybdd.core.pages.PageObject;

public class CategoryPage extends PageObject {

    // Locators based on actual HTML structure
    private static final By PARENT_CATEGORY_DROPDOWN = By.xpath("//select");
    private static final By ADD_CATEGORY_BUTTON_SELECTOR = By.xpath("//a[normalize-space()='Add A Category']");
    private static final By SUCCESS_MESSAGE = By.xpath("//div[contains(@class, 'alert-success')]");
    private static final By SEARCH_INPUT_FIELD = By.xpath("//input[@name='name']");
    private static final By SEARCH_BUTTON = By.xpath("//button[contains(@class,'btn-primary')][@type='submit'] | //button[@type='submit']");
    private static final By CATEGORY_TABLE_BODY = By.xpath("//table//tbody");
    
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

    public boolean categoryDeleteButtonsDisabledForUser() {
        try {
            return getDriver().findElements(By.xpath("//table//button[contains(@class, 'delete')]")).stream()
                    .allMatch(button -> !button.isEnabled());
        } catch (Exception e) {
            // If delete buttons are not found, we can consider them as disabled for the user
            return true;
        }
    }

    public boolean categoryEditButtonsDisabledForUser() {
        try {
            return getDriver().findElements(By.xpath("//table//a[contains(@href, 'edit')]")).stream()
                    .allMatch(button -> !button.isEnabled());
        } catch (Exception e) {
            // If edit buttons are not found, we can consider them as disabled for the user
            return true;
        }
    }

    public boolean isCategoryVisibleInList(String categoryName) {
        try {
            return getDriver().findElement(By.xpath("//table//td[text()='" + categoryName + "']")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCategoryListDisplayed() {
        try {
            return getDriver().findElement(By.xpath("//table")).isDisplayed();
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

    public boolean isMessageDisplayedInTableBody(String messageText) {
        try {
            return getDriver().findElement(CATEGORY_TABLE_BODY).getText().contains(messageText);
        } catch (Exception e) {
            return false;
        }
    }
  
    public void clickDeleteButtonForCategory(String categoryName) {
        try {
            // Locate the delete button
            WebElement deleteButton = getDriver().findElement(
                By.xpath("//table//tr[td[normalize-space()='" + categoryName + "']]//form[contains(@action,'/ui/categories/delete')]//button")
            );
            
            // Scroll the element into view
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", deleteButton);
            
            // Wait a bit for any animations
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Try normal click first
            try {
                deleteButton.click();
            } catch (ElementClickInterceptedException e) {
                // If intercepted, use JavaScript click
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", deleteButton);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Delete button for category '" + categoryName + "' not found or not clickable", e);
        }
    }

    public void confirmDeletion() {
        try {
            // Wait for alert and accept it
            waitABit(500); // Wait for dialog to appear
            getDriver().switchTo().alert().accept();
        } catch (Exception e) {
            // If it's not a browser alert, it might be a modal dialog
            try {
                // Click OK button in the modal
                WebElement okButton = getDriver().findElement(By.xpath("//button[text()='OK' or contains(text(), 'OK')]"));
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", okButton);
            } catch (Exception ex) {
                throw new RuntimeException("Could not confirm deletion dialog", ex);
            }
        }
    }
}
    public void navigateToAddCategoryPageDirectly() {
        String baseUrl = "http://localhost:8080"; // or get from environment
        getDriver().get(baseUrl + "/ui/categories/add");
    }

    public boolean isRedirectedFromAddCategoryPage() {
        String currentUrl = getDriver().getCurrentUrl();
        boolean notOnAddPage = !currentUrl.contains("/ui/categories/add");
        boolean onDashboard = currentUrl.contains("/ui/dashboard");
        boolean onError = currentUrl.contains("403") || currentUrl.contains("forbidden");
        return notOnAddPage && (onDashboard || onError);
    }

    public boolean isAccessDeniedMessageDisplayed() {
        String pageSource = getDriver().getPageSource().toLowerCase();
        boolean hasAccessText = pageSource.contains("access denied")
                || pageSource.contains("forbidden")
                || pageSource.contains("403");
        return hasAccessText;
    }
}
