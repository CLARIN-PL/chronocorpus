##############################################
#  NLP Tools for Polish from G4.19 Group
#  Wroclaw University of Science and Technology
#  
#  Client - Workers
#           chrono...
#
#  Contact: Tomasz.Walkowiak@pwr.edu.pl
#
###############################################

FROM chronocorpus as chronocorpus

FROM openjdk:8
 
WORKDIR /home/worker
COPY --from=chronocorpus /home/install/chronocorpus/rest-service/target/RESTchronocorpus.jar .

