[![Compile and Build](https://github.com/saurabh-slacklife/job-scheduler/actions/workflows/maven-build.yml/badge.svg)](https://github.com/saurabh-slacklife/job-scheduler/actions/workflows/maven-build.yml)

## Table of contents

* [Job Scheduling System](#job-scheduling-system)
* [Design](#design)
    * [Assumptions](#assumptions)
    * [Design Aspects](#design-aspects)
    * [Algorithm](#algorithm)
        * [Sequence Diagram](#sequence-diagram)
        * [Flow chart](#flow-chart)
        * [UML Diagram](#uml-diagram)
        * [High Level Architecture](#high-level-architecture)
    * [Project Structure](#project-structure)
    * [Tech Stack](#tech-stack)
    * [Improvements](#improvements)
    * [Not covered](#not-covered)
* [Runbook](#runbook)

### Job Scheduling System

### Design

#### Assumptions

1. Tasks are idempotent, every task is independent of other.
2. The scheduled time is always in Future.
3. All services run in UTC timezone and tasks are required to be submitted in UTZ only. Though there
   isn’t any validation to check if the supplied time zone is not UTC.

#### Design Aspects

* **Flexibility** - The flexibility of multiple job types is achieved by using a generic request
  object to be executed based on type. Though of how to handle the newAction needs to be
  implemented.
    1. Based on Action type in Post request
    2. Based upon Strategy, Command and chain of responsibility.
* **Reliability** :
    1. Each job is executed and processed in it’s own thread I.e For a single job a new thread is
       created. The thread can either fail or succeed.
    2. If a thread fails, corresponding status and reason is updated in MongoDB
* **Internal Consistency** :
    1. It’s maintained at each level - job posted, job submitted to Kafka, job consumed and
       processed in scheduler, job being executed. Hence maintaining the required status.
* **Scheduling and Priority**:
    1. Scheduling is achieved by submitting the task at a given time which is not in past the
       current UTC timestamp-Delta seconds.
    2. The Ordering is guaranteed by using the same key of value as job type, when submitting data
       to Kafka. Ensuring the Each data with same key goes to only one Partition and is read by only
       one consumer group, thereby maintaining ordering.
    3. Scheduling is implemented through ScheduledThreadPool executor.
    4. The priority is defined as: HIGH, MEDIUM and LOW
        1. LOW: This is the default. I.e with this priority, the job will be executed based upon
           it’s own time.
        2. HIGH: The high priority task will be executed in configured delta seconds from the
           current time, irrespective when it was scheduled. The design can be changed. Since the
           Queue size of Runnables is Max and every other task is executing at t’s own time, their
           lower priority tasks will always be executed.
        3. MEDIUM: Can be handled in. Similar way.

#### Algorithm

Job J with Scheduled time T-UTC in future with default priority as LOW.

1. If scheduled time is in past than the current UTC timestamp minus configured delta, don’t accept
   the job and return Bad request.
    1. (ScheduledTimestamp-CurrentTimestamp)>0 -> Accept and Schedule the job
    2. (ScheduledTimestamp-CurrentTimestamp)<0 &&  (ScheduledTimestamp-CurrentTimestamp)+DeltaTS>0->
       Accept and Schedule the job
    3. (ScheduledTimestamp-CurrentTimestamp)<0 &&  (ScheduledTimestamp-CurrentTimestamp)+DeltaTS<0->
       Don’t accept and return Bad Request

##### Sequence Diagram

##### Flow chart

##### UML Diagram

##### High Level Architecture

#### Project Structure

#### Tech Stack

#### Improvements

1. In case of JVM failure, the jobs submitted in thread pool will loose, due to ephemeral nature. To
   handle this, Retry mechanism can be created which will pool Non-Failed or Non-Success tasks and
   execute again. This can be achieved through the, @Scheduled thread running as daemon.
    1. In this case, if retry is implemented - the system needs to ensure that no two or more JVM
       are handling same tasks. This can also be achieved through running a single serverless job
       which pulls jobs based on status and timeframe.

#### Not covered

### Runbook


