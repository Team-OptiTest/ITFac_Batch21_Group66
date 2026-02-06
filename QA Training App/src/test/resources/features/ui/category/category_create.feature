Feature: Create Category
    As an admin
    I want to be able to create categories 
    So that I can organize plants

  @Ui @Category_Create_001
  Scenario: "Add a category" button visible to admin user
    Given the user is logged in as an admin user
    When the user navigates to the categories page
    Then the user should see the "Add a category" button

  @Ui @Category_Create_002
  Scenario: Admin user can create a category
    Given the user is logged in as an admin user
    When the user navigates to the categories page
    And the user clicks the Add a category button
    And the user fills in the category name with "abcd"
    And the user clicks on the Save button
    Then the user should see a success message confirming the category was created
    And the new category "abcd" should be listed on the categories page
  
  @Ui @Category_Create_003
  Scenario: Validation error when creating a category with an empty name
    Given the user is logged in as an admin user
    When the user navigates to the categories page
    And the user clicks the Add a category button
    And the user leaves the category name field empty
    And the user clicks on the Save button
    Then the user should see a validation error message indicating that the category name is required

  @Ui @Category_Create_004
  Scenario: "Add a category" button not visible to regular user
    Given the user is logged in as a regular user
    When the user navigates to the categories page
    Then the user should not see the "Add a category" button