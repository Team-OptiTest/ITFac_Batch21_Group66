package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.model.util.EnvironmentVariables;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class NavigateTo implements Task {

    private final String path;
    private final EnvironmentVariables environmentVariables;

    public NavigateTo(String path, EnvironmentVariables environmentVariables) {
        this.path = path;
        this.environmentVariables = environmentVariables;
    }

    public static NavigateTo plantsPage(EnvironmentVariables environmentVariables) {
        return instrumented(NavigateTo.class, "/ui/plants", environmentVariables);
    }

    public static NavigateTo page(String path, EnvironmentVariables environmentVariables) {
        return instrumented(NavigateTo.class, path, environmentVariables);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                .from(environmentVariables)
                .getProperty("webdriver.base.url");

        actor.attemptsTo(
                Open.url(baseUrl + path));
    }
}
