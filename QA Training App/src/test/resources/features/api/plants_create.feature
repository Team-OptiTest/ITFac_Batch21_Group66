Feature: Plant Management API

  As an admin
  I want to manage plants in the system
  So that I can keep the plant catalog up to date

  @API @Plant
  Scenario: API_Plant_Create_001 - Create Plant with Valid Data
    Given the admin is authenticated
    And a valid category with ID 5 exists
    When I POST to "/api/plants/category/5" with following data:
      | name       | price | quantity |
      | Rose FINAL | 15.50 | 100      |
    Then the response status should be 201
    And the response should contain a plant object with an assigned ID
    And the plant name should be "Rose FINAL"

  @API @Plant @Negative
  Scenario: API_Plant_Create_002 - Unauthorized Create Attempt
    Given a regular user is authenticated
    And a valid category with ID 5 exists
    When I POST to "/api/plants/category/5" with following data:
      | name  | price | quantity |
      | Tulip | 12.00 | 50       |
    Then the response status should be 403
