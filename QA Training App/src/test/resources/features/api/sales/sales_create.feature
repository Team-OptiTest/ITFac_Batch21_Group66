Feature: Sales API - Create Sale

  Scenario: Create Sale with Valid Quantity - Admin
    Given admin is authenticated
    And plant exists with sufficient stock
    When admin creates a sale with valid quantity
    Then sale should be created successfully


  Scenario: Create Sale with Invalid Quantity (0) - Admin
    Given admin is authenticated
    And plant exists with sufficient stock
    When admin creates a sale with quantity 0
    Then sale creation should fail with status 400
    And error message should be "Quantity must be greater than 0"


  Scenario: Create Sale with Non-existent Plant ID - Admin
    Given admin is authenticated
    When admin creates a sale for plant 999999 with quantity 1
    Then sale creation should fail with status 404
    And error message should be "Plant not found"

  @215027P @API_Sales_Create_001 @Admin @Positive
  Scenario: Create Sale with Valid Quantity - Admin
    Given admin is authenticated
    And plant exists with sufficient stock
    When admin creates a sale with valid quantity
    Then verify plant quantity decreased by sale quantity
