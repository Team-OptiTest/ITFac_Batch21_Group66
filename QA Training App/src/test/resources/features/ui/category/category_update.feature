Feature: Update Category
    As an admin user
    I want to update an existing category

    @UI @Category @UI_Category_Update_001 @215013U
    Scenario: Successfully update a category
        Given the user is logged in as an admin user
        When the user navigates to the categories page
        And the user navigates to the add categories page
        And the user fills in the category name with "UpdateTest"
        And the user clicks on the Save button
        And the user clicks the edit button for the "UpdateTest" category
        Then the user should be navigated to the edit category page
        And the user updates the category name to "Updated"
        And the user clicks on the Save button
        And the user should see a success message confirming the category was updated
        And the "Updated" should be listed on the categories page

    @UI @Category @UI_Category_Update_002 @negative @215013U
    Scenario: Verify that the edit button is disabled for non-admin users
        Given the user is logged in as an admin user
        When the user navigates to the categories page
        And the user navigates to the add categories page
        And the user fills in the category name with "userTest"
        And the user clicks on the Save button
        And the user is logged in as a user
        And the user navigates to the categories page
        Then the user clicks on the edit button for the "userTest" category
        And the user should not be redirected to the edit category page