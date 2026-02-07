@UI @Sales @AccessControl @215027P
Feature: Sales Management - Access Control
  As different user roles
  I want appropriate access to sales functionality
  So that users can only perform actions according to their permissions

  @UI @Sales @AccessControl @215027P
  Scenario: User cannot access Sell Plant page (Direct URL)
    Given the user is logged in as regular user
    When the user navigates directly to the sell plant page
    Then the user is redirected to 403 page or dashboard
    And access denied message is displayed

  @UI @Sales @AccessControl @215027P
  Scenario: Regular user cannot see Sell Plant button
    Given the user is logged in as regular user
    And the user is on the Sales page
    Then the Sell Plant button should not be visible
    And the sales table should be displayed

  @UI_Sales_Create_003 @Admin @215116M
  Scenario: Sell Plant Button Visibility - Admin
    Given the user is logged in as an admin
    When the user navigates to the sales page
    Then the user should be able to see the "Sell Plant" button
