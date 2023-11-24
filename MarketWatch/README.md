# marketwatch
Marketwatch project

A simple marketwatch project with the following:
- Create
- List all marketwatches
- Add / Remove marketwatch
- Add / Remove scrip to marketwatch

The project is being developed with Quarkus. Hence maven commands will be different.
All the other spring related tech reference remains same.

## Pre-Requisites:
- Java 11 or above
- MySQL or MariaDB
- Keycloak

## Pre-Process
- Run the script under scripts/mariadb.sql for table creation
- Run Keycloak server
    - Login to admin
    - At the bottom of the screen, click import
    - Import the keycloak config file
    ```
    scripts/keycloak_realm_config.json
    ```
    - After import, select clients --> amsso --> installation --> Keycloak IDC JSON format


## Build and Deployment
Normal maven commands can be used to do a build:
```
mvn clean install
``` 
For running locally, run with following command:
```
mvnw quarkus:dev 
```
## API Documentation / Swagger Documentation
Once the application is run, access the swagger api documentation in the following url:
```
http://localhost:9000/q/swagger-ui/
```