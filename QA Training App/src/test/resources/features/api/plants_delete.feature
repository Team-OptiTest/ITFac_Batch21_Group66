Feature: Plant Deletion API

  As an admin
  I want to delete plants from the system
  So that I can manage the plant inventory

  @API @Plant @Delete
  Scenario: Delete Plant Record
    Given the admin is authenticated
    And a plant with ID exists in the system
    When I DELETE to "/api/plants/{id}"
    Then the response status should be 204
    And the plant should no longer exist when retrieved

  @API @Plant @Delete @Authorization
  Scenario: Unauthorized Delete Attempt
    Given the admin is authenticated
    And a plant with ID exists in the system
    And the user is authenticated with ROLE_USER
    When I DELETE to "/api/plants/{id}"
    Then the response status should be 403
