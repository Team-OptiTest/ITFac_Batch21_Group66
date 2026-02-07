@sales @delete
Feature: Sales API - Delete Sale

  @215116M @API_Sales_Delete_001 @Admin @Positive
  Scenario: Delete Sale by Valid ID - Admin
    Given admin is authenticated
    And a sale exists with a known valid saleId
    When admin deletes the sale with valid saleId
    Then the sale should be deleted successfully with status 204
    And the deleted sale should not be retrievable
