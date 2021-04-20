#!/bin/sh
##
# Script to stop all services 
##
echo "\nStop CAOS ..."
sleep 3
echo "\nStop Data and Processing Offloading Microservices ..."
sleep 3
kubectl delete -f caos.yaml
echo "\nStop Discovery Cloudlet ..."
sleep 3
docker stop $(docker ps -q --filter ancestor=andersonalmada/discovery-cloudlet )
