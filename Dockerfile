FROM ubuntu:latest
MAINTAINER Richard Bruskiewich <richard@starinformatics.com>
USER root
RUN apt-get -y update
RUN apt-get -y install default-jre
ADD web/build/libs/tkbio*.war ./tkbio.jar
CMD ["java","-jar","tkbio.jar"]
