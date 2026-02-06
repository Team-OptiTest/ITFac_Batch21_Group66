Feature: Sales API - Create Sale

  @API @Sales_Business_001
  Scenario: Sale reduces plant inventory
    Given admin is authenticated
    And a plant exists with sufficient stock
    When admin creates a sale with quantity 2
    Then the sale should be created with status 201
    And the plant quantity should be reduced by 2