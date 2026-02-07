Feature: Managing Plants Update Functionality

  @UI @UI_Plant_Update_002 @215063V
  Scenario: UI_Plant_Update_002 Restricted Actions - No Edit
    Given the user is logged in as a normal user
    And at least one plant exists in the list
    When the user navigates to the Plants page
    And the user observes the columns in the plants table
    Then there are no "Edit" or "Delete" buttons visible for any plant row
    And the "Actions" column is empty or not present
