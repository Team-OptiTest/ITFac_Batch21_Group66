@Ui @Sales @Create @215116M
Feature: Sales UI - Create Sale

  @UI_Sales_Create_003 @Admin @215116M
  Scenario: Sell Plant Button Visibility - Admin
    Given the user is logged in as an admin
    When the user navigates to the sales page
    Then the user should be able to see the "Sell Plant" button
