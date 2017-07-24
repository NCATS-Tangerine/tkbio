FROM ubuntu:16.04

ENV PATH $PATH:/home/gradle-3.4.1/bin/

RUN apt-get update
RUN apt-get -y install openjdk-8-jdk wget unzip git

RUN wget https://services.gradle.org/distributions/gradle-3.4.1-bin.zip /home/gradle.zip && \
    unzip /home/gradle-3.4.1-bin.zip -d /home/ && \
    mkdir /home/ndex

COPY . /home/ndex/

RUN cd home/ndex && \
    gradle clean -x test && \
    gradle build -x test && \
    cd web

ENTRYPOINT ["java", "-jar", "home/ndex/server/build/libs/knowledge-beacon-server-1.0.12.jar"]
