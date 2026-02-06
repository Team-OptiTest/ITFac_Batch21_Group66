Feature: User Authentication
  As a user
  I want to be able to login to the application
  So that I can access the system

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters username "admin"
    And the user enters password "admin123"
    And the user clicks the login button
    Then the user should be logged in successfully