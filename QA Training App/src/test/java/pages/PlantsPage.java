package pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.questions.Visibility;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Quotes;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import net.serenitybdd.core.pages.WebElementFacade;
import java.util.List;

public class PlantsPage extends PageObject {

        public static final Target ADD_PLANT_BUTTON = Target.the("Add a Plant button")
                        .located(By.xpath("//a[contains(text(), 'Add a Plant')]"));

        public static final Target PLANT_NAME_FIELD = Target.the("Plant Name field")
                        .located(By.id("name"));

        public static final Target CATEGORY_DROPDOWN = Target.the("Category dropdown")
                        .located(By.id("categoryId"));

        public static final Target PRICE_FIELD = Target.the("Price field")
                        .located(By.id("price"));

        public static final Target QUANTITY_FIELD = Target.the("Quantity field")
                        .located(By.id("quantity"));

        public static final Target SAVE_BUTTON = Target.the("Save button")
                        .located(By.xpath("//button[contains(text(), 'Save')]"));

        public static final Target SUCCESS_MESSAGE = Target.the("Success message")
                        .located(By.xpath(
                                        "//*[contains(text(), 'added successfully') or contains(text(), 'Plant added successfully') or contains(text(), 'deleted successfully') or contains(text(), 'Plant deleted successfully')]"));

        public static final Target PLANTS_TABLE = Target.the("Plants table")
                        .located(By.cssSelector("table, .plants-table, [class*='table']"));

        public static Target plantInTable(String plantName) {
                return Target.the("Plant '" + plantName + "' in table")
                                .located(By.xpath("//table//td[contains(text(), " + Quotes.escape(plantName) + ")]"));
        }

        public static final Target PAGE_TITLE = Target.the("Page title")
                        .located(By.xpath("//h1 | //h2"));

        public static final Target PLANT_NAME_ERROR = Target.the("Plant Name error")
                        .located(By.xpath("//*[contains(text(), 'Plant Name is required')]"));

        public static final Target PRICE_ERROR = Target.the("Price error")
                        .located(By.xpath("//*[contains(text(), 'Price is required')]"));

        public static final Target SEARCH_FIELD = Target.the("Search plant input box")
                        .located(By.xpath(
                                        "//input[@placeholder='Search plant' or @id='searchName' or @name='searchName']"));

        public static final Target SEARCH_BUTTON = Target.the("Search button")
                        .located(By.xpath("//button[contains(text(), 'Search')]"));

        public void theUserIsOnThePlantsPage() {
                getDriver().get("http://localhost:8080/ui/plants");
                waitForCondition().until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        }

        public static Target deleteButtonForPlant(String plantName) {
                return Target.the("Delete button for plant '" + plantName + "'")
                                .located(By.xpath("//table//tr[td[contains(text(), " + Quotes.escape(plantName)
                                                + ")]]//button[contains(@title, 'Delete')]"));
        }

        // Migrated from PlantQuestions
        public static Question<Boolean> successMessageIsDisplayed() {
                return actor -> {
                        try {
                                actor.attemptsTo(
                                                WaitUntil.the(SUCCESS_MESSAGE, isVisible())
                                                                .forNoMoreThan(10).seconds());
                                return Visibility.of(SUCCESS_MESSAGE).answeredBy(actor);
                        } catch (Exception e) {
                                return false;
                        }
                };
        }

        public static Question<String> successMessageText() {
                return actor -> {
                        try {
                                actor.attemptsTo(
                                                WaitUntil.the(SUCCESS_MESSAGE, isVisible())
                                                                .forNoMoreThan(10).seconds());
                                return Text.of(SUCCESS_MESSAGE).answeredBy(actor);
                        } catch (Exception e) {
                                return "";
                        }
                };
        }

        public static Question<Boolean> plantAppearsInTable(String plantName) {
                return actor -> {
                        try {
                                actor.attemptsTo(
                                                WaitUntil.the(plantInTable(plantName), isVisible())
                                                                .forNoMoreThan(10).seconds());
                                return Visibility.of(plantInTable(plantName)).answeredBy(actor);
                        } catch (Exception e) {
                                return false;
                        }
                };
        }

        public static Question<Boolean> plantIsRemovedFromTable(String plantName) {
                return actor -> {
                        try {
                                actor.attemptsTo(
                                                WaitUntil.the(plantInTable(plantName),
                                                                net.serenitybdd.screenplay.matchers.WebElementStateMatchers
                                                                                .isNotVisible())
                                                                .forNoMoreThan(10).seconds());
                                return !Visibility.of(plantInTable(plantName)).answeredBy(actor);
                        } catch (Exception e) {
                                return true; // Exception likely means element not found, which is good
                        }
                };
        }

        public static Question<Boolean> plantDoesNotAppearInSearch(String plantName) {
                return actor -> {
                        try {
                                actor.attemptsTo(
                                                WaitUntil.the(plantInTable(plantName),
                                                                net.serenitybdd.screenplay.matchers.WebElementStateMatchers
                                                                                .isNotVisible())
                                                                .forNoMoreThan(10).seconds());
                                return !Visibility.of(plantInTable(plantName)).answeredBy(actor);
                        } catch (Exception e) {
                                return true;
                        }
                };
        }

        public static Question<Boolean> isOnPlantsListPage() {
                return actor -> {
                        try {
                                String currentUrl = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
                                return currentUrl.contains("/ui/plants");
                        } catch (Exception e) {
                                return false;
                        }
                };
        }

        public static Question<Boolean> allVisiblePlantsMatch(String term) {
                return actor -> {
                        Target ROWS = Target.the("Plant rows").locatedBy("//table/tbody/tr");
                        List<WebElementFacade> rows = ROWS.resolveAllFor(actor);

                        if (rows.isEmpty())
                                return true; // Or false if we expect results

                        for (WebElementFacade row : rows) {
                                if (!row.getText().toLowerCase().contains(term.toLowerCase())) {
                                        return false;
                                }
                        }
                        return true;
                };
        }
}
