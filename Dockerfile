FROM openjdk:8

LABEL application="Chronocorpus"
LABEL description="Client - Workers"
LABEL organiztation="NLP Tools for Polish from G4.19 Group - Wroclaw University of Science and Technology"
LABEL maintainer="tomasz.walkowiak@pwr.edu.pl"

RUN apt-get update
RUN apt-get install -y subversion maven

WORKDIR /home/install
RUN git clone https://github.com/CLARIN-PL/chronocorpus.git

WORKDIR /home/install/chronocorpus/computation-service
RUN mvn clean
RUN mvn install

WORKDIR /home/install/chronocorpus/rest-service
RUN mvn clean install


