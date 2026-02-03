@sales
Feature: Sales API - Create Sale

  Scenario: Create Sale with Valid Quantity - Admin
    Given admin is authenticated
    And plant exists with sufficient stock
    When admin creates a sale with valid quantity
    Then sale should be created successfully
    And plant stock should be reduced accordingly

  Scenario: Create Sale with Invalid Quantity (0) - Admin
    Given admin is authenticated
    And plant exists with sufficient stock
    When admin creates a sale with quantity 0
    Then sale creation should fail with status 400
    And error message should be "Quantity must be greater than 0"
