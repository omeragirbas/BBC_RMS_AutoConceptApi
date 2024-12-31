@api
  Feature: Schedule API Tests

    Background:
      Given make a GET request to endpoint "/ibltest"

    Scenario: Validate HTTP status code and response time
      Then the response status code should be 200
      And the response time should be less than 1000 milliseconds

    Scenario: Validate ID field and type in response
      Then the id field for all items in the data array should not be null or empty
      And the type in episode for every item should always be "episode"

    Scenario: Validate title field in episode
      Then the title field in episode should not be null or empty for any schedule item

    Scenario: Validate that only one episode has live field as true
     Then only one episode in the list should have live field in episode as true

    Scenario: Validate transmission_start and transmission_end dates
      Then the transmission_start date should be before the transmission_end date

    Scenario: Validate the Date header in the response
      Then the response headers should contain the current Date value

    Scenario: Validate 404 error for a non-existent endpoint
      Given make a GET request to endpoint "/ibltest/2023-09-11"
      Then the response status code should be 404
      And the response should have error properties "details" and "http_response_code"