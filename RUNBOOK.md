## Run Book

## Table of contents

* [Pre requisites](#pre-requisites)
* [Steps](#steps)
    * [Build, install and package all binaries/jar](#1-build--install-and-package-all-binaries/jar)
    * [Run all services via Docker Compose](#2-run-all-services-via-docker-compose)
    * [Create broker topic with specified partition and replication](#3-create-broker-topic-with-specified-partition-and-replication)
    * [Produce jobs to Scheduling system via Producer API](#4-produce-jobs-to-scheduling-system-via-producer-api)
    * [Stop all services](#5-stop-all-services)
* [Logs](#logs)
    * [Local system](#local-system)
    * [Docker](#docker)

### Pre requisites

1. JDK 11.
2. Installed Maven
3. Docker runtime
4. Running Kafka and Zookeeper instances/cluster.
    * Install Kafka and Zookeeper binaries and run OR
    * Run via supplied Docker-compose.

### Steps

#### 1. Build, install and package all binaries/jar

* Working directory: **job-scheduling**
* Non-Windows machine:
    * Run script: `./scripts/build.sh`
* Windows machine:
    * Run individual maven install commands listed in file:
        * `scripts/build.sh`

#### 2. Run all services via Docker Compose

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

#### 3. Create broker topic with specified partition and replication

The system produces and listens on topic **`job-task-system`**. The below command will create:

1. topic: **`job-task-system`**
2. with replication factor: `1`
3. with partitions: `3`

```shell
  docker-compose exec broker kafka-topics --create --topic job-task-system --bootstrap-server broker:9092 --replication-factor 1 --partitions 3
```

### 4. Produce jobs to Scheduling system via Producer API

`curl -X POST 'http://localhost:9000/job/ingest/' \
--header 'Content-Type: application/json' \
--data-raw '{"taskId": "1","taskRequest" : {"name":"1","hello":"hello"}}'`

### 5. Stop all services

* Working directory: **job-scheduling**
* Execute below docker compose command:
  ```shell
    docker compose -f docker-compose.yml stop
  ```

#### Mongo DB Connection String

* MongoDB Host: localhost
* MongoDB port: 27017
* User: root
* Password: example
* Authentication Database: admin
  ```shell
    mongodb://root:example@localhost:27017/?authSource=admin&readPreference=primary&ssl=false
   ```
