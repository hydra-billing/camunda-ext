FROM alpine as artifacts

RUN apk add git maven openjdk8 bash zip curl -f

COPY ./demo_processes/pizza_order/src      /usr/src/demo_processes/pizza_order/src
COPY ./demo_processes/pizza_order/pom.xml  /usr/src/demo_processes/pizza_order/pom.xml
COPY ./demo_processes/pizza_order/build.sh /usr/src/demo_processes/pizza_order/build.sh

COPY ./seed/src                            /usr/src/seed/src
COPY ./seed/pom.xml                        /usr/src/seed/pom.xml
COPY ./seed/build.sh                       /usr/src/seed/build.sh
COPY ./src                                 /usr/src/src
COPY ./pom.xml                             /usr/src/pom.xml

ENV ROOT_DIR /usr/src/

WORKDIR $ROOT_DIR

# Build camunda-ext
RUN mvn clean compile package

# Copy all JAR dependencies to ./target/dependencies
RUN mvn dependency:copy-dependencies -U

# Build seed
RUN cd ./seed && \
    ./build.sh && \
    cd $ROOT_DIR

FROM camunda/camunda-bpm-platform:7.9.0

SHELL ["/bin/bash", "-c"]
USER root
RUN rm -rf /camunda/webapps/camunda-invoice \
           /camunda/webapps/examples \
           /camunda/lib/groovy-all-2.4.13.jar \
           /camunda/lib/mail-1.4.1.jar && \
    apk add wget curl busybox-extras -f

USER camunda
RUN sed -i 's/<!-- <filter>/<filter>/' /camunda/webapps/engine-rest/WEB-INF/web.xml && sed -i 's/<\/filter-mapping> -->/<\/filter-mapping>/' /camunda/webapps/engine-rest/WEB-INF/web.xml
COPY --chown=camunda:camunda ./camunda.sh /camunda/
COPY ./context.xml /camunda/conf/

COPY --from=artifacts /usr/src/target/dependencies/*.jar /camunda/lib/
COPY --from=artifacts /usr/src/seed/target/*.war /camunda/webapps/
COPY --from=artifacts /usr/src/target/camunda-ext-*.jar /camunda/lib/camunda-ext.jar

ENV DB_DRIVER= \
    DB_HOST= \
    DB_PORT= \
    DB_NAME= \
    DB_USERNAME= \
    DB_PASSWORD= \
    DB_URL=

LABEL maintainer="Hydra Billing <info@hydra-billing.com>" \
  org.opencontainers.image.authors="Hydra Billing <info@hydra-billing.com>" \
  org.opencontainers.image.title="camunda-ext" \
  org.opencontainers.image.description="Camunda with camunda-ext library inside" \
  org.opencontainers.image.vendor="Hydra Billing Solutions LLC" \
  org.opencontainers.image.licenses="Apache License 2.0" \
  org.opencontainers.image.url="https://hub.docker.com/r/latera/camunda" \
  org.openbuildservice.disturl="https://github.com/latera/camunda-ext/releases" \
  org.opencontainers.image.source="https://github.com/latera/camunda-ext.git" \
  org.opencontainers.image.documentation="https://latera.github.io/camunda-ext"

HEALTHCHECK --start-period=60s \
  CMD wget -q --spider http://127.0.0.1:8080/camunda/app/welcome
