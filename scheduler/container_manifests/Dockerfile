FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine-slim

LABEL maintainer="saurabh.salcklife.io@gmail.com"

ENV APP_HOME=/job-scheduler/app/
ENV LOG_HOME=/tmp/job-scheduler/
RUN mkdir -p ${APP_HOME} ${LOG_HOME}

WORKDIR ${APP_HOME}

COPY scripts scripts/
COPY environ environ/
COPY target/scheduler-*.jar target/

ENTRYPOINT scripts/run-scheduler.sh