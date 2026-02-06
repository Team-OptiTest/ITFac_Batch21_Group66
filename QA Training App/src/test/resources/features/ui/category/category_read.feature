Feature: Read Category
    As an authenticated user
    I want to be able to view categories
    So that I can see how plants are organized

    @Ui @Category_Read_001
    Scenario: Categories page displays list of categories
        Given the user is logged in as a user
        When the user navigates to the categories page
        Then the user should see a list of categories displayed