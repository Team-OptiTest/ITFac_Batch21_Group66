Feature: Plant Management - UI Create Operations
  As an Admin user
  I want to add new plants through the UI
  So that I can manage the plant inventory

  Background:
    Given the admin user is authenticated
    And the user is on the Plants page

  @UI @Plant
  Scenario: Add New Plant (Success)
    When the user clicks on the "Add a Plant" button
    And the user enters "Daliya" as the Plant Name
    And the user selects a Category from the dropdown
    And the user enters "15.50" as the Price
    And the user enters "20" as the Quantity
    And the user clicks the Save button
    Then the user is redirected to the Plants list
    Then the "Plant added successfully" message is displayed
    And the user is redirected to the Plants list
    And the new plant "Daliya" appears in the table

  @UI @Plant @Validation
  Scenario: Add New Plant Validation (Failure)
    When the user clicks on the "Add a Plant" button
    And the user leaves the "Plant Name" empty
    And the user selects a Category from the dropdown
    And the user leaves the "Price" empty
    And the user enters "20" as the Quantity
    And the user clicks the Save button
    Then the form is not submitted
    And validation error messages are displayed below specific fields
    And the validation error "Plant Name is required" is displayed
    And the validation error "Price is required" is displayed

  @UI @Plant @AccessControl
  Scenario: Access Control - Normal User cannot add plant (Direct URL)
    Given the normal user is authenticated
    When the user navigates directly to the add plant page
    Then the user is redirected to the dashboard or sees access denied
