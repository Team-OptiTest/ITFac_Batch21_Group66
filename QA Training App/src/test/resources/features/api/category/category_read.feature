Feature: Category Read Operations

  As an authenticated user
  I want to view categories
  So that I can see category details


  @API @Category_Read_004 @dashboard @summary @215027P
  Scenario: Categories summary API returns aggregated data
    Given the user has a valid JWT token
    When the user requests categories summary
    Then the API should return 200 OK

  @API @Category_Read_005 @security @authentication @215027P
  Scenario: Categories summary API rejects invalid JWT token
    Given the user has an expired JWT token:
    """
    eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTc3ODM2MDAwLCJleHAiOjE1Nzc4MzYwMDB9.expired_signature_123
    """
    When the user requests categories summary
    Then the API should be returned 401 Unauthorized


  @API @Category @215013U
  Scenario: Verify admin can fetch category list
    Given the user is authenticated as admin
    When the admin fetches the category list
    Then the category list should be retrieved successfully

  @API @Category @215013U
  Scenario: Verify user can fetch category list
    Given the user is authenticated as user
    When the user fetches the category list
    Then the category list should be retrieved successfully

  @API @Category @215013U
  Scenario: Verify user can search categories via API
    Given the user is authenticated as user
    When the user searches for categories with name "Temp"
    Then the search results should be returned successfully

  @API @Category @215013U
  Scenario: Verify admin can search categories via API
    Given the user is authenticated as admin
    When the admin searches for categories with name "Temp"
    Then the search results should be returned successfully

  @API @Category @215013U
  Scenario: Verify user can filter categories by parent category
    Given the user is authenticated as user
    When the user filters categories by parent ID "1"
    Then the filtered categories should be retrieved successfully

  @API @Category @215013U
  Scenario: Verify admin can filter categories by parent category
    Given the user is authenticated as admin
    When the admin filters categories by parent ID "1"
    Then the filtered categories should be retrieved successfully

  @API @Category @API_Category_Read_007 @negative @215098G
  Scenario: Verify user receives 404 error when viewing non-existent category
    Given the user is authenticated as user
    When the user attempts to view a category with a non-existent ID
    Then the API should return 404 Not Found
    And the error message should be contained "Category not found"
  
  @API @Category @API_Category_Read_008 @215098G
  Scenario: Verify user can view main categories
    Given the user is authenticated as user
    When the user requests the main categories
    Then the API should return 200 OK
    And the response should contain a list of main categories

  @API @Category_Read_006 @security @authentication @215027P
  Scenario: API rejects requests without JWT token
    When a request is made to get categories without JWT token
    Then the API should be returned 401 Unauthorized