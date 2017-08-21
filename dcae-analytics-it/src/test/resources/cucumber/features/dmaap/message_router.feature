@dmaapMR
Feature: Verify DMaaP MR Functioning

  Scenario: DMaaP MR Publisher can publish message and DMaaP MR Subscriber can fetch same message
    Given DMaaP MR Service is up
    When I publish json message to publisher topic name "default" in file "data/json_message.json"
    And wait for "10" seconds
    And subscriber fetch message from publisher topic name "default"
    And compare fetched json message with published message
    Then fetched message must be same as published message
