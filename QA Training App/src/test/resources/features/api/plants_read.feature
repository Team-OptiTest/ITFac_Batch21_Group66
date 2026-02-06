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

  @API @Plant_Read_003_1 @negative
  Scenario: Negative page returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=-1&size=10&sort=name"
    Then the response status should be 400

  @API @Plant_Read_003_2 @negative
  Scenario: Negative size returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=-5&sort=name"
    Then the response status should be 400

  @API @Plant_Read_003_3 @negative
  Scenario: Non-numeric values return 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=abc&size=xyz&sort=name"
    Then the response status should be 400

  @API @Plant_Read_003_4 @negative
  Scenario: Zero size returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=0&sort=name"
    Then the response status should be 400

  @API @Plant_Read_003_5 @negative
  Scenario: Large size returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=1001&sort=name"
    Then the response status should be 400