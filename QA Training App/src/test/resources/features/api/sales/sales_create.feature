@sales
Feature: Sales API - Create Sale

  @API @Sales_Business_001
  Scenario: Sale reduces plant inventory
  Scenario: Create Sale with Valid Quantity - Admin
    Given admin is authenticated
    And plant exists with sufficient stock
    When admin creates a sale with valid quantity
    Then sale should be created successfully
    And plant stock should be reduced accordingly

  Scenario: Create Sale with Invalid Quantity (0) - Admin
    Given admin is authenticated
    And a plant exists with sufficient stock
    When admin creates a sale with quantity 2
    Then the sale should be created with status 201
    And the plant quantity should be reduced by 2
    And plant exists with sufficient stock
    When admin creates a sale with quantity 0
    Then sale creation should fail with status 400
    And error message should be "Quantity must be greater than 0"

  Scenario: Create Sale with Non-existent Plant ID - Admin
    Given admin is authenticated
    When admin creates a sale for plant 999999 with quantity 1
    Then sale creation should fail with status 404
    And error message should be "Plant not found"
