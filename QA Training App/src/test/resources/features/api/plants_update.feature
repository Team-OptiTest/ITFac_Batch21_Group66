Feature: Plant Management API - Update

  As an admin
  I want to update plant details
  So that I can adjust prices and inventory

  @API @Plant @Update @API_Plant_Update_002
  Scenario: API_Plant_Update_002 - Update Plant Price
    Given the admin is authenticated
    And a plant exists
    When I update the plant price to 25.50
    Then the response status should be 200
    And the plant price should be 25.50
