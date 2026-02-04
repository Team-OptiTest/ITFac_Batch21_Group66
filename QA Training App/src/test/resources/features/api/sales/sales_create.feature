Feature: Sales API - Create Sale

  @simple @API_Sales_Business_001
  Scenario: Sale reduces plant inventory
    Given admin is authenticated
    And a plant exists with quantity ≥ 5
    When admin gets the plant to note current quantity
    And admin creates a sale with quantity 2
    Then the sale should be created with status 201
    When admin gets the plant again
    Then the plant quantity should be reduced by 2