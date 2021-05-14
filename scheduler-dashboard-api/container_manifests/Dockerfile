FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine-slim

LABEL maintainer="saurabh.slacklife.io@gmail.com"

ARG APPLICATION_ENV

ENV APP_HOME=/scheduler-dashboard-api/app/
ENV LOG_HOME=/tmp/scheduler-dashboard-api/
ENV SERVICE_ENV=${APPLICATION_ENV}

RUN mkdir -p ${APP_HOME} ${LOG_HOME}

WORKDIR ${APP_HOME}

COPY scripts scripts/
COPY environ environ/
COPY target/job-scheduler-dashboard-api-*.jar target/

EXPOSE 9001

ENTRYPOINT scripts/run-api-server.sh
