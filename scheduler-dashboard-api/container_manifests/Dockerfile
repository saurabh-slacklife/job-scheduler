FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine-slim

LABEL maintainer="saurabh.salcklife.io@gmail.com"

ENV APP_HOME=/job-scheduler-api/app/
ENV LOG_HOME=/tmp/job-scheduler-api/
RUN mkdir -p ${APP_HOME} ${LOG_HOME}

WORKDIR ${APP_HOME}

COPY scripts scripts/
COPY environ environ/
COPY target/producer-api-*.jar target/

EXPOSE 9000

ENTRYPOINT scripts/run-api-server.sh