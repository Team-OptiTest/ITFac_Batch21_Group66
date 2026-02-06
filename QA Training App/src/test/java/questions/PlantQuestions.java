package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.questions.Visibility;
import net.serenitybdd.screenplay.waits.WaitUntil;
import pages.PlantsPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class PlantQuestions {

    public static Question<Boolean> successMessageIsDisplayed() {
        return actor -> {
            try {
                // Wait up to 10 seconds for the success message to appear
                actor.attemptsTo(
                        WaitUntil.the(PlantsPage.SUCCESS_MESSAGE, isVisible())
                                .forNoMoreThan(10).seconds());
                return Visibility.of(PlantsPage.SUCCESS_MESSAGE).answeredBy(actor);
            } catch (Exception e) {
                return false;
            }
        };
    }

    public static Question<String> successMessageText() {
        return actor -> {
            try {
                // Wait for the success message to appear
                actor.attemptsTo(
                        WaitUntil.the(PlantsPage.SUCCESS_MESSAGE, isVisible())
                                .forNoMoreThan(10).seconds());
                return Text.of(PlantsPage.SUCCESS_MESSAGE).answeredBy(actor);
            } catch (Exception e) {
                return "";
            }
        };
    }

    public static Question<Boolean> plantAppearsInTable(String plantName) {
        return actor -> {
            try {
                // Wait up to 10 seconds for the plant to appear in the table
                actor.attemptsTo(
                        WaitUntil.the(PlantsPage.plantInTable(plantName), isVisible())
                                .forNoMoreThan(10).seconds());
                return Visibility.of(PlantsPage.plantInTable(plantName)).answeredBy(actor);
            } catch (Exception e) {
                return false;
            }
        };
    }

    public static Question<Boolean> plantIsRemovedFromTable(String plantName) {
        return actor -> {
            try {
                // Wait up to 10 seconds for the plant to appear in the table
                actor.attemptsTo(
                        WaitUntil.the(PlantsPage.plantInTable(plantName), isVisible())
                                .forNoMoreThan(10).seconds());
                return Visibility.of(PlantsPage.plantInTable(plantName)).answeredBy(actor);
            } catch (Exception e) {
                return false;
            }
        };
    }

    public static Question<Boolean> plantDoesNotAppearInSearch(String plantName) {
        return actor -> {
            try {
                // Wait up to 10 seconds for the plant to appear in the table
                actor.attemptsTo(
                        WaitUntil.the(PlantsPage.plantInTable(plantName), isVisible())
                                .forNoMoreThan(10).seconds());
                return Visibility.of(PlantsPage.plantInTable(plantName)).answeredBy(actor);
            } catch (Exception e) {
                return false;
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
}
