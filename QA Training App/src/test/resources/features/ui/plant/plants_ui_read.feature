Feature: Plant Management - UI Read Operations
  As an Admin user
  I want to search and filter plants
  So that I can find specific plants easily

  Background:
    Given the user is logged in as an admin user
    And the user is on the Plants page

  @UI @Plant @Reading @215063V
  Scenario: Filter Plants by Name
    Given multiple plants with different names exist
    When the user enters "Rose" in the "Search plant" input box
    And the user clicks the "Search" button
    Then the list updates to show only plants matching "Rose"
    And non-matching plants are hidden
