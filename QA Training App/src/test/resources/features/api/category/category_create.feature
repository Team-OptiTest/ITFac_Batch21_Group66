Feature: Category Creation
  As an authenticated user with appropriate permissions
  I want to create a category
  So that I can organize plants

  @API @Category @215013U
  Scenario: Create category with valid name
    Given the user is authenticated as admin
    When the admin creates a category with valid name "Flowers"
    Then the category should be created successfully

  @API @Category_Create_004 @validation @boundary @215027P
  Scenario: Create category with less than 3 characters
    Given the user is authenticated as admin
    When the admin creates a category with less than 3 characters "Ab"
    Then the category creation should fail with validation error
    And the error message should be contained "Category name must be between 3 and 10 characters"

  @API @Category_Create_005 @validation @boundary @215027P
  Scenario: Create category with more than 10 characters
    Given the user is authenticated as admin
    When the admin creates a category with more than 10 characters "ThisNameIsTooLongForCategory"
    Then the category creation should fail with validation error
    And the error message should be contained "Category name must be between 3 and 10 characters"

  @API @Category @215013U
  Scenario: Create category without name
    Given the user is authenticated as admin
    When the admin creates a category without name ""
    Then the category creation should fail with validation error

  @API @Category @215013U
  Scenario: Create category as user
    Given the user is authenticated as user
    When the user creates a category with name "abcd"
    Then the user should be denied permission to create a category

  @API @API_Category_Create_006 @validation @duplicate @215027P
  Scenario: Create duplicate category name
    Given the user is authenticated as admin
    And a category named "Roses" exists
    When the admin attempts to create another category with name "Roses"
    Then the API should return 400 Bad Request
    And the error message should be contained "already exists"

  @API @Category @API_Category_Create_007 @negative @215098G
  Scenario: Verify admin cannot create category with non-existent parent ID
    Given the user is authenticated as admin
    When the admin creates a category with a non-existent parent category ID
    Then the API should return 404 Not Found
    And the error message should be contained "Category not found"