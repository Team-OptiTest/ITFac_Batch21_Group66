Feature: Plant Management API
  As an admin
  I want to manage plants in the system
  So that I can keep the plant catalog up to date

  @API @Plant @215063V
  Scenario: Create Plant with Valid Data
    Given the admin is authenticated
    And a category is created with name "ChildCat1" and parent "ParentCat1"
    When I POST to "/api/plants/category/" with category "ChildCat1" and following data:
      | name    | price | quantity |
      | Sunrose | 15.50 |      100 |
    Then the response status should be 201
    And the response should contain a plant object with an assigned ID
    And the plant name should be "Sunrose"

  @API @Plant @Authorization @215063V
  Scenario: Unauthorized Create Attempt
    Given the user is authenticated with ROLE_USER
    When I POST to "/api/plants/category/5" with following data:
      | name      | price | quantity |
      | TestPlant | 20.00 |       50 |
    Then the response status should be 403

  @API @Plant @Validation @215063V
  Scenario: Plant Name Length Validation
    Given the admin is authenticated
    And a category is created with name "ChildCat3" and parent "ParentCat3"
    When I POST to "/api/plants/category/" with category "ChildCat3" and following data:
      | name | price | quantity |
      | Ab   | 10.00 |       50 |
    Then the response status should be 400
    And the response error message should contain "Plant name must be between 3 and 25 characters"

  @API @Plant @API_Plant_Create_004 @negative @215098G
  Scenario: Verify admin cannot create duplicate plant
    Given the admin is authenticated
    And at least one plant exists in the system
    When the admin creates a plant with the same name and category as an existing plant
    Then the response status should be 400
    And the response error message should contain "Duplicate plant"

  @API @Plant @API_Plant_Create_005 @negative @215098G
  Scenario: Verify admin cannot create plant with non-existent category ID
    Given the admin is authenticated
    When the admin creates a plant with a non-existent category ID
    Then the response status should be 404
    And the error message should contain "Category not found"
