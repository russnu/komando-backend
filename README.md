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

- Make sure MySQL runs on: `localhost:3306`

### 4. Start Kafka

- Navigate to the project root and run: `docker compose up -d`
- Kafka will run on localhost:9092
- `docker-compose.yaml` contains all necessary configuration.


### 5. Firebase
- The Firebase service account file will be shared privately by the project owner.
- Place the file in the project root as: `serviceAccountKey.json`
- Update the path in application.yaml if necessary:
```yaml
firebase:
  service-account-path: serviceAccountKey.json
```

### 6. Run project

- **Build the project** (run in the **parent folder**):
  - `mvn clean install`
- **Run the project** (run inside the `komandosb` folder):
  - `mvn exec:java`

### Optional: Populate Database with Seeder
After running the application once (so the tables are created), you may populate the database with sample data.
- Run either one of the SQL seeder scripts in MySQL Workbench or any SQL client:
  - `seed.sql`
  - `test-seed.sql`