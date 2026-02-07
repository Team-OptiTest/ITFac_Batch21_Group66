@sales @read
Feature: Sales API - Read Sales

  @215116M @API_Sales_Read_002 @Admin @Positive
  Scenario: API_Sales_Read_002 - Retrieve All Sales - Admin
    Given admin is authenticated
    And at least one sale exists in the system
    When admin retrieves all sales
    Then all sales should be returned successfully

  @215116M @API_Sales_Read_003 @User @Positive
  Scenario: API_Sales_Read_003 - Retrieve All Sales - User
    Given admin is authenticated
    And at least one sale exists in the system
    Given user is authenticated
    When user retrieves all sales
    Then all sales should be returned successfully

  @215116M @API_Sales_Read_004 @User @Positive
  Scenario: API_Sales_Read_004 - Retrieve Sale by Valid ID - User
    Given admin is authenticated
    And a sale exists with a known valid saleId
    Given user is authenticated
    When user retrieves the sale with valid saleId
    Then the sale details should be returned successfully

  @215116M @API_Sales_Read_005 @User @Negative
  Scenario: API_Sales_Read_005 - Retrieve Sale with Invalid ID - User
    Given user is authenticated
    When user attempts to retrieve a sale with non-existent ID
    Then the API should return 404 Not Found with message "Sale not found"

  @215116M @API_Sales_Read_006 @Unauthorized @Negative
  Scenario: API_Sales_Read_006 - Retrieve Sale – Unauthorized Access
    Given admin is authenticated
    And a valid sale exists in the system
    When an unauthenticated user attempts to retrieve the sale
    Then the API should return 401 Unauthorized

  @215116M @API_Sales_Read_007 @User @Positive
  Scenario: API_Sales_Read_007 - Retrieve Sales with Pagination and Sorting - User
    Given user is authenticated
    And multiple sales exist in the system
    When user retrieves sales page with params "page=0&size=5&sort=soldAt,desc"
    Then the response should contain pagination metadata for page 0 and size 5
    And the response content should be sorted by "soldAt" "desc"

  @API @Sales @API_Sales_Read_001 @negative @215098G
  Scenario: Verify user receives 404 error when viewing non-existent sale
    Given user is authenticated
    When user attempts to retrieve a sale with non-existent ID
    Then the API should return code 404 Not Found
    And the response should contain message "Sale not found"
