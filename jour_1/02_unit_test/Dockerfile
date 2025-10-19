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
COPY ./build_and_test.sh /opt/build_and_test.sh
ENTRYPOINT ["/opt/build_and_test.sh"]

FROM base AS production
COPY ./api_test_01 /app/
RUN ./gradlew build
ENTRYPOINT ["java", "-jar", "build/libs/cobaye_test.jar"]
