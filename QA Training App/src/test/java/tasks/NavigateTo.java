package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.model.util.EnvironmentVariables;

import java.net.URI;
import java.util.Optional;

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
        String baseUrl = Optional.ofNullable(
                net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                        .from(environmentVariables)
                        .getProperty("webdriver.base.url"))
                .orElseThrow(() -> new IllegalStateException("webdriver.base.url is not set"));
        String targetUrl = URI.create(baseUrl)
                .resolve(path).toString();
        actor.attemptsTo(Open.url(targetUrl));
    }
}
