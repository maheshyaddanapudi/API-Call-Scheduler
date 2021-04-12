# API Calls Quartz Scheduler
## With Embedded persistent Database(MariaDB) / External Database (MariaDB / MySQL)
## With Embedded OAuth2 / External OAuth2 / ADFS Support
## With containerization (Docker / Docker Compose)
## With Pivotal Cloud Foundry (PCF) deployment & Database as a service (MySQL) support


[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=maheshyaddanapudi_APICallsScheduler)

##### Note

      • Only MySQL is supported as external Database
      
      				Or
      
      • Embedded MariaDB4j is supported, which is persistent and data is not lost, thus eliminating the need for external


## Overview

The idea is to build a single production grade Spring Boot Jar with the following

      • API Calls Scheduling - Using Quartz
      
      • Optional Embedded Persistent MariaDB4j

## Motivation

To avoid the pain points of

      • Perform API calls at partical schedules of the day manually
      
      • Housing external database engine for API Calls Quartz Scheduler persistence unit.

## Tech / Framework used

      --> Docker Image to host the Jar. 
	  			
      --> Spring Boot 
            
            • MariaDB4j
            • Liquibase
            • Flyway
            • Spring Security
            • Spring Boot Quartz Starter

## Code coverage

CodeQL: ![CodeQL](https://github.com/maheshyaddanapudi/APICallsScheduler/workflows/CodeQL/badge.svg?branch=main)

## Code quality

SonarQube: [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=maheshyaddanapudi_APICallsScheduler&metric=alert_status)](https://sonarcloud.io/dashboard?id=maheshyaddanapudi_APICallsScheduler)

## Build using maven

		cd <to project root folder>
		mvn clean install
		
	The maven build should place the APICallsScheduler-2.2.4.RELEASE.jar inside the target folder.

## Build CI (Continuous Integration)

| CI Provider | Status          |
| ------- | ------------------ |
| Circle CI   | [![maheshyaddanapudi](https://circleci.com/gh/maheshyaddanapudi/APICallsScheduler.svg?style=shield)](https://circleci.com/gh/maheshyaddanapudi/APICallsScheduler) |
| Java CI   | ![Java CI with Maven](https://github.com/maheshyaddanapudi/APICallsScheduler/workflows/Java%20CI%20with%20Maven%20Main/badge.svg?branch=main) |
| Travis CI   | [![Build Status](https://travis-ci.com/maheshyaddanapudi/APICallsScheduler.svg?branch=main)](https://travis-ci.com/maheshyaddanapudi/APICallsScheduler) |

## Containerization CI (Continuous Integration)

| CI Provider | Status          |
| ------- | ------------------ |
| Docker   | ![Docker](https://github.com/maheshyaddanapudi/APICallsScheduler/workflows/Docker/badge.svg?branch=main) |
| Docker Image CI   | ![Docker Image CI](https://github.com/maheshyaddanapudi/APICallsScheduler/workflows/Docker%20Image%20CI/badge.svg?branch=main) |

Docker Image published to <a href="https://hub.docker.com/repository/docker/zzzmahesh/api-calls-scheduler" target="_blank">DockerHub here</a>

Image is equipped with basic tools like vim, curl, wget, net-tools(telnet), iputils-ping

To pull the image :

	docker pull zzzmahesh/APICallsScheduler

## Run API Calls Scheduler : Java

		cd <to project root folder>/target
		
	Below command will start the API Calls Scheduler Wrapper with Embedded MariaDB4J as Database and Embedded OAuth2 security as well as persistent Embedded Elasticsearch
		java -jar APICallsScheduler-2.2.4.RELEASE.jar

    The profiles included by default are
        - basic
        - mariadb4j

### Available Profiles

    1) basic
        This profile holds the basic startup configuration needed for the Spring Boot Wrapper.
        
    2) mariadb4j
        This profile configures and startsup Embedded MariaDB4J and the database details are passed on to Integrated ansible Server.
        By default, the database is non-persistent. Set MARIADB4J_DIR parameter for enabling a persistent database. More details are discussed in configurations section.
        Configurations available are as below. Shown are default values.
            MYSQL_DATABASE: quartz
            MYSQL_USER: quartz
            MYSQL_PASSWORD: quartz
            
            # This directory has to be explicitly set to let Embedded MariaDB know the master persistence location.
            # Use embedded/persistence/mariadb4j to have a common persistence location for all embedded components.
            # Default is NONE
            MARIADB4J_DIR: NONE

    3) mysql (cannot be selected alone, will need basic profile as well)
        This profile configures the external MySQL database details passed on to Integrated quartz Server.
        Configurations available are as below. Shown are default values.
            MYSQL_DATABASE_HOST: localhost
            MYSQL_DATABASE_PORT: 3306
            MYSQL_DATABASE: quartz
            MYSQL_USER: quartz
            MYSQL_PASSWORD: quartz
            
    Default Startup will include - basic, mariadb4j (without-persistent-storage).

    For more detailed properties, refer to application-{profile}.yml file as per the required profile properties.

## Application URLs

		http://localhost:8880/ - To access the Swagger pertaining to APIs for ansible-docs, as redirection is taken care to OpenAPI UI.

## Run API Calls Scheduler : Docker

To run the container :

    docker run --name APICallsScheduler -p 8880:8880 -d zzzmahesh/api-calls-scheduler:latest

Few other examples / ways / configurations to run the container as:

    1) Running with external MySQL - The database is decoupled. The Ansible Docs data is persistent as it uses MySQL Database.

        docker run --name APICallsScheduler -p 8880:8880 \
            -e SPRING_PROFILES_ACTIVE=basic,mysql \
            -e MYSQL_DATABASE_HOST=172.x.x.x \
            -e MYSQL_DATABASE_PORT=3306 \
            -e MYSQL_USER=quartz \
            -e MYSQL_PASSWORD=quartz \
            -d zzzmahesh/api-calls-scheduler:latest

    Similarly any combination of profile and configurations can be used.

#### All the below mentioned configurables / properties (under Available Profiles section) can be passed as Docker Container environment variables and will be set accordingly.

Available configurables - shown below with default values.

    MYSQL_DATABASE quartz
    MYSQL_USER quartz
    MYSQL_PASSWORD quartz
    MYSQL_DATABASE_HOST localhost
    MYSQL_DATABASE_PORT 3306
    MARIADB4J_DIR /appln/data/mariadb4j
    SPRING_PROFILES_ACTIVE basic,mariadb4j

Also the below mentioned paths / volumes can be mounted to docker container for persistence, in case of embedded profiles (mariadb4j)

    MARIADB4J_DIR /appln/data/mariadb4j

## Run API Calls Scheduler : Pivotal Cloud Foundry

    1) Deploy basic profile

        cf push -f manifests/manifest.yml

    2) Deploy cf-mysql profile : For PCF MySQL as Datasource i.e. binding Database service "cf-mysql-service"

        cf push -f manifests/manifest-cf-mysql.yml
