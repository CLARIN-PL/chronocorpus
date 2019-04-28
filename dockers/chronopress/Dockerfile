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
 
WORKDIR /home/worker
COPY --from=chronocorpus /home/install/chronocorpus/computation-service/target/chronocorpus.jar .

