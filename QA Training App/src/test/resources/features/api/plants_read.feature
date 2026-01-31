Feature: Read Plant API

  @API_Plant_Read_006
  Scenario: Get Paginated Plant List
    Given a regular user is authenticated
    When I GET to "/api/plants" with query params page=0&size=10
    Then the response status should be 200
    And the response should contain a list of plants and pagination metadata
