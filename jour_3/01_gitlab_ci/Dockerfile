FROM openjdk:17-jdk-slim-bullseye AS base

RUN useradd -m spring
RUN mkdir app && chown spring:spring /app

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y locales
RUN sed -i -e 's/# fr_FR.UTF-8 UTF-8/fr_FR.UTF-8 UTF-8/' /etc/locale.gen && \
    dpkg-reconfigure --frontend=noninteractive locales && \
    update-locale LANG=fr_FR.UTF-8

USER spring
WORKDIR /app

FROM base AS testing
COPY ./build_and_run.sh /opt/build_and_run.sh
COPY ./build_and_test.sh /opt/build_and_test.sh
ENTRYPOINT ["/opt/build_and_run.sh"]

FROM base AS production
COPY ./cobaye_api /app/
USER root
RUN chown -R spring:spring /app
USER spring
RUN ./gradlew clean build -x test
ENTRYPOINT ["java", "-jar", "/app/build/libs/cobaye_api.jar"]

FROM production AS ci
COPY ./data.json /var/data.json
ENTRYPOINT ["/app/gradlew", "test"]
