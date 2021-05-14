#! /usr/bin/env sh

if [ -z "${SERVICE_ENV}" ]
then
      echo "SERVICE_ENV not found using default dev profile"
      source ./environ/dev.env
      java -jar -Duser.timezone="UTC" -Dspring.profiles.active=dev target/job-scheduler-dashboard-api-0.0.1-SNAPSHOT.jar
else
      echo "Using Profile: ${SERVICE_ENV}"
      source ./environ/${SERVICE_ENV}.env
      java -jar -Duser.timezone="UTC" -Dspring.profiles.active=${SERVICE_ENV} target/job-scheduler-dashboard-api-0.0.1-SNAPSHOT.jar
fi

