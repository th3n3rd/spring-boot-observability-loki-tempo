# Get started with distributed tracing with Spring boot and Tempo 

This project is for demo purposes only, and is used to demonstrate a very simple strategy to instrument a spring boot
web application for tracing and log aggregation.

# Getting started

The following is required to run the application:

- Java Development Kit (JDK) 11
- Kotlin 1.4+
- Docker
- Kubernetes
- Helm

# Deployment

## Deploy the database locally

To run an instance of Mongo DB (if not already running), issue the following command in the terminal:

```shell
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install traceable-db bitnami/mongodb --set auth.enabled=false,persistence.enabled=false,architecture=standalone
```

## Deploy the application locally

To run an instance of the application, issue the following command in the terminal:

```shell
./gradlew bootBuildImage
kubectl apply -f deployment/k8s
```

To access the deployed application we can simply use port-forwarding by issuing the following command in the terminal:

```shell
kubectl port-forward svc/traceable 8080:80
```
