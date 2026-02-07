package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

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
        // Wait for either username field or any form element to be present
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(usernameInput));
        } catch (Exception e) {
            // If the username field isn't found, try waiting for a form to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//form | //input")));
        }
    }
    
    @Step("Enter username: {0}")
    public void enterUsername(String username) {
        // Wait for the username field to be present and clickable
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(usernameInput));
            wait.until(ExpectedConditions.elementToBeClickable(usernameInput));
            $(usernameInput).type(username);
        } catch (Exception e) {
            // Fallback: try to find the username input using common locators
            By fallbackLocator = By.xpath("//input[@type='text'] | //input[@placeholder='Username']");
            wait.until(ExpectedConditions.presenceOfElementLocated(fallbackLocator));
            wait.until(ExpectedConditions.elementToBeClickable(fallbackLocator));
            getDriver().findElement(fallbackLocator).sendKeys(username);
        }
    }
    
    @Step("Enter password")
    public void enterPassword(String password) {
        // Wait for the password field to be present and clickable
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(passwordInput));
            wait.until(ExpectedConditions.elementToBeClickable(passwordInput));
            $(passwordInput).type(password);
        } catch (Exception e) {
            // Fallback: try to find the password input using common locators
            By fallbackLocator = By.xpath("//input[@type='password'] | //input[@placeholder='Password']");
            wait.until(ExpectedConditions.presenceOfElementLocated(fallbackLocator));
            wait.until(ExpectedConditions.elementToBeClickable(fallbackLocator));
            getDriver().findElement(fallbackLocator).sendKeys(password);
        }
    }
    
    @Step("Click login button")
    public void clickLogin() {
        // Wait for the login button to be present and clickable
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(loginButton));
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
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

