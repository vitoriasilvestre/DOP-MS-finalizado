#!/bin/sh
##
# Script to deploy a Kubernetes project with a StatefulSet running a MongoDB Replica Set, to a local Minikube environment.
##

# Create mongodb service with mongod stateful-set
# TODO: Temporarily added no-valudate due to k8s 1.8 bug: https://github.com/kubernetes/kubernetes/issues/53309
kubectl apply -f mongodb2.yaml --validate=false
sleep 5
echo "Mongodb running ..."


