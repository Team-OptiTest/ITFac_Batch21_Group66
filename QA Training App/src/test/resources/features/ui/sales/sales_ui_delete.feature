@UI @Sales
Feature: Sales Management - Delete

  @UI @Sales @UI_Sales_Delete_001
  Scenario: Verify admin can cancel deletion of a sale
    Given the user is logged in as an admin
    And the user is on the Sales page
    And at least one sales record exists
    When the admin clicks the delete icon for a sales record
    Then a confirmation prompt should be displayed
    When the admin clicks cancel on the confirmation prompt
    Then the sales record should still be visible on the page

    @UI @Sales @UI_Sales_Delete_003 @User @215116M
  Scenario: Delete Action Hidden - User
    Given the user is logged in as regular user
    And the user is on the Sales page
    And at least one sales record exists
    Then the delete action should not be visible for any sales record


