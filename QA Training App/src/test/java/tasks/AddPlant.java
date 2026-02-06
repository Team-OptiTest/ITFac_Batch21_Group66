package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import pages.PlantsPage;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class AddPlant implements Task {

    private String plantName;
    private String category;
    private String price;
    private String quantity;

    public AddPlant() {
    }

    public static AddPlant withDetails() {
        return instrumented(AddPlant.class);
    }

    public AddPlant name(String plantName) {
        this.plantName = plantName;
        return this;
    }

    public AddPlant category(String category) {
        this.category = category;
        return this;
    }

    public AddPlant price(String price) {
        this.price = price;
        return this;
    }

    public AddPlant quantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(PlantsPage.ADD_PLANT_BUTTON));

        if (plantName != null) {
            actor.attemptsTo(Enter.theValue(plantName).into(PlantsPage.PLANT_NAME_FIELD));
        }

        if (category != null) {
            actor.attemptsTo(SelectFromOptions.byVisibleText(category).from(PlantsPage.CATEGORY_DROPDOWN));
        }

        if (price != null) {
            actor.attemptsTo(Enter.theValue(price).into(PlantsPage.PRICE_FIELD));
        }

        if (quantity != null) {
            actor.attemptsTo(Enter.theValue(quantity).into(PlantsPage.QUANTITY_FIELD));
        }

        actor.attemptsTo(
                Click.on(PlantsPage.SAVE_BUTTON));
    }
}
