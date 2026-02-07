@Sales @API
Feature: Sales API - Create Sale

  @215116M @API_Sales_Create_001 @Admin @Positive
  Scenario: Create Sale with Valid Quantity - Admin
    Given admin is authenticated
    And plant exists with sufficient stock
    When admin creates a sale with valid quantity
    Then sale should be created successfully


  @215116M @API_Sales_Create_002 @Admin @Negative
  Scenario: Create Sale with Invalid Quantity (0) - Admin
    Given admin is authenticated
    And plant exists with sufficient stock
    When admin creates a sale with quantity 0
    Then sale creation should fail with status 400
    And error message should be "Quantity must be greater than 0"


  @215116M @API_Sales_Create_003 @Admin @Negative
  Scenario: Create Sale with Non-existent Plant ID - Admin
    Given admin is authenticated
    When admin creates a sale for plant 999999 with quantity 1
    Then sale creation should fail with status 404
    And error message should be "Plant not found"
