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
        // Clear cookies and session before login
        getDriver().manage().deleteAllCookies();
        
        getDriver().get("http://localhost:8080/ui/login");
        
        // Wait for login form to load
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(usernameInput));
        } catch (Exception e) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//form | //input")));
        }
        
        // Additional wait for page to stabilize
        waitABit(1000);
    }
    
    @Step("Enter username: {0}")
    public void enterUsername(String username) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(usernameInput));
            wait.until(ExpectedConditions.elementToBeClickable(usernameInput));
            $(usernameInput).clear();
            $(usernameInput).type(username);
        } catch (Exception e) {
            By fallbackLocator = By.xpath("//input[@type='text'] | //input[@placeholder='Username']");
            wait.until(ExpectedConditions.presenceOfElementLocated(fallbackLocator));
            wait.until(ExpectedConditions.elementToBeClickable(fallbackLocator));
            getDriver().findElement(fallbackLocator).clear();
            getDriver().findElement(fallbackLocator).sendKeys(username);
        }
    }
    
    @Step("Enter password")
    public void enterPassword(String password) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(passwordInput));
            wait.until(ExpectedConditions.elementToBeClickable(passwordInput));
            $(passwordInput).clear();
            $(passwordInput).type(password);
        } catch (Exception e) {
            By fallbackLocator = By.xpath("//input[@type='password'] | //input[@placeholder='Password']");
            wait.until(ExpectedConditions.presenceOfElementLocated(fallbackLocator));
            wait.until(ExpectedConditions.elementToBeClickable(fallbackLocator));
            getDriver().findElement(fallbackLocator).clear();
            getDriver().findElement(fallbackLocator).sendKeys(password);
        }
    }
    
    @Step("Click login button")
    public void clickLogin() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(loginButton));
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        $(loginButton).click();
        
        // Wait for login to complete and redirect
        waitABit(2000);
        
        // Verify login was successful
        String currentUrl = getDriver().getCurrentUrl();
        if (currentUrl.contains("/login") || currentUrl.contains("/403")) {
            throw new RuntimeException("Login failed - still on: " + currentUrl);
        }
        
        System.out.println("Login successful - redirected to: " + currentUrl);
    }
    
    @Step("Verify user is logged in")
    public boolean isLoggedIn() {
        String currentUrl = getDriver().getCurrentUrl();
        return !currentUrl.contains("/login") && !currentUrl.contains("/403");
    }

    @Step("the user is logged in as an admin user")
    public void loginAsAdmin() {
        System.out.println("=== ADMIN LOGIN START ===");
        
        navigateToLoginPage();
        
        String username = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.admin.username");
        String password = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.admin.password");
        
        System.out.println("Admin username: " + username);
        
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        
        System.out.println("=== ADMIN LOGIN COMPLETE ===");
    }

    @Step("the user is logged in as a user")
    public void loginAsUser() {
        System.out.println("=== USER LOGIN START ===");
        
        navigateToLoginPage();
        
        String username = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.user.username");
        String password = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("test.user.password");
        
        System.out.println("User username: " + username);
        
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        
        System.out.println("=== USER LOGIN COMPLETE ===");
    }

    @Step("the user logs out")
    public void logout() {
        System.out.println("=== LOGOUT START ===");
        
        try {
            // Try to navigate to logout URL directly
            getDriver().get("http://localhost:8080/logout");
            waitABit(1000);
        } catch (Exception e) {
            // If that doesn't work, try clicking logout link
            try {
                getDriver().findElement(By.linkText("Logout")).click();
                waitABit(1000);
            } catch (Exception ex) {
                try {
                    getDriver().findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
                    waitABit(1000);
                } catch (Exception ex2) {
                    System.out.println("Warning: Could not find logout link, clearing cookies instead");
                }
            }
        }
        
        // Always clear cookies after logout
        getDriver().manage().deleteAllCookies();
        
        System.out.println("=== LOGOUT COMPLETE ===");
    }
}