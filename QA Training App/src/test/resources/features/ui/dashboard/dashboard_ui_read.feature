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
