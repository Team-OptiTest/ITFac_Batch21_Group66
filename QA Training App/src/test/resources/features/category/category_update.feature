Feature: Category Update
  As an admin user
  I want to update existing categories
  So that I can modify category information

  @API @Category
  Scenario: Update category with valid data
    Given the user is authenticated as admin
    And a category exists with name "new"
    When the admin updates that category with name "Updatednew"
    Then the category should be updated successfully
