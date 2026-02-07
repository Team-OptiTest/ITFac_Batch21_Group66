package actions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import net.serenitybdd.core.Serenity;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.*;
import java.util.List;
import utils.TestUtils;

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

    @Step("Verify error message contains")
    public void verifyErrorMessageContains(String expectedMessage) {
        SerenityRest.then()
                .body("message", containsString(expectedMessage));
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

    @Step("Get a sale with non-existent ID")
    public void getSaleWithNonExistentId() {
        // Use a non-existent ID generated from existing sale IDs
        int nonExistentId = (int) TestUtils.generateNonExistentId(getAllSaleIds());
        SerenityRest.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(getBaseUrl() + "/api/sales/" + nonExistentId);
    }

    @Step("Retrieve all sale IDs")
    public List<Integer> getAllSaleIds() {
        SerenityRest.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(getBaseUrl() + "/api/sales");

        List<Integer> ids = SerenityRest.lastResponse()
                .jsonPath()
                .getList("id", Integer.class);

        return ids;
    }

    @Step("Verify sale not found error")
    public void verifySaleNotFoundError() {
        SerenityRest.then()
                .statusCode(404)
                .body("message", containsString("Sale not found"));
    }

    @Step("Get sale by ID without authentication")
    public void getSaleByIdWithoutAuth(int saleId) {
        SerenityRest.given()
                .when()
                .get(getBaseUrl() + "/api/sales/" + saleId);
    }

    @Step("Verify unauthorized error response")
    public void verifyUnauthorizedError() {
        SerenityRest.then()
                .statusCode(401);
    }

    @Step("Get sales page with pagination: {0}?{1}")
    public void getSalesWithPagination(String endpoint, String queryParams) {
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

        String fullUrl = baseUrl + endpoint + "?" + queryParams;

        String tokenFromSession = Serenity.sessionVariableCalled("authToken");

        if (tokenFromSession != null) {
            SerenityRest.given()
                    .header("Authorization", "Bearer " + tokenFromSession)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(fullUrl);
        } else {
            // No auth header
            SerenityRest.given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(fullUrl);
        }
    }
}
