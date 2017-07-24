FROM openjdk:8

RUN wget -q https://services.gradle.org/distributions/gradle-3.4.1-bin.zip && \
    unzip gradle-3.4.1-bin.zip -d /opt && \
    rm gradle-3.4.1-bin.zip && \
    mkdir /home/tkbio

ENV PATH $PATH:/opt/gradle-3.4.1/bin/

COPY . /home/tkbio/

RUN cd home/tkbio && \
    gradle clean -x test && \
    gradle build -x test

WORKDIR /home/tkbio/web

ENTRYPOINT ["java", "-jar", "build/libs/tkbio-web-4.0.20.jar"]
