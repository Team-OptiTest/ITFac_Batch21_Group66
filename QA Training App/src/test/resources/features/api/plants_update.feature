Feature: Plant Update API

  As an admin
  I want to update plants in the system
  So that I can keep plant information current
  
  @simple @API @Plant @Update @Update_001
  Scenario: Update non-existent plant
    Given the admin is authenticated
    When I PUT to "/api/plants/9999" with the following plant data:
      | name     | price | quantity |
      | Test Rose | 15.99 | 50       |
    Then the response status should be 404