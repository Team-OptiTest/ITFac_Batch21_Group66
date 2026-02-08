package pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class PlantsPage extends PageObject {

        // Locators based on actual HTML structure
        private static final By ADD_PLANT_BUTTON = By
                        .xpath("//a[contains(text(), 'Add a Plant')] | //a[contains(@href, '/plants/add')]");
        private static final By PLANT_NAME_FIELD = By.id("name");
        private static final By CATEGORY_DROPDOWN = By.id("categoryId");
        private static final By PRICE_FIELD = By.id("price");
        private static final By QUANTITY_FIELD = By.id("quantity");
        private static final By FALLBACK_SAVE_BUTTON = By
                        .xpath("//button[contains(text(), 'Save')] | //button[@class='btn btn-primary']");
        private static final By SAVE_BUTTON = By.cssSelector("button[type='submit'], button.btn-primary, form button");

        private static final By SUCCESS_MESSAGE = By.cssSelector(".alert-success");
        private static final By PLANTS_TABLE = By.cssSelector("table, .plants-table, [class*='table']");
        private static final By PAGE_TITLE = By.xpath("//h1 | //h2");
        private static final By PLANT_NAME_ERROR = By.xpath("//*[contains(text(), 'Plant name is required')]");
        private static final By PRICE_ERROR = By.xpath("//*[contains(text(), 'Price is required')]");
        private static final By SEARCH_FIELD = By
                        .xpath("//input[@placeholder='Search plant' or @id='searchName' or @name='searchName']");
        private static final By SEARCH_BUTTON = By
                        .xpath("//button[contains(text(), 'Search')] | //button[@type='submit']");
        private static final By FIRST_PLANT_NAME = By.xpath("//table//tbody//tr[1]//td[1]");
        private static final By FILTER_CATEGORY_DROPDOWN = By.cssSelector(
                        "select#category, select#categoryId, select#categoryFilter, select[name='category']");
        private static final By PLANT_TABLE_BODY = By.cssSelector("table tbody");
        private static final By PLANT_ROWS = By.xpath("//table/tbody/tr");
        private static final By EDIT_BUTTONS = By.xpath(
                        "//table//button[contains(text(), 'Edit') or contains(@title, 'Edit')] | //table//a[contains(text(), 'Edit')]");
        private static final By DELETE_BUTTONS = By.xpath(
                        "//table//button[contains(text(), 'Delete') or contains(@title, 'Delete')] | //table//a[contains(text(), 'Delete')]");
        private static final By ACTIONS_HEADER = By.xpath("//th[contains(text(), 'Actions')]");

        // Navigation methods
        public void navigateToPlantsPage() {
                getDriver().get("http://localhost:8080/ui/plants");
                waitForCondition().until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        }

        public void navigateToAddPlantPage() {
                getDriver().get("http://localhost:8080/ui/plants/add");
        }

        // Button visibility and click methods
        public boolean isAddPlantButtonVisible() {
                try {
                        return getDriver().findElement(ADD_PLANT_BUTTON).isDisplayed();
                } catch (Exception e) {
                        return false;
                }
        }

        public boolean isAddPlantButtonNotVisible() {
                try {
                        return !getDriver().findElement(ADD_PLANT_BUTTON).isDisplayed();
                } catch (Exception e) {
                        return true;
                }
        }

        public void clickAddPlantButton() {
                try {
                        getDriver().findElement(ADD_PLANT_BUTTON).click();
                } catch (Exception e) {
                        getDriver().manage().window().maximize();
                        getDriver().findElement(ADD_PLANT_BUTTON).click();
                        throw new RuntimeException("Add Plant button not found", e);
                }
        }

        public void clickSaveButton() {
                try {
                        getDriver().findElement(SAVE_BUTTON).click();
                } catch (Exception e) {
                        getDriver().findElement(By.tagName("body")).sendKeys(Keys.PAGE_DOWN);
                        getDriver().findElement(FALLBACK_SAVE_BUTTON).click();
                        throw new RuntimeException("Save button not found", e);
                }
        }

        // Form input methods
        public void enterPlantName(String plantName) {
                try {
                        getDriver().findElement(PLANT_NAME_FIELD).clear();
                        getDriver().findElement(PLANT_NAME_FIELD).sendKeys(plantName);
                } catch (Exception e) {
                        throw new RuntimeException("Plant name field not found", e);
                }
        }

        public void enterPrice(String price) {
                try {
                        getDriver().findElement(PRICE_FIELD).clear();
                        getDriver().findElement(PRICE_FIELD).sendKeys(price);
                } catch (Exception e) {
                        throw new RuntimeException("Price field not found", e);
                }
        }

        public void enterQuantity(String quantity) {
                try {
                        getDriver().findElement(QUANTITY_FIELD).clear();
                        getDriver().findElement(QUANTITY_FIELD).sendKeys(quantity);
                } catch (Exception e) {
                        throw new RuntimeException("Quantity field not found", e);
                }
        }

        public void selectCategory(String category) {
                try {
                        getDriver().findElement(CATEGORY_DROPDOWN).sendKeys(category);
                } catch (Exception e) {
                        throw new RuntimeException("Category dropdown not found", e);
                }
        }

        public void clickCategoryDropdown() {
                try {
                        getDriver().findElement(CATEGORY_DROPDOWN).click();
                } catch (Exception e) {
                        throw new RuntimeException("Category dropdown not found", e);
                }
        }

        // Success message methods
        public boolean isSuccessMessageDisplayed() {
                try {
                        return getDriver().findElement(SUCCESS_MESSAGE).isDisplayed();
                } catch (Exception e) {
                        return false;
                }
        }

        public String getSuccessMessageText() {
                try {
                        return getDriver().findElement(SUCCESS_MESSAGE).getText();
                } catch (Exception e) {
                        return "";
                }
        }

        // Error message methods
        public boolean isPlantNameErrorDisplayed() {
                try {
                        return getDriver().findElement(PLANT_NAME_ERROR).isDisplayed();
                } catch (Exception e) {
                        return false;
                }
        }

        public boolean isPriceErrorDisplayed() {
                try {
                        return getDriver().findElement(PRICE_ERROR).isDisplayed();
                } catch (Exception e) {
                        return false;
                }
        }

        // Plant list verification methods
        public boolean isPlantVisibleInTable(String plantName) {
                try {
                        return getDriver().findElement(By.xpath("//table//td[contains(text(), '" + plantName + "')]"))
                                        .isDisplayed();
                } catch (Exception e) {
                        return false;
                }
        }

        public boolean isPlantNotVisibleInTable(String plantName) {
                try {
                        return !getDriver().findElement(By.xpath("//table//td[contains(text(), '" + plantName + "')]"))
                                        .isDisplayed();
                } catch (Exception e) {
                        return true;
                }
        }

        public boolean isPlantsTableDisplayed() {
                try {
                        return getDriver().findElement(PLANTS_TABLE).isDisplayed();
                } catch (Exception e) {
                        return false;
                }
        }

        // Search methods
        public void searchPlant(String searchTerm) {
                try {
                        getDriver().findElement(SEARCH_FIELD).clear();
                        getDriver().findElement(SEARCH_FIELD).sendKeys(searchTerm);
                        getDriver().findElement(SEARCH_BUTTON).click();
                } catch (Exception e) {
                        throw new RuntimeException("Search input or button not found", e);
                }
        }

        public void clickSearchInputField() {
                try {
                        getDriver().findElement(SEARCH_FIELD).click();
                } catch (Exception e) {
                        throw new RuntimeException("Search input field not found", e);
                }
        }

        public void enterSearchTerm(String searchTerm) {
                try {
                        getDriver().findElement(SEARCH_FIELD).clear();
                        getDriver().findElement(SEARCH_FIELD).sendKeys(searchTerm);
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
                        return getDriver().findElement(PLANT_TABLE_BODY).getText().contains(messageText);
                } catch (Exception e) {
                        return false;
                }
        }

        // Filter methods
        public void clickFilterCategoryDropdown() {
                try {
                        getDriver().findElement(FILTER_CATEGORY_DROPDOWN).click();
                } catch (Exception e) {
                        throw new RuntimeException("Filter category dropdown not found", e);
                }
        }

        public void selectFilterCategory(String category) {
                try {
                        getDriver().findElement(FILTER_CATEGORY_DROPDOWN).sendKeys(category);
                } catch (Exception e) {
                        throw new RuntimeException("Filter category dropdown not found", e);
                }
        }

        // Edit and Delete methods
        public void clickEditButtonForPlant(String plantName) {
                try {
                        By editButton = By.xpath("//table//tr[td[contains(text(), '" + plantName
                                        + "')]]//button[contains(@title, 'Edit') or contains(text(), 'Edit')] | //table//tr[td[contains(text(), '"
                                        + plantName + "')]]//a[contains(@title, 'Edit') or contains(text(), 'Edit')]");
                        getDriver().findElement(editButton).click();
                } catch (Exception e) {
                        throw new RuntimeException("Edit button for plant '" + plantName + "' not found", e);
                }
        }

        public void clickDeleteButtonForPlant(String plantName) {
                try {
                        By deleteButton = By.xpath("//table//tr[td[contains(text(), '" + plantName
                                        + "')]]//button[contains(@title, 'Delete')]");
                        getDriver().findElement(deleteButton).click();
                } catch (Exception e) {
                        throw new RuntimeException("Delete button for plant '" + plantName + "' not found", e);
                }
        }

        public boolean isEditButtonVisibleForPlant(String plantName) {
                try {
                        By editButton = By.xpath("//table//tr[td[contains(text(), '" + plantName
                                        + "')]]//button[contains(@title, 'Edit') or contains(text(), 'Edit')] | //table//tr[td[contains(text(), '"
                                        + plantName + "')]]//a[contains(@title, 'Edit') or contains(text(), 'Edit')]");
                        return getDriver().findElement(editButton).isDisplayed();
                } catch (Exception e) {
                        return false;
                }
        }

        public boolean isDeleteButtonVisibleForPlant(String plantName) {
                try {
                        By deleteButton = By.xpath("//table//tr[td[contains(text(), '" + plantName
                                        + "')]]//button[contains(@title, 'Delete')]");
                        return getDriver().findElement(deleteButton).isDisplayed();
                } catch (Exception e) {
                        return false;
                }
        }

        // Verification methods
        public boolean areEditOrDeleteButtonsVisible() {
                try {
                        return !getDriver().findElements(EDIT_BUTTONS).isEmpty()
                                        || !getDriver().findElements(DELETE_BUTTONS).isEmpty();
                } catch (Exception e) {
                        return false;
                }
        }

        public boolean isActionsColumnPresent() {
                try {
                        return !getDriver().findElements(ACTIONS_HEADER).isEmpty();
                } catch (Exception e) {
                        return false;
                }
        }

        public String getFirstPlantName() {
                try {
                        return getDriver().findElement(FIRST_PLANT_NAME).getText();
                } catch (Exception e) {
                        return null;
                }
        }

        public boolean isOnPlantsListPage() {
                try {
                        String currentUrl = getDriver().getCurrentUrl();
                        return currentUrl.contains("/ui/plants");
                } catch (Exception e) {
                        return false;
                }
        }

        // Advanced verification methods
        public boolean plantShowsPriceAndQuantity(String plantName, String price, String quantity) {
                try {
                        By plantRow = By.xpath("//table//tr[td[contains(text(), '" + plantName + "')]]");
                        String rowText = getDriver().findElement(plantRow).getText();
                        return rowText.contains(price) && rowText.contains(quantity);
                } catch (Exception e) {
                        return false;
                }
        }

        public boolean allVisiblePlantsMatch(String searchTerm) {
                try {
                        List<WebElementFacade> rows = findAll(PLANT_ROWS);
                        if (rows.isEmpty()) {
                                return true;
                        }

                        for (WebElementFacade row : rows) {
                                if (!row.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
                                        return false;
                                }
                        }
                        return true;
                } catch (Exception e) {
                        return false;
                }
        }

        public boolean allVisiblePlantsBelongToCategory(String category) {
                try {
                        List<WebElementFacade> rows = findAll(PLANT_ROWS);
                        if (rows.isEmpty()) {
                                return true;
                        }

                        for (WebElementFacade row : rows) {
                                if (!row.getText().toLowerCase().contains(category.toLowerCase())) {
                                        return false;
                                }
                        }
                        return true;
                } catch (Exception e) {
                        return false;
                }
        }

        // Page title verification
        public String getPageTitle() {
                try {
                        return getDriver().findElement(PAGE_TITLE).getText();
                } catch (Exception e) {
                        return "";
                }
        }

        public boolean isPageTitleDisplayed() {
                try {
                        return getDriver().findElement(PAGE_TITLE).isDisplayed();
                } catch (Exception e) {
                        return false;
                }
        }
}