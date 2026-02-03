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

  Scenario: API_Sales_Read_004 - Retrieve Sale by Valid ID - User
    Given admin is authenticated
    And a sale exists with a known valid saleId
    Given user is authenticated
    When user retrieves the sale with valid saleId
    Then the sale details should be returned successfully

  Scenario: API_Sales_Read_005 - Retrieve Sale with Invalid ID - User
    Given user is authenticated
    When user retrieves the sale with non-existent saleId 99999
    Then the retrieval should fail with status 404
    And error message should be "Sale not found: 99999"

  Scenario: API_Sales_Read_006 - Retrieve Sale - Unauthorized Access
    Given admin is authenticated
    And a sale exists with a known valid saleId
    And no user is logged in
    When an unauthenticated user retrieves the sale with valid saleId
    Then the retrieval should fail with status 401
    And error message should be "Unauthorized - Use Basic Auth or JWT"

  Scenario: API_Sales_Read_007 - Retrieve Sales with Pagination and Sorting - User
    Given admin is authenticated
    And multiple sales exist in the system
    And user is authenticated
    When user retrieves the sales with page 0, size 10, and sort "soldAt,desc"
    Then the paginated sales should be returned successfully with page size 10
