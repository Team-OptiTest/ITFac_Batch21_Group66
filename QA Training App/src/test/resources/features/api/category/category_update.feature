Feature: Category Update
  As an admin user
  I want to update existing categories
  So that I can modify category information

  @API @Category @215013U
  Scenario: Update category with valid data
    Given the user is authenticated as admin
    And a category named "new" exists
    When the admin updates that category with name "Updatednew"
    Then the category should be updated successfully

  @API @Category @215013U
  Scenario: Update category as a user
    Given the user is authenticated as admin
    And a category named "abc" exists
    And the user is authenticated as user
    When the user updates that category with name "updatedAbc"
    Then the category update should fail

  @API @Category @215013U
  Scenario: Update category with an invalid ID
    Given the user is authenticated as admin
    When the admin updates a category with non-existent ID
    Then the category update should fail