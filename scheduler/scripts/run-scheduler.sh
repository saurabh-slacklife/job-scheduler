#! /usr/bin/env sh

if [ -z "${SERVICE_ENV}" ]
then
      echo "SERVICE_ENV not found using default profile"
      source ./environ/dev.env
      java -jar target/scheduler-0.0.1-SNAPSHOT.jar -Duser.timezone="UTC" -Dspring.profiles.active=default -Djdk.tls.client.protocols=TLSv1.2
else
      echo "Using Profile: ${SERVICE_ENV}"
      source ./environ/${SERVICE_ENV}.env
      java -jar target/scheduler-0.0.1-SNAPSHOT.jar -Duser.timezone="UTC" -Dspring.profiles.active=${SERVICE_ENV} -Djdk.tls.client.protocols=TLSv1.2
fi





