Feature: Plant Management API

  As an admin
  I want to manage plants in the system
  So that I can keep the plant catalog up to date

  @API @Plant
  Scenario: API_Plant_Create_001 - Create Plant with Valid Data
    Given the admin is authenticated
    And a valid category with ID 5 exists
    When I POST to "/api/plants/category/5" with following data:
      | name  | price | quantity |
      | Sunrose | 15.50 | 100      |
    Then the response status should be 201
    And the response should contain a plant object with an assigned ID
    And the plant name should be "Sunrose"

  @API @Plant @Authorization
  Scenario: API_Plant_Create_002 - Unauthorized Create Attempt
    Given the user is authenticated with ROLE_USER
    And a valid category with ID 5 exists
    When I POST to "/api/plants/category/5" with following data:
      | name     | price | quantity |
      | TestPlant | 20.00 | 50       |
    Then the response status should be 403

  @API @Plant @Validation
  Scenario: API_Plant_Create_003 - Plant Name Length Validation
    Given the admin is authenticated
    And a valid category with ID 5 exists
    When I POST to "/api/plants/category/5" with invalid data:
      | name | price | quantity |
      | Ab   | 10.00 | 50       |
    Then the response status should be 400
    And the response error message should contain "Plant name must be between 3 and 25 characters"

  @API @Plant @API_Plant_Create_004 @negative @215098G
  Scenario: Verify admin cannot create duplicate plant
    Given the admin is authenticated
    And at least one plant exists in the system
    When the admin creates a plant with the same name and category as an existing plant
    Then the response status should be 400
    And the response error message should contain "Duplicate plant"