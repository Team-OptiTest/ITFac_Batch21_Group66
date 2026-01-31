Feature: Plant Management API - Delete
  
  As an admin
  I want to be able to remove plants from the inventory
  So that the catalog remains accurate

  @API @Plant @Delete
  Scenario: API_Plant_Delete_001 - Delete Plant Record
    Given the admin is authenticated
    And a plant exists
    When I delete the plant
    Then the response status should be 204
    And the plant should no longer exist
