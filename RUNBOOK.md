## Run Book

### Pre-requisites
1. JDK 11.
2. Installed Maven
3. Docker runtime
4. Running Kafka and Zookeeper instances/cluster. 
    * Install Kafka and Zookeeper binaries and run OR
    * Run via supplied Docker-compose.

### Build, install and package all binaries/jar
* Working directory: **job-scheduling**
* Run script: `./scripts/build.sh`
    
### Run locally - dev
* Working directory: **producer-api**
* Run command: `export SERVICE_ENV=dev`
* Run script: `./scripts/run-api-server.sh`

### Run via Docker
* Working directory: **job-scheduling**
* The below steps runs; Kafka, Zookeeper and Producer-api through docker-compose:
`docker compose -f docker-compose.yml up`
  
### Test the Producer API
`curl -X POST 'http://localhost:9000/job/ingest/' \
--header 'Content-Type: application/json' \
--data-raw '{"taskId": "1","taskRequest" : {"name":"1","hello":"hello"}}'`

### Stop Kafka, Zookeeper, producer-api
* Working directory: **job-scheduling**
* The below steps stops; Kafka, Zookeeper and Producer-api through docker-compose:
  `docker compose -f docker-compose.yml stop`