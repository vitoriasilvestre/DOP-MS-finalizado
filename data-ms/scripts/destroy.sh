#!/bin/sh
##
# Script to remove/undeploy all project resources.
##
echo "\nDestroy CAOS ..."
sleep 3
echo "\nDestroy Discovery Cloudlet ..."
sleep 3
docker stop $(docker ps -q --filter ancestor=andersonalmada/discovery-cloudlet )
echo "\nDestroy Data and Processing Offloading Microservices ..."
sleep 3
kubectl delete -f caos.yaml
echo "\nDestroy Databases ..."
sleep 3
kubectl delete -f databases.yaml 