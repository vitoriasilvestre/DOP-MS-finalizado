#!/bin/sh
##
# Script to stop discovery cloudlet and data caos microservice
##

echo "Stop Discovery Cloudlet ..."
docker stop $(docker ps -q --filter ancestor=andersonalmada/discovery-cloudlet )

