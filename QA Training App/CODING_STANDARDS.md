# Coding Standards and Patterns

> **IS4600 Automation Project - Serenity BDD Test Framework**  
> Last Updated: February 4, 2026

---

## Table of Contents

1. [Technology Stack](#technology-stack)
2. [Project Architecture](#project-architecture)
3. [Framework Pattern](#framework-pattern)
4. [Package Structure](#package-structure)
5. [Naming Conventions](#naming-conventions)
6. [Feature Files (Gherkin)](#feature-files-gherkin)
7. [Step Definitions](#step-definitions)
8. [Action Classes](#action-classes)
9. [Test Runners](#test-runners)
10. [Configuration Management](#configuration-management)
11. [API Testing Patterns](#api-testing-patterns)
12. [Assertions](#assertions)
13. [Session Management](#session-management)
14. [Code Style Guidelines](#code-style-guidelines)

---

## Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| **Language** | Java | 21 |
| **Build Tool** | Maven | 3.x |
| **BDD Framework** | Serenity BDD | 4.2.1 |
| **Test Runner** | JUnit 5 | 5.10.0 |
| **Cucumber** | Cucumber-JVM | 7.14.0 |
| **API Testing** | SerenityRest (RestAssured) | 4.2.1 |
| **Assertions** | AssertJ | 3.25.3 |
| **Logging** | Logback/SLF4J | 1.4.11 / 2.0.9 |

---

## Project Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Test Runners                              │
│         (JUnit 5 @Suite - Cucumber Platform Engine)             │
├─────────────────────────────────────────────────────────────────┤
│                      Feature Files                               │
│              (Gherkin .feature files)                           │
├─────────────────────────────────────────────────────────────────┤
│                    Step Definitions                              │
│        (Glue code connecting Gherkin to Actions)                │
├─────────────────────────────────────────────────────────────────┤
│                     Action Classes                               │
│   (Reusable business logic with @Step annotations)              │
├─────────────────────────────────────────────────────────────────┤
│                      SerenityRest                                │
│           (API calls integrated with reports)                   │
├─────────────────────────────────────────────────────────────────┤
│                   Application Under Test                         │
│              (REST API on localhost:8080)                       │
└─────────────────────────────────────────────────────────────────┘
```

---

## Framework Pattern

### Pattern Used: **Classic Action Classes** (NOT Screenplay)

This project uses the **Classic Page Objects / Action Classes** pattern with `@Step` annotations. We do NOT use the Screenplay pattern (Actors, Tasks, Abilities).

```java
// ✅ CORRECT - Classic Action Class Pattern
public class CategoryActions {
    @Step("Create category with name: {0}")
    public void createCategory(String categoryName) {
        // Implementation
    }
}

// ❌ NOT USED - Screenplay Pattern
Actor.named("Admin").attemptsTo(CreateCategory.withName("Flowers"));
```

### Why This Pattern?
- Simpler learning curve
- Direct method calls are easier to debug
- Clear separation between steps and reusable actions
- Well-suited for API testing

---

## Package Structure

```
src/test/java/
├── actions/                    # Reusable action classes
│   ├── AuthenticationActions.java
│   ├── CategoryActions.java
│   ├── PlantAction.java
│   └── SalesAction.java
│
├── runners/                    # JUnit 5 test runners
│   ├── ApiTestRunner.java
│   └── CategoryCreateRunner.java
│
└── stepdefinitions/            # Cucumber step definitions
    ├── CategoryStepDefinitions.java
    └── api/
        ├── PlantApiStepDefinitions.java
        └── SalesStepDefinitions.java

src/test/resources/
├── features/                   # Gherkin feature files
│   ├── api/
│   │   ├── plants_create.feature
│   │   ├── plants_read.feature
│   │   ├── plants_update.feature
│   │   └── plants_delete.feature
│   └── category/
│       ├── category_create.feature
│       ├── category_read.feature
│       ├── category_update.feature
│       └── category_delete.feature
│
├── serenity.conf              # Environment configuration
└── serenity.conf.example      # Template for local config
```

---

## Naming Conventions

### Files & Classes

| Type | Convention | Example |
|------|------------|---------|
| **Feature Files** | `{entity}_{operation}.feature` | `plants_create.feature` |
| **Action Classes** | `{Entity}Actions.java` or `{Entity}Action.java` | `CategoryActions.java` |
| **Step Definitions** | `{Entity}StepDefinitions.java` | `CategoryStepDefinitions.java` |
| **Runners** | `{Feature}Runner.java` | `ApiTestRunner.java` |

### Methods

| Type | Convention | Example |
|------|------------|---------|
| **Step methods** | Descriptive verb phrase | `createCategory()` |
| **Verification methods** | `verify{What}()` | `verifyStatusCode()` |
| **Getter methods** | `get{Property}()` | `getAuthToken()` |
| **Helper methods** | `{action}{Details}()` | `generateNonExistentId()` |

### Variables

| Type | Convention | Example |
|------|------------|---------|
| **Instance fields** | camelCase | `lastResponse`, `authToken` |
| **Constants** | Not currently used | - |
| **Parameters** | camelCase, descriptive | `categoryName`, `expectedStatus` |

---

## Feature Files (Gherkin)

### Structure

```gherkin
Feature: {Entity} {Operation}
  As an {actor}
  I want to {action}
  So that {benefit}

  @{Tag1} @{Tag2}
  Scenario: {Test_ID} - {Description}
    Given {precondition}
    When {action}
    Then {expected result}
```

### Tagging Strategy

| Tag Type | Format | Example | Purpose |
|----------|--------|---------|---------|
| **Domain** | `@{Domain}` | `@API`, `@Category`, `@Plant` | Group by feature area |
| **Test Type** | `@{Type}` | `@validation`, `@boundary` | Categorize test type |
| **Test ID** | `@{Feature}_{Operation}_{Number}` | `@API_Category_Create_005` | Unique identifier |
| **Special** | `@{Label}` | `@simple`, `@duplicate` | Custom grouping |

### Example

```gherkin
Feature: Category Creation
  As an authenticated user with appropriate permissions
  I want to create a category
  So that I can organize plants

  @API @Category
  Scenario: Create category with valid name
    Given the user is authenticated as admin
    When the admin creates a category with valid name "Flowers"
    Then the category should be created successfully

  @API_Category_Create_005 @validation @boundary
  Scenario: Create category with less than 3 characters
    Given the user is authenticated as admin
    When the admin creates a category with less than 3 characters "Ab"
    Then the category creation should fail with validation error
    And the error message should contain "Category name must be between 3 and 10 characters"
```

---

## Step Definitions

### Pattern

Step Definitions are **thin glue layers** that:
1. Parse Cucumber parameters
2. Delegate to Action classes
3. Perform assertions when needed

```java
package stepdefinitions;

import actions.CategoryActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryStepDefinitions {

    @Steps
    CategoryActions categoryActions;  // Injected by Serenity

    @Steps
    AuthenticationActions authenticationActions;

    @Given("the user is authenticated as admin")
    public void theUserIsAuthenticatedAsAdmin() {
        authenticationActions.authenticateAsAdmin();
    }

    @When("the admin creates a category with valid name {string}")
    public void theAdminCreatesACategoryWithName(String categoryName) {
        categoryActions.createCategory(categoryName);
    }

    @Then("the API should return {int} OK")
    public void theAPIShouldReturnOK(int expectedStatusCode) {
        assertThat(categoryActions.getLastResponseStatusCode())
            .as("API should return " + expectedStatusCode + " status code")
            .isEqualTo(expectedStatusCode);
    }
}
```

### Key Rules

1. **Use `@Steps` annotation** for injecting Action classes (not `new`)
2. **Keep step methods short** - delegate complex logic to Actions
3. **Use AssertJ** for assertions in step definitions
4. **Match method names** to step text for readability

---

## Action Classes

### Pattern

Action classes contain **reusable business logic** with `@Step` annotations for reporting.

```java
package actions;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;
import net.thucydides.model.environment.SystemEnvironmentVariables;

public class CategoryActions {

    private Response lastResponse;
    private Integer lastCreatedCategoryId;
    
    private final EnvironmentVariables environmentVariables = 
        SystemEnvironmentVariables.createEnvironmentVariables();
    
    // Helper method for configuration access
    private String getBaseUrl() {
        return EnvironmentSpecificConfiguration.from(environmentVariables)
            .getProperty("api.base.url");
    }

    // Helper method for session-based token retrieval
    private String getAuthToken() {
        return Serenity.sessionVariableCalled("authToken");
    }

    @Step("Create category with name: {0}")
    public void createCategory(String categoryName) {
        String requestBody = String.format("{\"name\":\"%s\"}", categoryName);
        
        lastResponse = SerenityRest.given()
            .header("Authorization", "Bearer " + getAuthToken())
            .header("Content-Type", "application/json")
            .body(requestBody)
            .when()
            .post(getBaseUrl() + "/api/categories");
        
        // Store created ID for later use
        if (lastResponse.getStatusCode() == 201 || lastResponse.getStatusCode() == 200) {
            lastCreatedCategoryId = lastResponse.jsonPath().getInt("id");
            Serenity.setSessionVariable("lastCreatedCategoryId").to(lastCreatedCategoryId);
        }
    }

    @Step("Get response status code")
    public int getLastResponseStatusCode() {
        return lastResponse.getStatusCode();
    }
}
```

### Key Rules

1. **Every public method should have `@Step` annotation** for report visibility
2. **Use parameter placeholders** in `@Step` text: `@Step("Create category with name: {0}")`
3. **Store state in instance fields** (e.g., `lastResponse`, `createdId`)
4. **Use Serenity session** for cross-action data sharing
5. **Read config from `serenity.conf`** via `EnvironmentSpecificConfiguration`

---

## Test Runners

### JUnit 5 Platform Suite Pattern

```java
package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("/features/category")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "stepdefinitions")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, 
    value = "io.cucumber.core.plugin.SerenityReporterParallel,pretty")
public class CategoryCreateRunner {
}
```

### Runner Configuration Options

| Annotation | Purpose | Example |
|------------|---------|---------|
| `@Suite` | Marks as JUnit 5 test suite | Required |
| `@IncludeEngines("cucumber")` | Use Cucumber engine | Required |
| `@SelectClasspathResource` | Feature file location | `"/features/api"` |
| `@ConfigurationParameter` | Cucumber options | Glue, plugins |

---

## Configuration Management

### serenity.conf Structure

```hocon
serenity {
    project.name = "IS4600 Automation Project"
    test.root = "stepdefinitions"
    take.screenshots = FOR_FAILURES
    timeout = 30000
}

webdriver {
    driver = chrome
    timeouts.implicitlywait = 10000
}

headless.mode = true

environments {
    default {
        api.base.url = "http://localhost:8080"
        api.endpoints.auth.login = "/api/auth/login"
        api.endpoints.plants.category = "/api/plants/category/"
        
        test.user.username = "testuser"
        test.user.password = "test123"
        test.admin.username = "admin"
        test.admin.password = "admin123"
    }
}
```

### Accessing Configuration

```java
// Initialize environment variables
private final EnvironmentVariables environmentVariables = 
    SystemEnvironmentVariables.createEnvironmentVariables();

// Get property value
String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
    .getProperty("api.base.url");

// Get optional property with fallback
String username = EnvironmentSpecificConfiguration.from(environmentVariables)
    .getOptionalProperty("test.user.username")
    .orElseThrow(() -> new RuntimeException("Property not configured"));
```

---

## API Testing Patterns

### Use SerenityRest (Not Plain RestAssured)

```java
// ✅ CORRECT - SerenityRest (integrated with reports)
import net.serenitybdd.rest.SerenityRest;

Response response = SerenityRest.given()
    .header("Authorization", "Bearer " + token)
    .contentType(ContentType.JSON)
    .body(requestBody)
    .when()
    .post(url);

// ❌ AVOID - Plain RestAssured (not in reports)
import io.restassured.RestAssured;
RestAssured.given()...  // Won't appear in Serenity reports
```

### Response Validation Patterns

```java
// Pattern 1: Lambda-based validation (for simple checks)
@Step("Verify response status code is {0}")
public void verifyStatusCode(int expectedStatus) {
    SerenityRest.restAssuredThat(response -> 
        response.statusCode(expectedStatus));
}

// Pattern 2: Hamcrest matchers for body validation
@Step("Verify response contains assigned ID")
public void verifyAssignedId() {
    SerenityRest.restAssuredThat(response -> 
        response.body("id", org.hamcrest.Matchers.notNullValue()));
}

// Pattern 3: Extract and validate with lastResponse()
@Step("Verify error message contains {0}")
public void verifyErrorMessage(String expectedMessage) {
    String message = SerenityRest.lastResponse()
        .jsonPath()
        .getString("message");
    
    if (!message.contains(expectedMessage)) {
        throw new AssertionError("Expected: " + expectedMessage);
    }
}
```

### Authentication Pattern

```java
@Step("Authenticate as admin")
public void authenticateAsAdmin() {
    Response response = SerenityRest.given()
        .contentType("application/json")
        .body("{\"username\":\"admin\",\"password\":\"admin123\"}")
        .when()
        .post(getBaseUrl() + "/api/auth/login");
    
    String token = response.jsonPath().getString("token");
    
    // Store in Serenity session for cross-action access
    Serenity.setSessionVariable("authToken").to(token);
}
```

---

## Assertions

### Primary: AssertJ (in Step Definitions)

```java
import static org.assertj.core.api.Assertions.assertThat;

@Then("the API should return {int} OK")
public void theAPIShouldReturnOK(int expectedStatusCode) {
    assertThat(categoryActions.getLastResponseStatusCode())
        .as("API should return " + expectedStatusCode + " status code")
        .isEqualTo(expectedStatusCode);
}

@Then("the category should be created successfully")
public void theCategoryShouldBeCreatedSuccessfully() {
    assertThat(categoryActions.getLastResponseStatusCode())
        .as("Category creation should succeed")
        .isIn(200, 201);
}
```

### Secondary: Hamcrest (in Action Classes via SerenityRest)

```java
import org.hamcrest.Matchers;

@Step("Verify plant name is {0}")
public void verifyPlantName(String expectedName) {
    SerenityRest.restAssuredThat(response -> 
        response.body("name", Matchers.containsString(expectedName)));
}
```

---

## Session Management

### Storing Data Across Actions

```java
// Store value
Serenity.setSessionVariable("authToken").to(token);
Serenity.setSessionVariable("lastCreatedCategoryId").to(categoryId);

// Retrieve value
String token = Serenity.sessionVariableCalled("authToken");
Integer categoryId = Serenity.sessionVariableCalled("lastCreatedCategoryId");
```

### Common Session Variables

| Variable Name | Type | Set By | Used By |
|---------------|------|--------|---------|
| `authToken` | String | AuthenticationActions | All action classes |
| `lastCreatedCategoryId` | Integer | CategoryActions | Delete/Update tests |

---

## Code Style Guidelines

### Indentation
- **4 spaces** (not tabs) - Note: Some files currently use 8 spaces

### Imports
```java
// Order: java.*, third-party, project packages
import java.util.Map;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;

import actions.AuthenticationActions;
```

### Debug Logging Pattern
```java
System.out.println("=== CREATE CATEGORY RESULT ===");
System.out.println("Input: " + categoryName);
System.out.println("Status: " + response.getStatusCode());
System.out.println("Response: " + response.getBody().asString());
System.out.println("==============================");
```

### Error Handling
```java
// Extract values safely after successful response
if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
    try {
        lastCreatedCategoryId = response.jsonPath().getInt("id");
    } catch (Exception e) {
        System.out.println("Could not extract ID: " + e.getMessage());
        lastCreatedCategoryId = null;
    }
}
```

### Null Safety
```java
// Check for null before operations
if (createdPlantId == null) {
    throw new IllegalStateException(
        "createdPlantId is null — plant creation failed; cannot proceed");
}
```

---

## Running Tests

### Maven Commands

```bash
# Run all tests
mvn clean verify

# Run specific runner
mvn clean verify -Dtest=ApiTestRunner

# Run with specific environment
mvn clean verify -Denvironment=dev

# Run tests with specific tags
mvn clean verify -Dcucumber.filter.tags="@API"
```

### Generated Reports

After test execution, Serenity reports are generated at:
```
target/site/serenity/index.html
```

---

## Quick Reference

### Creating a New Feature

1. Create feature file: `src/test/resources/features/{domain}/{entity}_{operation}.feature`
2. Create/update step definitions: `src/test/java/stepdefinitions/{Entity}StepDefinitions.java`
3. Create/update action class: `src/test/java/actions/{Entity}Actions.java`
4. Create runner (if needed): `src/test/java/runners/{Feature}Runner.java`

### Checklist for New Actions

- [ ] Add `@Step` annotation with parameter placeholders
- [ ] Use `SerenityRest` (not plain RestAssured)
- [ ] Read config from `serenity.conf`
- [ ] Store auth token from Serenity session
- [ ] Handle response status codes appropriately
- [ ] Store created entity IDs in session for cleanup

---

*This document reflects the current state of the codebase as of February 2026.*
