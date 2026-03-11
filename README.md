# 🚀 Komando Mobile - Spring Boot Backend

## 🛠 Prerequisites

- Java 17+
- Maven 3+
- Docker & Docker Compose
- MySQL 8+
- Firebase project with service account (will be shared privately by the project owner)
---
## 📦 Setup

### 1. Clone the repository

```
git clone https://github.com/russnu/komando-backend.git
cd komando-backend
```



### 2. Database
- Make sure MySQL is running on localhost:3306.
- Update credentials in `src/main/resources/application.yaml` if needed.
- DB will be auto-created on startup.
- Configure `username`  and `password` via environment variables or type them directly in `application.yaml`.
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/komandomobiledb?allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true&useSSL=false
    # Option 1: Use environment variables
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    # Option 2: Or type them directly
    # username: root
    # password: your_password
```

### 3. Start MySQL

- ***If running the app locally***: ensure MySQL is available on `localhost:3306`
- ***If using Docker Compose***: `docker compose up --build`
  - This will build all images and start MySQL, Kafka, and backend containers.

### 4. Start Kafka
#### ***Local backend***
**Kafka runs in Docker for both local and Dockerized backend setups.**
- By default, the configuration in `docker-compose.yaml` assumes the backend runs locally:
```yaml
KAFKA_CONTROLLER_QUORUM_VOTERS: "1@localhost:9093"
KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
```
- ***Navigate to the project root and run:*** `docker compose up -d kafka`

#### ***Dockerized backend***

- If you run the app in Docker, update the Kafka environment in Compose:
```yaml
KAFKA_CONTROLLER_QUORUM_VOTERS: "1@kafka:9093"
KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
```
- ***Run full Docker stack:*** `docker compose up`


### 5. Firebase
- The Firebase service account file will be shared privately by the project owner.
- Place the file in the project root or any directory in your machine as: `serviceAccountKey.json`
- Update the path in application.yaml if necessary:
```yaml
firebase:
  service-account-path: your/path/to/serviceAccountKey.json
```

### 6. Run the project

1. **Build the project** (run in the **parent directory**):
   - `mvn clean install`
2. **Run Core Backend (API)** (run inside the `komandosb` directory):
   - `mvn exec:java`
   - This starts: `http://localhost:8080`
   - ***Responsibilities***:
     - REST API 
     - database operations 
     - publishing Kafka events

3. **Run Notification Service** (run inside the `komando-notification-service` directory):
   - `mvn exec:java`
   - This starts: `http://localhost:8081`
   - ***Responsibilities***:
     - consume Kafka events
     - send push notifications via Firebase

#### Responsibilities:
- REST API
- database operations
- publishing Kafka events

### Optional: Populate Database with Seeder
After running the application once (so the tables are created), you may populate the database with sample data.
- Run either one of the SQL seeder scripts in MySQL Workbench or any SQL client:
  - `seed.sql`
  - `test-seed.sql`

Using Docker exec (if MySQL runs in Docker)
```bash
# on CMD, navigate to parent folder
docker exec -it komando-mysql mysql -u root -p komandomobiledb < seed.sql  # or test-seed.sql
```