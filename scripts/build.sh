#! /usr/bin/env sh


mvn -f scheduler-models/pom.xml clean compile install

mvn -f producer-api/pom.xml clean compile package spring-boot:repackage

mvn -f scheduler/pom.xml clean compile package spring-boot:repackage

mvn -f scheduler-api/pom.xml clean compile package spring-boot:repackage