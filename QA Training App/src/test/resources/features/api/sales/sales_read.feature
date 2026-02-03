@sales @read
Feature: Sales API - Read Sales

  Scenario: API_Sales_Read_002 - Retrieve All Sales - Admin
    Given admin is authenticated
    And at least one sale exists in the system
    When admin retrieves all sales
    Then all sales should be returned successfully

  Scenario: API_Sales_Read_003 - Retrieve All Sales - User
    Given admin is authenticated
    And at least one sale exists in the system
    Given user is authenticated
    When user retrieves all sales
    Then all sales should be returned successfully
