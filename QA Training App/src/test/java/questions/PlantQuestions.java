package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.questions.Visibility;
import pages.PlantsPage;

public class PlantQuestions {

    public static Question<Boolean> successMessageIsDisplayed() {
        return actor -> Visibility.of(PlantsPage.SUCCESS_MESSAGE).answeredBy(actor);
    }

    public static Question<String> successMessageText() {
        return actor -> Text.of(PlantsPage.SUCCESS_MESSAGE).answeredBy(actor);
    }

    public static Question<Boolean> plantAppearsInTable(String plantName) {
        return actor -> Visibility.of(PlantsPage.plantInTable(plantName)).answeredBy(actor);
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
}
