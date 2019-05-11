#!/usr/bin/env bash
kubectl delete -f config.yaml
kubectl delete -f services.yaml
kubectl delete -f ingress.yaml
kubectl delete -f service-ingress.yaml