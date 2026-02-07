Feature: Management of Plants - Delete
  As an Admin user
  I want to be able to delete an existing plant
  So that I can remove obsolete plants

  Background:
    Given the user is logged in as Admin with username "admin" and password "admin"

  @ui @delete_plant @215063V
  Scenario: UI_Plant_Delete_001 Delete Plant (Success)
    Given the user is on the Plants page
    # Create a plant to delete (Pre-condition step to ensure a known plant exists)
    When the user clicks on the "Add a Plant" button
    And the user enters "DeleteMe" as the Plant Name
    And the user selects a Category from the dropdown
    And the user enters "10" as the Price
    And the user enters "5" as the Quantity
    And the user clicks the Save button
    And the "Plant added successfully" message is displayed
    And the user is redirected to the Plants list
    # Delete the plant
    When the user searches for the created plant
    And the user clicks the Delete button for the created plant
    And the user confirms the deletion in the browser dialog
    # Verify deletion
    Then the "Plant deleted successfully" message is displayed
    And the created plant is removed from the table
    And the created plant no longer appears in search results
