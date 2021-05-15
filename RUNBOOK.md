## Run Book

## Table of contents

* [Pre requisites](#pre-requisites)
* [Steps](#steps)
    * [Build, install and package all binaries/jar](#1-build-install-and-package-all-jar)
    * [Run all services via Docker Compose](#2-run-all-services-via-docker-compose)
    * [Create broker topic with specified partition and replication](#3-check-broker-topic-with-specified-partition-and-replication)
    * [Status and Logs](#4-status-and-logs)
    * [Produce jobs to Scheduling system via Producer API](#5-produce-jobs-to-scheduling-system-via-producer-api)
    * [Get Status of Job through Dashboard API](#6-get-status-of-job-through-dashboard-api)
    * [Stop all services](#7-stop-all-services)
* [Troubleshooting](#troubleshooting)
    * [Check kafka connection](#check-kafka-connection)
    * [Check Mongo connection](#check-mongo-connection)
    * [Check application logs](#check-application-logs)

### Pre requisites

1. JDK 11.
2. Installed Maven
3. Docker runtime
4. Running Kafka and Zookeeper instances/cluster.
    * Install Kafka and Zookeeper binaries and run OR
    * Run via supplied Docker-compose.

### Steps

#### 1 Build install and package all jar

* Working directory: **job-scheduling**
* Non-Windows machine:
    * Run script: `./scripts/build.sh`
* Windows machine:
    * Run individual maven install commands listed in file:
        * `scripts/build.sh`

#### 2 Run all services via Docker Compose

* Working directory: **job-scheduling**
* The below command runs; Zookeeper(zookeeper), Kafka(broker), MongoDb(mongo), producer-api(
  producer-api), scheduler(scheduler) and scheduler-dashboard-api(scheduler-dashboard-api) through
  docker-compose:

```shell
docker compose -f docker-compose.yml up --build
```

Or as docker daemon:

```shell
docker compose -f docker-compose.yml up -d --build
```

#### 3 Check broker topic with specified partition and replication

The system produces and listens on topic **`job-task-system`**. The below command will create:

1. topic: **`job-task-system`**
2. with replication factor: `1`
3. with partitions: `1`

```shell
docker-compose exec broker kafka-topics --list --bootstrap-server broker:9092
```
#### 4 Status and Logs

* Check the container status and exposed ports:
    * `docker compose ps`
* Tail logs:
    * Zookeeper
      `docker compose logs -f zookeeper`
    * Kafka broker
      `docker compose logs -f broker`
    * Mongo
      `docker compose logs -f mongo`
    * Producer API
      `docker compose logs -f producer-api`
    * Scheduler
      `docker compose logs -f scheduler`
    * Scheduler Dashboard API
      `docker compose logs -f scheduler-dashboard-api`

The individual container logs can be checked with below `docker compose` commands

#### 5 Produce jobs to Scheduling system via Producer API

1. Execute the HTTP POST on host `localhost:9000` with URI `/job/{job_type}/`
2. If HTTP status is 202 (accepted), store the value of `Location` header response. This will be
   used in next step to obtain the status Job.

Refer to sample request here [Post Curl request](runbook-examples/Post-Curl-request.txt)

```shell
  curl -i --location --request POST 'http://localhost:9000/job/push_notification/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "requestId": "JobSystemId1234",
    "jobScheduleTimeUtc": "2021-05-15T18:05:10.00Z",
    "priority": "LOW",
    "taskRequest": {
        "name": "PushNotification",
        "clientId": "fwjrbqeiurwb14234",
        "message":"Hello, World!"
    }
}'
  ```

###### Sample response:

<p align="center">
  <img src="runbook-examples/Post-Job-Curl-request.png">
  <br/>
</p>

#### 6 Get Status of Job through Dashboard API

1. Use the value of `Location`from response header from
   step [Produce jobs to Scheduling system via Producer API](#4-produce-jobs-to-scheduling-system-via-producer-api)
   and execute GET call on server `localhost:9001`

```shell
  curl --location --request GET 'http://localhost:9001/api/job/status/cHVzaF9ub3RpZmljYXRpb246NjA5ZmYyMWQ1ZTBiMjQ3MzZiOWM1MDE0'
  ```

* `json_pp` can be used to beautify Json response.

```shell
curl --location --request GET 'http://localhost:9001/api/job/status/cHVzaF9ub3RpZmljYXRpb246NjA5ZmYyMWQ1ZTBiMjQ3MzZiOWM1MDE0' | json_pp
```

###### Sample response:

<p align="center">
  <img src="runbook-examples/GET-curl-request.png">
  <br/>
</p>

#### 7 Stop all services

* Working directory: **job-scheduling**
* Execute below docker compose command:
  ```shell
  docker compose -f docker-compose.yml stop
  ```

#### Kafka Connection String from local machine

* KAFKA_BOOTSTRAP_SERVERS: localhost:29092

#### Mongo DB Connection String from local machine

* MongoDB Host: localhost
* MongoDB port: 27017
* User: root
* Password: example
* Authentication Database: admin
  ```shell
  mongodb://root:example@localhost:27017/?authSource=admin&readPreference=primary&ssl=false
   ```
  
### Troubleshooting

* #### Check kafka connection
  * Check Broker logs and see there is no error while partitioning, creating topic or replica.
    ```shell
        docker compose logs -f broker
    ```
  * Check Kafka Topics
    ```shell
    docker-compose exec broker kafka-topics --describe --topic job-task-system --bootstrap-server broker:9092
    ```
    The output should be similar to below. If there is any, it might imply that Topic wasn't created:
    ```shell
    Topic: job-task-system  PartitionCount: 1       ReplicationFactor: 1    Configs: 
        Topic: job-task-system  Partition: 0    Leader: 101     Replicas: 101   Isr: 101
    ```
* #### Check Mongo
    ```shell
        docker compose logs -f mongo
    ```
* #### Check application logs
    Refer to steps in section: [Status and Logs](#4-status-and-logs)