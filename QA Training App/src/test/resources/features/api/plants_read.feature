@Plant
Feature: Read Plant API

  @API_Plant_Read_006
  Scenario: Get Paginated Plant List
    Given a regular user is authenticated
    When I GET to "/api/plants" with query params page=0&size=10
    Then the response status should be 200
    And the response should contain a list of plants and pagination metadata

  @API_Plant_Read_007
  Scenario: Search Plant by Name
    Given a regular user is authenticated
    And a plant exists
    When I search for plants with name "Plant to Delete" and page=0&size=10
    Then the response status should be 200
    And the response should contain plants filtering by name "Plant to Delete"
