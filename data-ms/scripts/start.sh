#!/bin/sh
##
# Script to add/deploy all project resources.
##
echo "\nStart CAOS ..."
sleep 3
echo "\nStart Discovery Cloudlet ..."
sleep 3
docker run --rm -d --network host --name discovery andersonalmada/discovery-cloudlet
echo "\nStart Databases ..."
sleep 3
kubectl apply -f databases.yaml 
echo "\nStart Data and Processing Offloading Microservices ..."
sleep 3
kubectl apply -f caos.yaml