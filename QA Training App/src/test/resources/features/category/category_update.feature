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

  @API @Category
  Scenario: Update category as a user
    Given the user is authenticated as admin
    And a category exists with name "abc"
    And the user is authenticated as user
    When the user updates that category with name "updatedAbc"
    Then the category update should fail
