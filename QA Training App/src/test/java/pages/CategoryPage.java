package pages;

import org.openqa.selenium.By;

import net.serenitybdd.core.pages.PageObject;

public class CategoryPage extends PageObject {

    // Locators based on actual HTML structure
    private static final By PARENT_CATEGORY_DROPDOWN = By.xpath("//select[@name='parentId'] | //select[@id='parentId'] | //form//select");
    private static final By ADD_CATEGORY_BUTTON_SELECTOR = By.xpath("//a[contains(text(),'Add A Category')] | //a[contains(@href,'/categories/add')]");
    private static final By SUCCESS_MESSAGE = By.cssSelector(".alert-success");
    private static final By SEARCH_INPUT_FIELD = By.name("name");
    private static final By SEARCH_BUTTON = By.cssSelector("button.btn-primary[type='submit']");
    private static final By CATEGORY_TABLE_BODY = By.cssSelector("table tbody");

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

    public boolean isMessageDisplayedInTableBody(String messageText) {
        try {
            return getDriver().findElement(CATEGORY_TABLE_BODY).getText().contains(messageText);
        } catch (Exception e) {
            return false;
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
