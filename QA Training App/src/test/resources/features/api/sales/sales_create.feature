Feature: Sales API - Create Sale

@sales
  Scenario: Create Sale with Valid Quantity - Admin
    Given admin is authenticated
    And plant exists with sufficient stock
    When admin creates a sale with valid quantity
    Then sale should be created successfully
    And plant stock should be reduced accordingly
