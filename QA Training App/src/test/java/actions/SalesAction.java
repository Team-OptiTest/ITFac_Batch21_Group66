package actions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import static org.hamcrest.Matchers.*;

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

    @Step("Create a sale for a plant")
    public void createSale(int plantId, int quantity) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .when()
            .post(getBaseUrl() + "/api/sales/plant/" + plantId + "?quantity=" + quantity);
    }

    @Step("Verify creation success")
    public void verifySaleCreatedSuccessfully(int expectedQuantity) {
        SerenityRest.then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("plant", notNullValue())
            .body("quantity", equalTo(expectedQuantity))
            .body("totalPrice", notNullValue())
            .body("soldAt", notNullValue());
    }
    
    @Step("Verify response status code")
    public void verifyStatusCode(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    @Step("Verify error message")
    public void verifyErrorMessage(String expectedMessage) {
        SerenityRest.then()
            .body("message", equalTo(expectedMessage));
    }

    @Step("Retrieve all sales")
    public void getAllSales() {
        SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get(getBaseUrl() + "/api/sales");
    }

    @Step("Verify sales list is returned")
    public void verifySalesListReturned() {
        SerenityRest.then()
            .statusCode(200)
            .body("$", instanceOf(java.util.List.class))
            .body("[0].id", notNullValue())
            .body("[0].plant", notNullValue())
            .body("[0].plant.id", notNullValue())
            .body("[0].quantity", notNullValue())
            .body("[0].totalPrice", notNullValue())
            .body("[0].soldAt", notNullValue());
    }

    @Step("Delete a sale")
    public void deleteSale(int saleId) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .when()
            .delete(getBaseUrl() + "/api/sales/" + saleId);
    }

    @Step("Get a sale by ID")
    public void getSaleById(int saleId) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get(getBaseUrl() + "/api/sales/" + saleId);
    }

    public int getLastCreatedSaleId() {
        return SerenityRest.lastResponse().jsonPath().getInt("id");
    }

    @Step("Verify sale object is returned")
    public void verifySaleReturned(int expectedSaleId) {
        SerenityRest.then()
            .statusCode(200)
            .body("id", equalTo(expectedSaleId))
            .body("plant", notNullValue())
            .body("plant.id", notNullValue())
            .body("quantity", notNullValue())
            .body("totalPrice", notNullValue())
            .body("soldAt", notNullValue());
    }
    @Step("Verify error response schema")
    public void verifyErrorSchema() {
        SerenityRest.then()
            .body("status", notNullValue())
            .body("error", notNullValue())
            .body("message", notNullValue());
    }
    @Step("Get a sale by ID without authentication")
    public void getSaleByIdUnauthenticated(int saleId) {
        SerenityRest.given()
            .when()
            .get(getBaseUrl() + "/api/sales/" + saleId);
    }
}
