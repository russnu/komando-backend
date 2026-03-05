# Komando Backend

Spring Boot backend for Komando Mobile.

## Requirements

- Java 17
- MySQL 8
- Apache Kafka
- Firebase project
- Maven

## Setup Steps

1. Clone repo

git clone https://github.com/yourname/komando-backend.git


2. Create database

CREATE DATABASE komandomobiledb;


3. Start MySQL

Make sure MySQL runs on:

localhost:3306


4. Start Kafka

Run Zookeeper:
bin/zookeeper-server-start.sh config/zookeeper.properties

Run Kafka:
bin/kafka-server-start.sh config/server.properties


5. Firebase

Place the Firebase service account file in project root:

serviceAccountKey.json


6. Run project

mvn spring-boot:run