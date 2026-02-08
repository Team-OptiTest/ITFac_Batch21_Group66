Feature: Delete Category
    As an admin user
    I want to delete an existing category

  @UI @Category @UI_Category_Delete_001 @215013U
  Scenario: Verify that the admin can delete a category
    Given the user is logged in as an admin user
    When the user navigates to the categories page
    And the user navigates to the add categories page
    And the user fills in the category name with "DeleteTest"
    And the user clicks on the Save button
    And the user should see a success message confirming the category was created
    And the user clicks the delete button for the "DeleteTest" category
    And the user confirms the deletion
    Then the user should see a success message confirming the category was deleted
    And the "DeleteTest" category should no longer be listed on the categories page

  @UI @Category @UI_Category_Delete_002 @negative @215013U
  Scenario: Verify that the delete button is disabled for non-admin users
    Given the user is logged in as an admin user
    When the user navigates to the categories page
    And the user navigates to the add categories page
    And the user fills in the category name with "DeleteTest"
    And the user clicks on the Save button
    And the user is logged in as a user
    And the user navigates to the categories page
    Then the delete button for each category should be disabled to the user
