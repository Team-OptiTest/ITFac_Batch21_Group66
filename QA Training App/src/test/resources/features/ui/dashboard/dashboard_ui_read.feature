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
  
  @UI @Dashboard @002 @215027P
  Scenario: Dashboard navigation menu active state
    Given User logged in
    When Navigate to dashboard page
    Then Verify "Dashboard" menu item is highlighted with active CSS class
  
  @UI @Dashboard @001 @215027P
  Scenario: Dashboard loads after login
    Given Valid credentials available
    When Login successfully
    Then Dashboard page loads immediately with summary information

  @UI @Dashboard @Summary @215027P
  Scenario: Dashboard summary card "Categories" shows number of "main" and "sub" categories
    Given User logged in
    And Database has 3 main categories and 8 sub-categories
    When Navigate to dashboard page
    And Check "Main" and "Sub" in "Categories" card
    Then Summary card "Categories" shows correct main categories count
    And Summary card "Categories" shows correct sub categories count
  
  @UI @Dashboard @Summary @215027P
  Scenario: Dashboard summary card "Sales" shows number of Sales and Revenue
    Given User logged in
    And Database has sales 10 sales and Rs. 10000 revenue
    When Navigate to dashboard page
    And Check "Revenue" and "Sales" in "Sales" card
    Then Summary card "Sales" shows "10000" Revenue and "10" Sales