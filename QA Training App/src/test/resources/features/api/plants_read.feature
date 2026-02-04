Feature: Plant Read Operations

  As a user
  I want to retrieve plants from the system
  So that I can view the plant catalog


  @simple @API_Plant_Read_001 @summary @dashboard
  Scenario: Plants summary API returns inventory statistics
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/summary"
    Then the response status should be 200
    And the response should contain inventory statistics

  @simple @API_Plant_Read_002 @pagination @boundary
  Scenario: Get plants with pagination boundary values
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=2&size=10&sort=name"
    Then the response status should be 200
    And the response should contain a content array
    And the response page number should be 2
    And the response page size should be 10

  @simple @API_Plant_Read_003_1 @negative
  Scenario: Negative page returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=-1&size=10&sort=name"
    Then the response status should be 400

  @simple @API_Plant_Read_003_2 @negative
  Scenario: Negative size returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=-5&sort=name"
    Then the response status should be 400

  @simple @API_Plant_Read_003_3 @negative
  Scenario: Non-numeric values return 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=abc&size=xyz&sort=name"
    Then the response status should be 400

  @simple @API_Plant_Read_003_4 @negative
  Scenario: Zero size returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=0&sort=name"
    Then the response status should be 400

  @simple @API_Plant_Read_003_5 @negative
  Scenario: Large size returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=1001&sort=name"
    Then the response status should be 400

  @simple @API_Plant_Read_004 @pagination @default
  Scenario: Get paginated plant list with default values
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=10&sort=name"
    Then the response status should be 200
    And the response should contain a content array
    And the response page number should be 0
    And the response page size should be 10
    And the response should contain pagination metadata