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

  @UI_Sales_Create_004 @Admin @215116M
  Scenario: Access Sell Plant Page - Admin
    Given the user is logged in as an admin
    When the user navigates directly to the sell plant page
    Then the Sell Plant page should be displayed
    And the plant dropdown should be visible
    And the quantity field should be visible

  @UI @Sales @UI_Sales_Create_002 @215098G
  Scenario: Verify admin can cancel a sale without creating it
    Given the user is logged in as an admin
    When the user navigates directly to the sell plant page
    And the user selects a plant from the dropdown
    And the user enters a valid quantity
    And the user clicks the Cancel button
    Then the user should be redirected to the sales page

  @UI @Sales @UI_Sales_Create_009 @Admin @215116M
  Scenario: Plant Required Validation - Admin
    Given the user is logged in as an admin
    When the user navigates directly to the sell plant page
    And the user leaves the plant dropdown empty
    And the user enters quantity "1"
    And the user clicks the Sell button
    Then the user should see the plant required validation message


    @UI @Sales @UI_Sales_Create_010 @Admin @215116M
Scenario: Quantity Greater Than Zero Validation - Admin
  Given the user is logged in as an admin
  When the user navigates directly to the sell plant page
  And the user selects a plant from the dropdown
  And the user sets quantity as "0" using javascript
  And the user submits the sale form bypassing browser validation
  Then the user should see the quantity greater than zero validation message
