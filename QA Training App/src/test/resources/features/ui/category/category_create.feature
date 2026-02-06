Feature: Create Category
    As an admin
    I want to be able to create categories 
    So that I can organize plants

  Scenario: "Add a category" button visible to admin user
    Given the user is logged in as an admin user
    When the user navigates to the categories page
    Then the user should see the "Add a category" button