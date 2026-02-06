package pages;

import org.openqa.selenium.By;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class LoginPage extends PageObject {

    private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By loginButton = By.xpath("//button[@type='submit']");
    
    @Step("Navigate to login page")
    public void navigateToLoginPage() {
        getDriver().get("http://localhost:8080/ui/login");
    }
    
    @Step("Enter username: {0}")
    public void enterUsername(String username) {
        $(usernameInput).type(username);
    }
    
    @Step("Enter password")
    public void enterPassword(String password) {
        $(passwordInput).type(password);
    }
    
    @Step("Click login button")
    public void clickLogin() {
        $(loginButton).click();
    }
    
    @Step("Verify user is logged in")
    public boolean isLoggedIn() {
        // Update this condition based on what appears after successful login
        return getDriver().getCurrentUrl().contains("/dashboard");
    }

    @Step("the user is logged in as an admin user")
    public void loginAsAdmin() {
        navigateToLoginPage();
        enterUsername(net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.admin.username"));
        enterPassword(net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.admin.password"));
        clickLogin();
    }

    @Step("the user is logged in as a user")
    public void loginAsUser() {
        navigateToLoginPage();
        enterUsername(net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.user.username"));
        enterPassword(net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.user.password"));
        clickLogin();
    }
}

