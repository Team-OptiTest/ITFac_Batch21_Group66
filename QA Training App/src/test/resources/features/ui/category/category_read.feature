Feature: Read Category
    As an authenticated user
    I want to be able to view categories
    So that I can see how plants are organized

  @UI @Category @UI_Category_Read_001 @215013U
  Scenario: Categories page displays list of categories
    Given the user is logged in as a user
    When the user navigates to the categories page
    Then the user should see a list of categories displayed

  @UI @Category @UI_Category_Read_002 @215013U
  Scenario: Search for a category by name
    Given the user is logged in as an admin user
    And the user navigates to the categories page
    And the user clicks the Add a category button
    And the user fills in the category name with "result"
    And the user clicks on the Save button
    And the user navigates to the categories page
    And the user clicks the Add a category button
    And the user fills in the category name with "ignore"
    And the user clicks on the Save button
    And the user is logged in as a user
    And the user navigates to the categories page
    When the user clicks on the search input field
    And the user enters "result" into the search input field
    And the user clicks the search button
    Then the user should see "result" in the search results
    And the user should not see "ignore" in the search results

  @UI @Category @UI_Category_Read_006 @negative @215098G
  Scenario: Verify user sees empty message when category search returns no results
    Given the user is logged in as a user
    And the user navigates to the categories page
    When the user searches for a category that does not exist
    Then the message "No category found" should be displayed in the table body

  @UI @Category_Read_003 @215013U
  Scenario: Filter categories by parent category
      Given the user is logged in as an admin user
      And the user navigates to the categories page
      And the user clicks the Add a category button
      And the user fills in the category name with "Parent"
      And the user clicks on the Save button
      And the user navigates to the categories page
      And the user clicks the Add a category button
      And the user fills in the category name with "Child"
      And the user selects "Parent" as the parent category
      And the user clicks on the Save button
      And the user is logged in as a user
      And the user navigates to the categories page
      And the user clicks on the parent category filter dropdown
      And the user selects "Parent" from the dropdown
      Then the user should see "Child" in the filtered results
