# Getting start

## MyAutomation

This is a test framework built using Selenium, Cucumber, TestNG, Report Portal, Selenium Grid 4, and Jenkins.

## Features
- **Selenium & Cucumber**: Powers the core tests with a behavior-driven approach.

- **TestNG & Selenium Grid 4**: Enables parallel test method execution across various browsers, enhancing coverage and efficiency.

- **Jenkins**: Manages continuous integration, automating builds and tests.

- **Report Portal**: Provides an intuitive dashboard for detailed test reporting.

- **Docker**: Houses Selenium Grid, Jenkins, and Report Portal, ensuring a consistent environment.

## Development Environment

Before you begin, ensure that your system meets the following prerequisites:

- **IDE**: While Intellij is preferred, you can use any IDE of your choice.
- **Java JDK v17**: Ensure that JDK v17 is installed and properly configured.
- **Maven v3.9.2**: The project relies on Maven for dependency management.
- **Docker**: Needed to initialize the Selenium Grid 4, Jenkins, and Report Portal.

## Project Structure

The main components of the project structure are:

- `main`: where all Page Objects, Driver Factory, Utils located
- `test`: where all Features, Steps located

## Configuration

Before running the tests, ensure that the necessary configurations are set:

- `Docker-compose.yml`: where to init docker image
- `config.properties`: setting selenium hub url
- `reportportal.properties`: setting report portal configuration

## Running 

1. initialize the Docker images `docker-compose Docker-compose.yml up`
2. Run tests via IDE or Maven.

## Jenkins Setup
1. Access Jenkins in your browser.
2. Create a new 'Pipeline' project.
3. Configure Build triggers to Determine how your project will be built (e.g., Poll SCM, Periodically).
4. Configure Pipeline using 'Git' and point to your repository. For 'Script Path', set to pipeline/RUNSINGLE.
5. Click Build Now to run.
