#!/bin/sh
##
# Script to run discovery cloudlet and data caos microservice
##

echo "Run Discovery Cloudlet ..."
docker run --rm -d --network host --name discovery andersonalmada/discovery-cloudlet

