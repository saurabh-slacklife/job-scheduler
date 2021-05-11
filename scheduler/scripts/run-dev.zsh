#! /usr/bin/env zsh

source ./environ/dev.env

mvn spring-boot:run -Dspring-boot.run.jvmArguments=" -Dspring.profiles.active=dev -Djdk.tls.client.protocols=TLSv1.2"
