Feature: Plant Management - UI Read Operations
  As an Admin user
  I want to search and filter plants
  So that I can find specific plants easily

  @UI @UI_Plant_Read_001 @215063V
  Scenario: Filter Plants by Name
    Given the user is logged in as an admin user
    When the user navigates to the Plants page
    And multiple plants with different names exist
    And the user enters "Rose" in the "Search plant" input box
    And the user clicks the "Search" button
    Then the list updates to show only plants matching "Rose"
    And non-matching plants are hidden

  @UI @UI_Plant_Read_002 @215063V
  Scenario: View Plants List (Read-Only)
    Given the user is logged in as a normal user
    When the user navigates to the Plants page
    Then the list of plants is displayed with valid data
    And the "Add a Plant" button is not present

  @UI @UI_Plant_Read_003 @215063V
  Scenario: Filter Plants by Name - Normal User
    Given the user is logged in as a normal user
    When the user navigates to the Plants page
    And multiple plants with different names exist
    And the user enters "Rose" in the "Search plant" input box
    And the user clicks the "Search" button
    Then the list updates to show only plants matching "Rose"
    And non-matching plants are hidden

  @UI @UI_Plant_Read_004 @215063V
  Scenario: Filter Plants by Category
    Given the user is logged in as a normal user
    When the user navigates to the Plants page
    And plants of different categories exist
    And the user selects the "Herbs" category from the filter
    And the user clicks the "Search" button
    Then the list updates to show only plants belonging to the "Herbs" category
