#!/usr/bin/env bash
kubectl create -f config.yaml
kubectl create -f services.yaml
kubectl create -f ingress.yaml
kubectl create -f service-ingress.yaml