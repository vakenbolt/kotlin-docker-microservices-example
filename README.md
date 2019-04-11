# kotlin-docker-microservices-example


#### Requirements:
- Docker 18.0+
- Gradle 5.0+
- Java 1.8+

#### Configuration:
1. Export an environment variable called `API_CONFIG` that points to the location of the server configuration JSON file.
```bash
$ export API_CONFIG=<%PATH%>/kotlin-docker-microservices-example/configs/api_server.config.json
```

2. Optionally, if you would like the route path (`/`) of the server to return a `200` status code.
```bash
export ROOT_PATH_AVAILABLE=TRUE
```
>_This is particularly useful if the server will run in container environments where the root path is used for health checks (e.g. Kubernetes on Google Cloud)_

#### Integration Tests:

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

#### Development environment:

1. Tear down the integration `docker-compose down` environment if it is running and bring up the development environment.

```bash
$ docker-compose up
```


#### HMAC Key Generator Module:
The `hmac-sha-keygen` module is available to create keys that are compatible with the encryption settings on the API server. The key used by the API server for encrypting JWT tokens is called `jwtKey` in the API server configuration file.

To generate a new key:

```bash
$ java -jar hmac-sha-keygen/build/libs/hmac-sha-keygen-0.9-SNAPSHOT-all.jar

dQWulnW5AUcrcr288/fzkl4a+Cwb59rWyIA1YRr587CcsbUdrT/iyA3rQGgJNhQLleIDJn5ipzv9z3ASKud70g
```