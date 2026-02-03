Feature: Category Read Operations

  As an authenticated user with appropriate permissions
  I want to see the category summary
  So that I can get a quick look at Number of Categories and Sub Categories


  @simple @API_Category_Read_004 @dashboard @summary
  Scenario: Categories summary API returns aggregated data
  Given the user is authenticated as admin
  When the admin requests categories summary
  Then the API should return 200 OK