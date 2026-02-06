package pages;

import org.openqa.selenium.By;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.pages.PageObject;

public class LoginPage extends PageObject {
    
    // Update these locators based on your actual HTML elements
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
        enterUsername("admin");
        enterPassword("admin123");
        clickLogin();
    }

    @Step("the user is logged in as a user")
    public void loginAsUser() {
        navigateToLoginPage();
        enterUsername("testuser");
        enterPassword("test123");
        clickLogin();
    }
}
