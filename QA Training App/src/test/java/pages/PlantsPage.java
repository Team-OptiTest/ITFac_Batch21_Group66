package pages;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Quotes;

public class PlantsPage {

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
                                        "//*[contains(text(), 'added successfully') or contains(text(), 'Plant added successfully')]"));

        public static final Target PLANTS_TABLE = Target.the("Plants table")
                        .located(By.cssSelector("table, .plants-table, [class*='table']"));

        public static Target plantInTable(String plantName) {
                return Target.the("Plant '" + plantName + "' in table")
                                .located(By.xpath("//table//td[contains(text(), " + Quotes.escape(plantName) + ")]"));
        }

        public static final Target PAGE_TITLE = Target.the("Page title")
                        .located(By.xpath("//h1 | //h2"));
}
