#!/bin/bash
# 
set -e

NAMESPACE="converter-app"
PROJECT_ROOT=$(pwd)
K8S_DIR=$PROJECT_ROOT/k8s
MONITORING_DIR=$PROJECT_ROOT/monitoring

eval $(minikube docker-env)

docker build -t uploader:local $PROJECT_ROOT/uploader
docker build -t audio-cleaner:local $PROJECT_ROOT/audio-cleaner
docker build -t basic-pitch-worker:local $PROJECT_ROOT/basic-pitch-worker

eval $(minikube docker-env -u)

kubectl delete namespace $NAMESPACE --ignore-not-found
sleep 2
kubectl create namespace $NAMESPACE

# base additional
kubectl apply -f $K8S_DIR/namespace.yml
kubectl apply -f $K8S_DIR/configmaps.yml -n $NAMESPACE
kubectl apply -f $K8S_DIR/volumes.yml -n $NAMESPACE
kubectl apply -f $K8S_DIR/pvcs.yml -n $NAMESPACE
kubectl apply -f $K8S_DIR/services.yml -n $NAMESPACE

# base services
kubectl apply -f $K8S_DIR/audio-cleaner-deployment.yml -n $NAMESPACE
kubectl apply -f $K8S_DIR/basic-pitch-worker-deployment.yml -n $NAMESPACE
kubectl apply -f $K8S_DIR/uploader-deployment.yml -n $NAMESPACE
kubectl apply -f $K8S_DIR/nginx-deployment.yml -n $NAMESPACE

# monitoring
kubectl apply -f $MONITORING_DIR/prometheus-config.yaml
kubectl apply -f $MONITORING_DIR/prometheus-deployment.yaml
kubectl apply -f $MONITORING_DIR/grafana-configmap.yaml
kubectl apply -f $MONITORING_DIR/grafana-deployment.yaml


(minikube tunnel > /dev/null 2>&1 &)

kubectl get pods -n $NAMESPACE -w

nohup kubectl port-forward svc/nginx 8080:80 -n converter-app > /tmp/nginx-port-forward.log 2>&1 &

# kubectl get pods -n converter-app --no-headers | awk '{print $1}' | xargs -I {} kubectl logs -n converter-app --all-containers=true --follow {}
