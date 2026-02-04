Feature: Category Creation
  As an authenticated user with appropriate permissions
  I want to create a category
  So that I can organize plants

  @API @Category
  Scenario: Create category with valid name
    Given the user is authenticated as admin
    When the admin creates a category with valid name "Flowers"
    Then the category should be created successfully

  @API_Category_Create_005 @validation @boundary
  Scenario: Create category with less than 3 characters
    Given the user is authenticated as admin
    When the admin creates a category with less than 3 characters "Ab"
    Then the category creation should fail with validation error
    And the error message should contain "Category name must be between 3 and 10 characters"

  @API @Category
  Scenario: Create category with more than 10 characters
    Given the user is authenticated as admin
    When the admin creates a category with more than 10 characters "ThisNameIsTooLongForCategory"
    Then the category creation should fail with validation error
    And the error message should contain "Category name must be between 3 and 10 characters"

  @API @Category
  Scenario: Create category without name
    Given the user is authenticated as admin
    When the admin creates a category without name ""
    Then the category creation should fail with validation error

  @API @Category
  Scenario: Create category as user
    Given the user is authenticated as user
    When the user creates a category with name "abcd"
    Then the user should be denied permission to create a category

  @simple @API_Category_Create_006 @validation @duplicate
  Scenario: Create duplicate category name
    Given the user is authenticated as admin
    And a category named "Roses" exists
    When the admin attempts to create another category with name "Roses"
    Then the API should return 400 Bad Request
    And the error message should contain "already exists"