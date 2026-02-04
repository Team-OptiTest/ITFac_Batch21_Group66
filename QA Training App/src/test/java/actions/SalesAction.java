package actions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class SalesAction {

    private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    private String token;

    private String getBaseUrl() {
        return EnvironmentSpecificConfiguration.from(environmentVariables)
            .getProperty("api.base.url");
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Step("Create a sale with specific plant ID and quantity")
    public void createSaleForPlant(int plantId, int quantity) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .when()
            .post(getBaseUrl() + "/api/sales/plant/" + plantId + "?quantity=" + quantity);
    }

    @Step("Verify response status code")
    public void verifyStatusCode(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }
}