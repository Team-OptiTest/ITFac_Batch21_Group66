Feature: Category Deletion
  As an authenticated user with appropriate permissions
  I want to delete a category
  So that I can remove incorrect or unused categories

  @API @Category
  Scenario: Delete category as admin
    Given the user is authenticated as admin
    And a category named "Temp" exists
    When the admin deletes that category
    Then the category should be deleted successfully

  @API @Category
  Scenario: Delete category as User
    Given the user is authenticated as admin
    And a category named "userTemp" exists
    And the user is authenticated as user
    When the user deletes that category
    Then the category deletion should fail
    
  @API @Category
  Scenario: Delete category with invalid ID
    Given the user is authenticated as admin
    When the admin deletes a category with non-existent ID
    Then the category deletion should fail