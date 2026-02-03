Feature: Category Read Operations

  As an authenticated user with appropriate permissions
  I want to see the category summary
  So that I can get a quick look at Number of Categories and Sub Categories


  @simple @API_Category_Read_004 @dashboard @summary
  Scenario: Categories summary API returns aggregated data
    Given the user has a valid JWT token
    When the user requests categories summary
    Then the API should return 200 OK

  @simple @API_Category_Read_005 @security @authentication
  Scenario: Categories summary API rejects invalid JWT token
    Given the user has an expired JWT token:
    """
    eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTc3ODM2MDAwLCJleHAiOjE1Nzc4MzYwMDB9.expired_signature_123
    """
    When the user requests categories summary
    Then the API should return 401 Unauthorized
  @simple @API_Category_Read_006 @security @authentication
  Scenario: API rejects requests without JWT token
    When a request is made to get categories without JWT token
    Then the API should return 401 Unauthorized