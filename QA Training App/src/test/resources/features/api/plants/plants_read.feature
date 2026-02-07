Feature: Plant Retrieval API
  As a user
  I want to retrieve plants from the system
  So that I can view the plant catalog

  @API @Pagination @215063V
  Scenario: Get Paginated Plant List
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=10"
    Then the response status should be 200
    And the response should contain a list of plants
    And the response should contain pagination metadata

  @API @Plant @Search @215063V
  Scenario: Search Plant by Name
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "name=Rose"
    Then the response status should be 200
    And the response should contain plants with name containing "Rose"

  @API @Plant @Category @215063V
  Scenario: Get Plant by Category
    Given the user is authenticated with ROLE_USER
    And a valid category with ID 5 exists
    When I GET to "/api/plants/category/5"
    Then the response status should be 200
    And the response should contain an array of plants

  @API @Plant @API_Plant_Read_009 @negative @215098G
  Scenario: Verify user receives 404 error when viewing non-existent plant
    Given the user is authenticated as user
    When the user attempts to view a plant with a non-existent ID
    Then the API should return 404 Not Found
    And the error message should contain "Plant not found"

  @API @Plant @API_Plant_Read_010 @negative @215098G
  Scenario: Verify user receives 404 error when getting plants by non-existent category
    Given the user is authenticated as user
    When the user attempts to get plants by a non-existent category ID
    Then the API should return 404 Not Found
    And the error message should contain "Category not found"

  @API @Plant_Read_003_1 @negative @215027P
  Scenario: Negative page returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=-1&size=10&sort=name"
    Then the response status should be 400

  @API @Plant_Read_003_2 @negative @215027P
  Scenario: Negative size returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=-5&sort=name"
    Then the response status should be 400

  @API @Plant_Read_003_3 @negative @215027P
  Scenario: Non-numeric values return 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=abc&size=xyz&sort=name"
    Then the response status should be 400

  @API @Plant_Read_003_4 @negative @215027P
  Scenario: Zero size returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=0&sort=name"
    Then the response status should be 400

  @API @Plant_Read_003_5 @negative @215027P
  Scenario: Large size returns 400
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=1001&sort=name"
    Then the response status should be 400

  @API @Plant_Read_004 @pagination @default @215027P
  Scenario: Get paginated plant list with default values
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/paged" with query params "page=0&size=10&sort=name"
    Then the response status should be 200
    And the response should contain a content array
    And the response page number should be 0
    And the response page size should be 10
    And the response should contain pagination metadata

  @API @Plant_Read_005 @security @authentication @215027P
  Scenario: API rejects invalid JWT token for plant retrieval
    Given the user has an invalid JWT token
    When I GET to "/api/plants/paged" with query params "page=0&size=10&sort=name"
    Then the response status should be 401 Unauthorized

  @API @Plant_Read_001 @215027P
  Scenario: Plants summary API returns inventory statistics
    Given the user is authenticated with ROLE_USER
    When I GET to "/api/plants/summary"
    Then the response status should be 200
    And the response should be in read-only format