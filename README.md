# RMSAutoConcept Testing Framework

## Overview

RMSAutoConcept is an automated testing framework designed for testing RESTful APIs using Behavior-Driven Development (BDD) principles. The framework leverages Cucumber for defining test scenarios, RestAssured for API interactions, and JUnit for assertions and test execution. This setup ensures a robust and scalable approach to API testing, providing clear and concise test reports.

## Features

- **Behavior-Driven Development (BDD)**: Write readable test scenarios using Gherkin syntax.
- **RESTful API Testing**: Utilize RestAssured for seamless API request and response handling.
- **Comprehensive Reporting**: Generate detailed HTML and JSON reports with Cucumber Reporting.
mvn clean install
## Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Ensure JDK 8 or higher is installed.
- **Maven**: Install Maven for dependency management and build automation.

## Technology:

1. Programming language - `Java`
2. Build and project management tool - `Maven`
3. Testing framework - `JUnit 4`
4. Request handling - `Cucumber` & `Rest Assured`
5. Reporting framework - `maven-surefire-plugin`

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/omeragirbas/BBC_RMS_AutoConceptApi
   cd RMSAutoConcept

## Running the test cases

You can run your application using either in intellij terminal or in system terminal from project directory:
```shell script
mvn clean test
```

This command will run all tests and generate reports in the target directory.

Test Reports
HTML Report: View the detailed test execution report at target/cucumber-reports.html.
JSON Report: The JSON report is available at target/cucumber.json for integration with other tools.
