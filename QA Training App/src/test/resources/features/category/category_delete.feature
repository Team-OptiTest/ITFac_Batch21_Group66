Feature: Category Deletion
  As an authenticated user with appropriate permissions
  I want to delete a category
  So that I can remove incorrect or unused categories

  @API @Category
  Scenario: Delete category as admin
    Given the user is authenticated as admin
    And a category exists with name "Temp"
    When the admin deletes that category
    Then the category should be deleted successfully

  @API @Category
  Scenario: Delete category as User
    Given the user is authenticated as admin
    And a category exists with name "userTemp"
    And the user is authenticated as user
    When the user deletes that category
    Then the category deletion should fail