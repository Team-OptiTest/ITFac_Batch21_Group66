Feature: Managing Plants Update Functionality

  @UI @UI_Plant_Update_001 @215063V
  Scenario: UI_Plant_Update_001 Edit Plant Details (Success)
    Given the user is logged in as an admin user
    And at least one plant exists in the list
    When the user navigates to the Plants page
    And the user identifies the first plant in the list as the target
    And the user clicks the Edit button for the target plant
    And the user enters "25.00" as the Price
    And the user enters "50" as the Quantity
    And the user clicks the Save button
    Then the "Plant updated successfully" message is displayed
    And the user is redirected to the Plants list
    And the target plant shows price "25.00" and quantity "50" in the table

  @UI @UI_Plant_Update_002 @215063V
  Scenario: UI_Plant_Update_002 Restricted Actions - No Edit
    Given the user is logged in as a normal user
    And at least one plant exists in the list
    When the user navigates to the Plants page
    And the user observes the columns in the plants table
    Then there are no "Edit" or "Delete" buttons visible for any plant row
    And the "Actions" column is empty or not present
