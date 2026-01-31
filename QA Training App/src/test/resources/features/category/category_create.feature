Feature: Category Creation
  As an admin user
  I want to create a category
  So that I can organize plants

  @simple
  Scenario: Create category with valid name
    Given the user is authenticated as admin
    When the admin creates a category with valid name "Flowers"
    Then the category should be created successfully

  @simple
  Scenario: Create category with less than 3 characters
    Given the user is authenticated as admin
    When the admin creates a category with less than 3 characters "a"
    Then the category should not be created

  @simple
  Scenario: Create category with more than 10 characters
    Given the user is authenticated as admin
    When the admin creates a category with more than 10 characters "abcdefghijkl"
    Then the category should not be created

  @simple
  Scenario: Create category without name
    Given the user is authenticated as admin
    When the admin creates a category without name ""
    Then the category should not be created