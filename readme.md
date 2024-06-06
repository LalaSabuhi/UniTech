# UniTech Task

## Installation

App requires Java 8, Maven, PostgreSQL, Tomcat to compile code and run.

!!! Application needs config postgres datasource !!!. You can config that in application.properties file

In project folder:
```sh
mvn clean package
```
Get the war file from target folder and drop it to Tomcat/webapps folder.
Run Tomcat.

## App Test

> Note: Swagger url is http://[localhost]/swagger-ui/index.html.

1. Register as user
2. Login and get your auth bearer token from Header
3. Add account via account-controller with your bearer token (Authorization - Bearer [TOKEN])
4. Add balance to your account (Token not required. This method must delete in future releases)
5. Transfer money between accounts
6. Get currency rates betwen 2 currencies and other things...

GOOD LUCK
