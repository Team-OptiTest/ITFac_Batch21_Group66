Feature: Category Deletion
  As an authenticated user with appropriate permissions
  I want to delete a category
  So that I can remove incorrect or unused categories

  @simple
  Scenario: Delete category as admin
    Given the user is authenticated as admin
    When the admin deletes an existing category with ID 3
    Then the category should be deleted successfully