package actions;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class AuthenticationActions {

    private String authToken;
    
    private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    
    private String getBaseUrl() {
        return EnvironmentSpecificConfiguration.from(environmentVariables)
            .getProperty("api.base.url");
    }

    /**
     * Clear all session variables before authenticating
     */
    private void clearSession() {
        try {
            Serenity.getCurrentSession().clear();
            authToken = null;
            System.out.println("Session cleared before authentication");
        } catch (Exception e) {
            System.out.println("Warning: Could not clear session: " + e.getMessage());
        }
    }

    @Step("Authenticate user")
    public void authenticateUser() {
        clearSession(); // Clear session before login
        
        String username = EnvironmentSpecificConfiguration.from(environmentVariables)
            .getOptionalProperty("test.user.username")
            .orElseThrow(() -> new RuntimeException("test.user.username not configured in serenity.conf"));
        String password = EnvironmentSpecificConfiguration.from(environmentVariables)
            .getOptionalProperty("test.user.password")
            .orElseThrow(() -> new RuntimeException("test.user.password not configured in serenity.conf"));
        
        authenticate(username, password);
    }

    @Step("Authenticate as admin")
    public void authenticateAsAdmin() {
        clearSession(); // Clear session before login
        
        String username = EnvironmentSpecificConfiguration.from(environmentVariables)
            .getOptionalProperty("test.admin.username")
            .orElseThrow(() -> new RuntimeException("test.admin.username not configured in serenity.conf"));
        String password = EnvironmentSpecificConfiguration.from(environmentVariables)
            .getOptionalProperty("test.admin.password")
            .orElseThrow(() -> new RuntimeException("test.admin.password not configured in serenity.conf"));
        
        authenticate(username, password);
    }

    private void authenticate(String username, String password) {
        Response response = SerenityRest.given()
            .contentType("application/json")
            .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
            .when()
            .post(getBaseUrl() + "/api/auth/login");
        
        System.out.println("=== AUTHENTICATION DEBUG ===");
        System.out.println("Username: " + username);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
        System.out.println("============================");
        
        if (response.getStatusCode() != 200) {
            throw new IllegalStateException(
                "Authentication failed with status " + response.getStatusCode() + 
                ": " + response.getBody().asString());
        }
        
        authToken = response.jsonPath().getString("token");
        
        // Store token in Serenity session for use across action classes
        Serenity.setSessionVariable("authToken").to(authToken);
        
        System.out.println("Authentication successful. Token stored in session.");
    }

    public String getAuthToken() {
        return authToken;
    }
    
    @Step("Set invalid JWT token")
    public void setInvalidJWTToken() {
        clearSession(); // Clear session before setting invalid token
        
        // Set an obviously invalid token
        String invalidToken = "invalid.jwt.token.123";
        authToken = invalidToken;
        Serenity.setSessionVariable("authToken").to(invalidToken);
        System.out.println("Set invalid JWT token for testing");
    }
    
    @Step("Logout and clear session")
    public void logout() {
        clearSession();
        System.out.println("User logged out and session cleared");
    }
}