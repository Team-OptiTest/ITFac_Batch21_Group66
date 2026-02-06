package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.model.util.EnvironmentVariables;
import pages.LoginPage;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Login implements Task {

    private final String username;
    private final String password;
    private final EnvironmentVariables environmentVariables;

    public Login(String username, String password, EnvironmentVariables environmentVariables) {
        this.username = username;
        this.password = password;
        this.environmentVariables = environmentVariables;
    }

    public static Login asUser(String username, String password, EnvironmentVariables environmentVariables) {
        return instrumented(Login.class, username, password, environmentVariables);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String baseUrl = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
                .from(environmentVariables)
                .getProperty("webdriver.base.url");

        actor.attemptsTo(
                Open.url(baseUrl + "/ui/login"),
                Enter.theValue(username).into(LoginPage.USERNAME_FIELD),
                Enter.theValue(password).into(LoginPage.PASSWORD_FIELD),
                Click.on(LoginPage.LOGIN_BUTTON));
    }
}
