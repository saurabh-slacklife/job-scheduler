#! /usr/bin/env sh


mvn -f scheduler-models/pom.xml clean compile install package -Dmaven.test.skip=true

mvn -f producer-api/pom.xml clean compile install spring-boot:repackage -Dmaven.test.skip=true

mvn -f scheduler/pom.xml clean compile install spring-boot:repackage -Dmaven.test.skip=true

mvn -f scheduler-dashboard-api/pom.xml clean compile install spring-boot:repackage -Dmaven.test.skip=true