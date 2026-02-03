Feature: Plant Retrieval API

  As a user
  I want to retrieve plants from the system
  So that I can view the plant catalog

  @API @Plant @Pagination
  Scenario: Get Paginated Plant List
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=10"
    Then the response status should be 200
    And the response should contain a list of plants
    And the response should contain pagination metadata

  @API @Plant @Search
  Scenario: Search Plant by Name
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "name=Rose"
    Then the response status should be 200
    And the response should contain plants with name containing "Rose"

  @API @Plant @Category
  Scenario: Get Plant by Category
    Given the user is authenticated with ROLE_USER
    And a valid category with ID 5 exists
    When I GET to "/api/plants/category/5"
    Then the response status should be 200
    And the response should contain an array of plants
