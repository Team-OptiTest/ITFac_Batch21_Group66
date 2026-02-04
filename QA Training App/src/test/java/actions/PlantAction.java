package actions;

import java.util.HashMap;
import java.util.Map;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;

public class PlantAction {

    private String baseUrl = "http://localhost:8080";
    private String username;
    private String password;
    private String token;
    private int lastCreatedPlantId;
    private String lastCreatedPlantName;
    private Response lastResponse;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setLastCreatedPlantName(String name) {
        this.lastCreatedPlantName = name;
    }

    public String getLastCreatedPlantName() {
        return this.lastCreatedPlantName;
    }

    @Step("Authenticate with credentials")
    public void authenticate(String username, String password) {
        this.username = username;
        this.password = password;

        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", username);
        loginData.put("password", password);

        Response response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post(baseUrl + "/api/auth/login");

        if (response.getStatusCode() == 200) {
            this.token = response.jsonPath().getString("token");
        } else {
            this.token = null;
        }
    }

    @Step("Authenticate as user")
    public void authenticateAsUser(String username, String password) {
        authenticate(username, password);
    }

    @Step("Create a new plant")
    public void createPlant(int categoryId, Map<String, Object> plantData) {
        var request = SerenityRest.given()
                .contentType(ContentType.JSON);

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            request.auth().preemptive().basic(username, password);
        }

        Response response = request.body(plantData)
                .when()
                .post(baseUrl + "/api/plants/category/" + categoryId);

        lastResponse = response;
        
        if (response.getStatusCode() == 201) {
            lastCreatedPlantId = response.jsonPath().getInt("id");
        }
    }

    @Step("Delete a plant")
    public void deletePlant(int id) {
        var request = SerenityRest.given();

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            request.auth().preemptive().basic(username, password);
        }

        lastResponse = request.when()
                .delete(baseUrl + "/api/plants/" + id);
    }

    @Step("Get a plant")
    public void getPlant(int id) {
        var request = SerenityRest.given();

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else {
            request.auth().preemptive().basic(username, password);
        }

        lastResponse = request.when()
                .get(baseUrl + "/api/plants/" + id);
    }

    // @Step("GET request to {string}")
    // public void getRequest(String endpoint) {
    //     var request = SerenityRest.given();

    //     if (token != null) {
    //         request.header("Authorization", "Bearer " + token);
    //     } else {
    //         request.auth().preemptive().basic(username, password);
    //     }

    //     lastResponse = request.when()
    //             .get(baseUrl + endpoint);
    // }

    // @Step("GET request to {string} with query params {string}")
    // public void getPlantsWithPagination(String endpoint, String queryParams) {
    //     var request = SerenityRest.given();

    //     if (token != null) {
    //         request.header("Authorization", "Bearer " + token);
    //     } else {
    //         request.auth().preemptive().basic(username, password);
    //     }

    //     if (queryParams != null && !queryParams.isEmpty()) {
    //         String[] pairs = queryParams.split("&");
    //         for (String pair : pairs) {
    //             String[] keyValue = pair.split("=");
    //             if (keyValue.length == 2) {
    //                 request.queryParam(keyValue[0], keyValue[1]);
    //             }
    //         }
    //     }

    //     lastResponse = request.when()
    //             .get(baseUrl + endpoint);
    // }
@Step("GET request to {string} with query params {string}")
    public void getPlantsWithPagination(String endpoint, String queryParams) {
        var request = SerenityRest.given();

        String sessionToken = Serenity.sessionVariableCalled("authToken");
        
        if (sessionToken != null) {
            request.header("Authorization", "Bearer " + sessionToken);
        } else if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else if (username != null && password != null) {
            request.auth().preemptive().basic(username, password);
        }

        if (queryParams != null && !queryParams.isEmpty()) {
            String[] pairs = queryParams.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    request.queryParam(keyValue[0], keyValue[1]);
                }
            }
        }

        lastResponse = request.when()
                .get(baseUrl + endpoint);
    }

    @Step("GET request to {string}")
    public void getRequest(String endpoint) {
        var request = SerenityRest.given();

        String sessionToken = Serenity.sessionVariableCalled("authToken");
        
        if (sessionToken != null) {
            request.header("Authorization", "Bearer " + sessionToken);
        } else if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else if (username != null && password != null) {
            request.auth().preemptive().basic(username, password);
        }

        lastResponse = request.when()
                .get(baseUrl + endpoint);
    }
    public int getLastCreatedPlantId() {
        return lastCreatedPlantId;
    }

    @Step("Verify response status code")
    public void verifyStatusCode(int statusCode) {
        if (lastResponse != null) {
            SerenityRest.then().statusCode(statusCode);
        }
    }

    @Step("Verify assigned ID")
    public void verifyAssignedId() {
        SerenityRest.then().body("id", org.hamcrest.Matchers.notNullValue());
    }

    @Step("Verify plant name")
    public void verifyPlantName(String expectedName) {
        SerenityRest.then().body("name", org.hamcrest.Matchers.equalTo(expectedName));
    }

    @Step("Verify inventory statistics")
    public void verifyInventoryStatistics() {
        SerenityRest.then()
                .body("totalPlants", org.hamcrest.Matchers.notNullValue())
                .body("lowStockPlants", org.hamcrest.Matchers.notNullValue());
    }

    @Step("Verify plant list exists")
    public void verifyPlantListExists() {
        SerenityRest.then()
                .body("content", org.hamcrest.Matchers.notNullValue());
    }

    @Step("Verify response has content array")
public void verifyResponseHasContentArray() {
    SerenityRest.then()
            .body("content", org.hamcrest.Matchers.notNullValue());
}

@Step("Verify pagination info for page {int} size {int}")
public void verifyPaginationInfoForPageSize(int expectedPage, int expectedSize) {
    SerenityRest.then()
            .body("number", org.hamcrest.Matchers.equalTo(expectedPage))
            .body("size", org.hamcrest.Matchers.equalTo(expectedSize));
}

@Step("Verify response is not empty")
public void verifyResponseIsNotEmpty() {
    SerenityRest.then()
            .body("empty", org.hamcrest.Matchers.equalTo(false));
}
@Step("Verify page number is {0}")
public void verifyPageNumber(int expectedPage) {
    SerenityRest.then().body("number", org.hamcrest.Matchers.equalTo(expectedPage));
}

@Step("Verify page size is {0}")
public void verifyPageSize(int expectedSize) {
    SerenityRest.then().body("size", org.hamcrest.Matchers.equalTo(expectedSize));
}
@Step("Verify pagination metadata")
public void verifyPaginationMetadata() {
    SerenityRest.then()
            .body("pageable", org.hamcrest.Matchers.notNullValue())
            .body("totalElements", org.hamcrest.Matchers.notNullValue())
            .body("totalPages", org.hamcrest.Matchers.notNullValue())
            .body("size", org.hamcrest.Matchers.notNullValue())
            .body("number", org.hamcrest.Matchers.notNullValue());
}
@Step("Update plant at {0} with data {1}")
    public void updatePlant(String endpoint, Map<String, Object> plantData) {
        var request = SerenityRest.given()
                .contentType(ContentType.JSON);

        String sessionToken = Serenity.sessionVariableCalled("authToken");
        
        if (sessionToken != null) {
            request.header("Authorization", "Bearer " + sessionToken);
        } else if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else if (username != null && password != null) {
            request.auth().preemptive().basic(username, password);
        }

        request.body(plantData);
        lastResponse = request.when()
                .put(baseUrl + endpoint);
    }
    @Step("Update plant price at {0} with data {1}")
    public void updatePlantPrice(String endpoint, Map<String, Object> updateData) {
        var request = SerenityRest.given();

        String sessionToken = Serenity.sessionVariableCalled("authToken");
        
        if (sessionToken != null) {
            request.header("Authorization", "Bearer " + sessionToken);
        } else if (token != null) {
            request.header("Authorization", "Bearer " + token);
        } else if (username != null && password != null) {
            request.auth().preemptive().basic(username, password);
        }

        request.contentType(ContentType.JSON)
               .body(updateData);
               
        lastResponse = request.when()
                .put(baseUrl + endpoint);
    }
    @Step("Verify error message contains {0}")
    public void verifyErrorMessage(String expectedMessage) {
        if (lastResponse != null) {
            String messageField = lastResponse.jsonPath().getString("message");
            String errorField = lastResponse.jsonPath().getString("error");

            boolean messageContains = messageField != null && messageField.toLowerCase().contains(expectedMessage.toLowerCase());
            boolean errorContains = errorField != null && errorField.toLowerCase().contains(expectedMessage.toLowerCase());

            if (!messageContains && !errorContains) {
                throw new AssertionError("Expected error message containing '" + expectedMessage +
                        "' but got message: '" + messageField + "' and error: '" + errorField + "'");
            }
        }
    }

    @Step("Clear authentication")
    public void clearAuthentication() {
        this.token = null;
        this.username = null;
        this.password = null;
    }
}