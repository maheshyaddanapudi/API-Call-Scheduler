FROM maven:3.6.3-jdk-8 as builder

# Setting JAVA_HOME for performing Maven build.
ENV JAVA_HOME /usr/local/openjdk-8
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Creating base directory
RUN mkdir /tmp/APICallsScheduler

COPY pom.xml /tmp/APICallsScheduler/
COPY src /tmp/APICallsScheduler/src

# Building the executable.
RUN cd /tmp/APICallsScheduler \
  && mvn clean install -Dmaven.test.skip=true -q

FROM openjdk:8-jdk as server

MAINTAINER zzzmahesh@gmail.com

ENV DEBIAN_FRONT_END noninteractive

# Declaring internal / defaults variables
ENV ADFS_CLIENT_ID POPULATE_CLIENT_ID
ENV ADFS_RESOURCE POPULATE_ADFS_RESOURCE
ENV ADFS_USER_AUTHORIZATION_URL https://adfs.example.com/adfs/oauth2/authorize
ENV ADFS_ACCESS_TOKEN_URL https://adfs.example.com/adfs/oauth2/token
ENV ADFS_USER_INFO_URL https://adfs.example.com/adfs/oauth2/authorize
ENV OAUTH2_USER_INFO_URL http://localhost:9990/oauth/userinfo
ENV MYSQL_DATABASE quartz
ENV MYSQL_USER quartz
ENV MYSQL_PASSWORD quartz
ENV MYSQL_DATABASE_HOST localhost
ENV MYSQL_DATABASE_PORT 3306
ENV MARIADB4J_DIR /appln/data/mariadb4j
ENV SPRING_PROFILES_ACTIVE basic,mariadb4j
ENV USER_TIMEZONE IST
ENV SCHEDULER_VERSION 2.2.24.RELEASE

# Switching to root working  directory
WORKDIR /

# Starting up as root user
USER root

# Installing all the base necessary packages for execution of embedded MariaDB4j i.e. Open SSL, libaio & libncurses5
RUN apt-get -y -qq update --ignore-missing --fix-missing \
  && apt-get -y -qq install libaio1 libaio-dev libncurses5 openssl sudo

# Creating necessary directory structures to host the platform
RUN mkdir /appln /appln/bin /appln/bin/scheduler /appln/data /appln/data/mariadb4j /appln/scripts /appln/logs /appln/tmp /appln/tmp/APICallsScheduler

# Creating a dedicated user scheduler
RUN groupadd -g 999 scheduler \
  && useradd -u 999 -g scheduler -G sudo --create-home -m -s /bin/bash scheduler \
  && echo -n 'scheduler:scheduler' | chpasswd

# Delegating password less SUDO access to the user scheduler
RUN sed -i.bkp -e \
      's/%sudo\s\+ALL=(ALL\(:ALL\)\?)\s\+ALL/%sudo ALL=NOPASSWD:ALL/g' \
      /etc/sudoers

# Taking the ownership of working directories
RUN chown -R scheduler:scheduler /appln

# Changing to the user scheduler
USER scheduler

# Moving the executable / build to the run location
COPY --from=builder /tmp/APICallsScheduler/target/APICallsScheduler-2.2.4.RELEASE.jar /appln/bin/scheduler/

# Creating the startup script, by passing the env variables to run the jar. Logs are written directly to continer logs.
RUN echo "#!/bin/bash" > /appln/scripts/startup.sh \
  && echo "cd /appln/bin/scheduler" >> /appln/scripts/startup.sh \
  && echo "java \
  -Dspring.profiles.active=\$SPRING_PROFILES_ACTIVE \
  -Duser.timezone=\$USER_TIMEZONE \
  -DADFS_CLIENT_ID=\$ADFS_CLIENT_ID \
  -DADFS_RESOURCE=\$ADFS_RESOURCE \
  -DADFS_USER_AUTHORIZATION_URL=\$ADFS_USER_AUTHORIZATION_URL \
  -DADFS_ACCESS_TOKEN_URL=\$ADFS_ACCESS_TOKEN_URL \
  -DADFS_USER_INFO_URL=\$ADFS_USER_INFO_URL \
  -DOAUTH2_USER_INFO_URL=\$OAUTH2_USER_INFO_URL \
  -DMYSQL_DATABASE=\$MYSQL_DATABASE \
  -DMYSQL_USER=\$MYSQL_USER \
  -DMYSQL_PASSWORD=\$MYSQL_PASSWORD \
  -DMYSQL_DATABASE_HOST=\$MYSQL_DATABASE_HOST \
  -DMYSQL_DATABASE_PORT=\$MYSQL_DATABASE_PORT \
  -DMARIADB4J_DIR=\$MARIADB4J_DIR \
  -jar APICallsScheduler-2.2.4.RELEASE.jar" >> /appln/scripts/startup.sh

# Owning the executable scripts
RUN sudo chown -R scheduler:scheduler /appln/scripts /appln/bin \
    && sudo chmod -R +x /appln/scripts /appln/bin \
    && sudo chmod -R +w /appln/data

# Exposing the necessary ports
EXPOSE 8880

# Enabling the startup
CMD ["/appln/scripts/startup.sh"]
ENTRYPOINT ["/bin/bash"]
