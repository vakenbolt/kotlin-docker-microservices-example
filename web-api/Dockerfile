FROM openjdk:8-jre-alpine

ENV DOCKERIZE_VERSION v0.6.0
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

COPY build/libs/web-api-0.9-SNAPSHOT-all.jar /server/web-api-0.9-SNAPSHOT-all.jar
CMD dockerize -wait tcp://nba_finals_mysql_integration:3306 -timeout 5m java -jar /server/web-api-0.9-SNAPSHOT-all.jar
