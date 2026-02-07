Feature: Management of Plants - Delete
  As an Admin user
  I want to be able to delete an existing plant
  So that I can remove obsolete plants

  @UI @UI_Plant_Delete_001 @215063V
  Scenario: Delete Plant (Success)
    Given the user is logged in as an admin user
    And the user is on the Plants page
    When the user clicks on the "Add a Plant" button
    And the user enters "DeleteMe" as the Plant Name
    And the user selects a Category from the dropdown
    And the user enters "10" as the Price
    And the user enters "5" as the Quantity
    And the user clicks the Save button
    And the "Plant added successfully" message is displayed
    And the user is redirected to the Plants list
    When the user searches for the created plant
    And the user clicks the Delete button for the created plant
    And the user confirms the deletion in the browser dialog
    Then the "Plant deleted successfully" message is displayed
    And the created plant is removed from the table
    And the created plant no longer appears in search results
