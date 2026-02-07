@UI @Dashboard @Summary @215027P
Feature: Dashboard Summary Verification
  As a user
  I want to see accurate summary information on the dashboard
  So that I can trust the system statistics

  @UI @Dashboard @Summary @215027P
  Scenario: Dashboard summary card "Plants" shows accurate total plant count
    Given User logged in
    And Database has 25 plants
    When Navigate to dashboard page
    And Check "Total" in "Plants" card
    Then Summary card "Plants" shows the correct plant count
 
  @UI @Dashboard @Summary @215027P
  Scenario: Dashboard summary card "Plants" shows low stock plants
    Given User logged in
    And 2 Plants have quantity < 5
    When Navigate to dashboard page
    And Check "Low Stock" in "Plants" card
    Then Summary card "Plants" shows correct low stock count
  
  @UI @Dashboard @001 @215027P
  Scenario: Dashboard loads after login
    Given Valid credentials available
    When Login successfully
    Then Dashboard page loads immediately with summary information

