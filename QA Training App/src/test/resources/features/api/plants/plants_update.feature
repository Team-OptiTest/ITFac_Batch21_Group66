Feature: Plant Update API

  As an admin
  I want to update plants in the system
  So that I can keep plant information current

  @API @Plant @Update
  Scenario: Update Plant Price
    Given the admin is authenticated
    And a plant with ID exists in the system
    When I PUT to "/api/plants/{id}" with new price "35.99"
    Then the response status should be 200
    And the response should show updated price "35.99"

  @API @Plant @Update @Validation
  Scenario: Negative Quantity Validation
    Given the admin is authenticated
    And a plant with ID exists in the system
    When I PUT to "/api/plants/{id}" with new quantity "-5"
    Then the response status should be 400
    And the response error message should contain "Quantity cannot be negative"
