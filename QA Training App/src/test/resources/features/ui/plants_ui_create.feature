Feature: Plant Management - UI Create Operations
  As an Admin user
  I want to add new plants through the UI
  So that I can manage the plant inventory

  Background:
    Given the user is logged in as Admin
    And the user is on the Plants page

  @UI_Plant_Create_001
  Scenario: Add New Plant (Success)
    When the user clicks on the "Add a Plant" button
    And the user enters "Rose" as the Plant Name
    And the user selects a Category from the dropdown
    And the user enters "15.50" as the Price
    And the user enters "20" as the Quantity
    And the user clicks the Save button
    Then the "Plant added successfully" message is displayed
    And the user is redirected to the Plants list
    And the new plant "Rose" appears in the table
