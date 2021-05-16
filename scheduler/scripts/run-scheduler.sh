#! /usr/bin/env sh

if [ -z "${SERVICE_ENV}" ]
then
      echo "SERVICE_ENV not found using default dev profile"
      source ./environ/dev.env

      echo "Sleeping for 20 seconds on Kafka Broker to start"
      sleep 20s

      java -jar -Duser.timezone="UTC" -Dspring.profiles.active=dev target/scheduler-0.0.1-SNAPSHOT.jar
else
      echo "Using Profile: ${SERVICE_ENV}"
      source ./environ/${SERVICE_ENV}.env
      echo "Sleeping for 20 seconds on Kafka Broker to start"
      sleep 20s
      java -jar -Duser.timezone="UTC" -Dspring.profiles.active=${SERVICE_ENV} target/scheduler-0.0.1-SNAPSHOT.jar
fi





