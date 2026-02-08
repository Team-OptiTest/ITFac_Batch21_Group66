@UI @Sales
Feature: Sales Management - Read

  @UI @Sales @UI_Sales_Read_003 @User @215116M
  Scenario: View Sales List - User
    Given at least one sales record exists in the system
    And the user is logged in as regular user
    When the user navigates to the sales page
    Then the sales list page should be displayed
    And sales records should be listed with plant name, quantity, total price, and sold date
