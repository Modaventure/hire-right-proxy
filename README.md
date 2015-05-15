# hire-right-proxy
Contains proxy service to the HireRight API and integration tests for it.

## Configuration for HBM project
This jar is not uploaded on any maven repository so we have to install in our local
repository in order to be available for the main HBM project.

Before setting up the project clone this repository, checkout specific version and
run
```
mvn clean install
```

## Running the tests
In order to run the tests you will have to create configuration file containing
the environment properties for the HireRight backend. For example
```
#/path/to/development-env.properties
endpoint.url=https://screening.hireright.com/ws-connect-webapi-development/api/ws-hbm/
profile=profile_name
username=some_username
password=some_username_password
```
To run the tests using this configuration file use
```
mvn clean test -DenvConfig=/path/to/development-env.properties
```
If the envConfig option is ommited the tests will default to using 
**/path/to/repo/hire-right.properties** and if the file does't exist it will
throw FileNotFound error.
