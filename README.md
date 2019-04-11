# kotlin-docker-microservices-example

<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/The_NBA_Finals_logo.svg/1280px-The_NBA_Finals_logo.svg.png" alt="NBA Finals logo" height="80">

A Vertx microservice that provides the winning team statistics for the NBA Finals(1980-2018). The data set for this example is found [here](https://www.kaggle.com/daverosenman/nba-finals-team-stats/version/6).  **_Swagger docs forthcoming_**.
>Note: The dataset mentioned above has a couple of spelling errors that are corrected in the imported version for this project.


##### Table of Contents
- [Requirements](#requirements)
- [Configuration](#configuration)
- [Integration Tests](#integration_tests)
- [Development Environment](#dev_env)
- [JWT HMAC Key Generator Module](#jwt_hmac)
- [CURLing the API Server](#curl_api)

<a name="requirements"/>

### Requirements:
- Docker 18.0+
- Gradle 5.0+
- Java 1.8+

<a name="configuration"/>

### Configuration:
1. Export an environment variable called `API_CONFIG` that points to the location of the server configuration JSON file.
```bash
$ export API_CONFIG=<%PATH%>/kotlin-docker-microservices-example/configs/api_server.config.json
```

2. Optionally, if you would like the route path (`/`) of the server to return a `200` status code.
```bash
export ROOT_PATH_AVAILABLE=TRUE
```
>_This is particularly useful if the server will run in container environments where the root path is used for health checks (e.g. Kubernetes on Google Cloud)_

<a name="integration_tests"/>

### Integration Tests:

1. Start by building the API server and all associated modules.
```bash
$ ./gradlew clean build shadowJar
```

2. Build a Docker image for the successfully built API server.
```bash
$ docker build -t nba_finals_web_api:0.9 web-api/
```

3. Start the integration testing environment via `docker-compose`
```bash
$ docker-compose -f docker-compose-integration.yml up
```

4. Run the integration tests.
```bash
$ ./gradlew test -Dtest.profile=integration
```

To shutdown an environment running via `docker-compose`:
```bash
$ docker-compose down -v --remove-orphans
```

<a name="dev_env"/>

### Development environment:

1. Tear down the integration `docker-compose down` environment if it is running and bring up the development environment.

```bash
$ docker-compose up
```

<a name="jwt_hmac"/>

### JWT HMAC Key Generator Module:
The `hmac-sha-keygen` module is available to create keys that are compatible with the encryption settings on the API server. The key used by the API server for encrypting JWT tokens is called `jwtKey` in the API server configuration file.

To generate a new key:

```bash
$ java -jar hmac-sha-keygen/build/libs/hmac-sha-keygen-0.9-SNAPSHOT-all.jar

dQWulnW5AUcrcr288/fzkl4a+Cwb59rWyIA1YRr587CcsbUdrT/iyA3rQGgJNhQLleIDJn5ipzv9z3ASKud70g
```

<a name="curl_api"/>

### CURLing the API Server:

Sign up as a new user to get an authentication token.
```bash
$ curl -i http://localhost:8080/signUp -d '{"username":"ChicagoBulls", "password":"Testing123!"}'

HTTP/1.1 201 Created
content-length: 159

{"bearer":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNTU1MDEwMDc1fQ.m4RBIjn7qYJOr57XAdyDl_BTLOEFFmFCqCQIaKJBR6HzDDawzIDtGcGDab9z_ERSXEBsNvHKHKv2WmwxtyXTmw"}
```

To authenticate.
```bash
curl -i http://localhost:8080/authenticate -d '{"username":"ChicagoBulls", "password":"Testing123!"}'

HTTP/1.1 201 Created
content-length: 159

{"bearer":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNTU1MDExNDQ1fQ.WiqtD_0EHXD42J4weKgMKNM5FgfNQmZ_M7-kgJdlSJnQiFc7GLJm5mCxBCswMwPPE2eq2gBADv33WTQQ7wlL1w"}
```

To get all of the games for a given year.
```bash
curl -i http://localhost:8080/games/2018 -H 'Authorization: Bearer <%auth_token%>'
```

To get all of the games won for a given year.
```bash
curl -i http://localhost:8080/games/2010/wins -H 'Authorization: Bearer <%auth_token%>'
```

To get all of the games lost for a given year.
```bash
curl -i http://localhost:8080/games/2001/losses -H 'Authorization: Bearer <%auth_token%>'
```

To get all of the home games for a given year.
```bash
curl -i http://localhost:8080/games/1992/home -H 'Authorization: Bearer <%auth_token%>'
```

To get all of the away games for a given year.
```bash
curl -i http://localhost:8080/games/1987/away -H 'Authorization: Bearer <%auth_token%>'
```

To get all of the years that data is available.
```bash
curl -i http://localhost:8080/getYears -H 'Authorization: Bearer <%auth_token%>'
```

To get all of the teams in the data set.
```bash
curl -i http://localhost:8080/getTeams -H 'Authorization: Bearer <%auth_token%>'
```

To get all of the games by team.
```bash
curl -i http://localhost:8080/getGamesByTeam -d '{"team":"Bulls"}' -H 'Authorization: Bearer <%auth_token%>'
```

To get all of the games by team for a given year.
```bash
curl -i http://localhost:8080/getGamesByTeamAndYear -d '{"team":"Lakers","year":1980}' -H 'Authorization: Bearer <%auth_token%>'
```

To perform a health check.
```bash
curl -i http://localhost:8080/healthCheck

HTTP/1.1 200 OK
content-length: 0
```

### Architecture:

#### Security
**MySQL:**
The `docker-compose` file instructs MySQL to auto-generate the _root_ password and not expose it.  A non-root user called `nba_finals_db` is created instead. MySQL is configured via startup SQL scripts located in `configs/docker-entrypoint-initdb.d`. The `nba_finals_db` user is _not_ used to connect to the database from the API Server. This is accomplished with the `nba_finals_service` user which is created in the `3_create_mysql_users_and_roles.sql` script.
  >**Important Note:**  The `nba_finals_service` user has _very_ limited permissions. It is _only_ allowed to `SELECT` on all tables in the `champions` database except for the `user` table on which it can perform an `INSERT`.


 **JWT:**
 [Tokens](https://jwt.io/) are signed with HS512 and used for authentication and accessing secure endpoints. Once a token is granted, it is white-listed in Redis. The life-cycle of the Redis key is equal to the expiration time associated with the token. This ensures that an expired token is automatically removed from the white-list. Only tokens granted by the API server will be honored for authentication and verification.


#### Endpoints and Resolvers:
The code revolves around the concept of **Endpoint Handlers** and **Resolvers**. An endpoint handler is responsible for extracting and validating all of the information that a resolver would need to execute the request. For example, `AuthenticationEndpoint` parses the request and calls an `AuthenticationResolver` to execute _(or resolve)_ the request. Dependency injection is used to connect endpoint handlers to resolvers. Endpoints implement the `Endpoint` interface and are provided using Guice's `Multibinder`. All resolver functions return a `ResolverResponse<T>` which contains the response payload and status code.

#### Responder:
Every `Endpoint` has a `Responder` injected as a dependency used to handle API responses. The Responder takes care of routing paths and invoking the resolver function. The `Responder` will catch and handle any exceptions that occur within the resolver function.

#### Localization:
Localized error messages are provided via the `localized_exception_messages` resource bundle located in the `resources` folder in `web-api`. Localized messages are available via the `ApiException` which is provided by the `ApiExceptionFactory` class.
>Note: Localization is currently not supported for JSR-303 annotations. These will be provided later via configurations available in the Hibernate Validation Library.

#### Dependency Injection:
Dependency injection is provided via the Google Guice library. There is only one (and _only_ one) injector used to provide an instance. This injector kicks off the whole dependency graph and makes the application available for use. The injector is found in the `main` function in `NBAFinalsApiServer.kt`. Guice Modules are also added in the `main` function. Below is a list of the Guice modules used in the API server.

Module | Description
------------ | ------------
DaoModule | Provides JDBI data access objects.
EndpointsModule | Provides all of the Endpoint Handlers.
HikariModule | Provides HikarCP database connection pool.
HttpServerModule | Provides Vertx HTTP Server.
JacksonModule | Provides Jackson's `ObjectMapper` for serializing/de-serializing JSON objects.
JdbiModule | Provides JDBI with SqlObject and Kotlin native support.
JwtModule | Provides the `JwtAuthentication` class used to handle JWT authentication.
LocalizationModule | Provides localization support
RedisModule | Provides Redis connectivity via the `Jedis` library
ServerConfigModule | Provides the `ServerConfig` class.
