Feature: Category Read Operations
  As an authenticated user
  I want to view categories
  So that I can see category details

  @API @Category
  Scenario: Verify admin can fetch category list
    Given the user is authenticated as admin
    When the admin fetches the category list
    Then the category list should be retrieved successfully

  @API @Category
  Scenario: Verify user can fetch category list
    Given the user is authenticated as user
    When the user fetches the category list
    Then the category list should be retrieved successfully

  @API @Category
  Scenario: Verify user can search categories via API
    Given the user is authenticated as user
    When the user searches for categories with name "Temp"
    Then the search results should be returned successfully

  @API @Category
  Scenario: Verify admin can search categories via API
    Given the user is authenticated as admin
    When the admin searches for categories with name "Temp"
    Then the search results should be returned successfully