package actions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import static org.hamcrest.Matchers.*;

public class SalesAction {

    private String baseUrl = "http://localhost:8080";
    private String token;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Step("Create a sale for a plant")
    public void createSale(int plantId, int quantity) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + token)
            .when()
            .post(baseUrl + "/api/sales/plant/" + plantId + "?quantity=" + quantity);
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
}
