# Supplier Management Module

A Spring Boot REST API for managing suppliers, with PostgreSQL persistence and Kafka event publishing.

## Tech Stack

| Layer      | Technology                                          |
|------------|-----------------------------------------------------|
| Framework  | Spring Boot 4.x                                     |
| Database   | PostgreSQL 16                                       |
| Messaging  | Apache Kafka 3.9 (KRaft — no Zookeeper)             |
| ORM        | JPA / Hibernate                                     |
| Security   | Spring Security (stateless, open for API endpoints) |
| API Docs   | SpringDoc OpenAPI 3 / Swagger UI                    |
| Build      | Maven (via `mvnw.cmd` wrapper)                      |

---

## Running with Docker (Recommended)

### Prerequisites

- Docker Desktop installed and running

### Start all services

```bash
docker-compose up --build
```

This starts three containers:

| Container          | Service    | Host Port |
|--------------------|------------|-----------|
| `supplier-postgres`| PostgreSQL | `5433`    |
| `supplier-kafka`   | Kafka      | `9092`    |
| `supplier-app`     | Spring Boot| `8081`    |

> The Spring Boot app waits for both PostgreSQL and Kafka health checks to pass before starting.

### Stop all services

```bash
docker-compose down
```

To also remove persisted volumes:

```bash
docker-compose down -v
```

---

## Running Locally (without Docker)

### Prerequisites

- Java 17+
- PostgreSQL running on `localhost:5432`
- Kafka running on `localhost:9092` *(optional — app starts without it)*

### Create the database

```sql
CREATE DATABASE supplier_db;
```

### Configure credentials

Update `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    password: YOUR_POSTGRES_PASSWORD
```

### Start the application

**Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

### Build a fat JAR

```powershell
.\mvnw.cmd package -DskipTests
java -jar target/*.jar
```

---

## API Endpoints

Base URL: `http://localhost:8081/api/v1/suppliers`

| Method   | Route            | Status | Description                  |
|----------|------------------|--------|------------------------------|
| `POST`   | `/add`           | `201`  | Create a new supplier        |
| `PUT`    | `/update/{id}`   | `200`  | Update an existing supplier  |
| `GET`    | `/{id}`          | `200`  | Retrieve a supplier by ID    |
| `DELETE` | `/delete/{id}`   | `204`  | Delete a supplier by ID      |

### Swagger UI

Open **http://localhost:8081/swagger-ui.html** in your browser for interactive API documentation.

---

### Example — Create a supplier

```bash
curl -X POST http://localhost:8081/api/v1/suppliers/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Acme Corp",
    "email": "contact@acme.com",
    "phone": "+1-800-555-0100",
    "address": "123 Main St, Springfield",
    "contactPerson": "Jane Doe"
  }'
```

### Example — Update a supplier

```bash
curl -X PUT http://localhost:8081/api/v1/suppliers/update/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Acme Corp Updated",
    "email": "newemail@acme.com",
    "phone": "+1-800-555-0199",
    "address": "456 New Ave, Springfield",
    "contactPerson": "John Smith"
  }'
```

### Example — Get a supplier

```bash
curl http://localhost:8081/api/v1/suppliers/1
```

### Example — Delete a supplier

```bash
curl -X DELETE http://localhost:8081/api/v1/suppliers/delete/1
```

---

## Response Envelope

All endpoints (except `DELETE`) return a standard envelope:

```json
{
  "success": true,
  "message": "Supplier created successfully",
  "data": { ... },
  "timestamp": "2026-05-05T10:30:00"
}
```

On error:

```json
{
  "success": false,
  "message": "Supplier not found with id: 99",
  "timestamp": "2026-05-05T10:30:00"
}
```

---

## Kafka Events

Every operation publishes an event to the `supplier-events` topic (3 partitions, key = supplier ID).

| Operation           | Event Type           |
|---------------------|----------------------|
| `POST /add`         | `SUPPLIER_CREATED`   |
| `PUT /update/{id}`  | `SUPPLIER_UPDATED`   |
| `GET /{id}`         | `SUPPLIER_RETRIEVED` |
| `DELETE /delete/{id}` | `SUPPLIER_DELETED` |

### Event payload structure

```json
{
  "eventType": "SUPPLIER_CREATED",
  "supplierId": 1,
  "supplierName": "Acme Corp",
  "timestamp": "2026-05-05T10:30:00",
  "data": { ... }
}
```

### Consume events from host (while Docker is running)

```bash
docker exec supplier-kafka \
  /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic supplier-events \
  --from-beginning
```

---

## Project Structure

```
src/main/java/Supplier/Mgt/Supplier/Mgt/
├── annotation/
│   ├── ApiConflictError.java         # @ApiResponse 409 meta-annotation
│   ├── ApiNotFoundError.java         # @ApiResponse 404 meta-annotation
│   └── ApiValidationError.java       # @ApiResponse 400 meta-annotation
├── config/
│   ├── KafkaConfig.java              # supplier-events topic bean
│   └── SecurityConfig.java           # Spring Security (stateless, open)
├── controller/
│   └── SupplierController.java       # REST endpoints
├── dto/
│   ├── ApiResult.java                # Standard response envelope
│   ├── SupplierRequest.java          # Request payload (create / update)
│   └── SupplierResponse.java         # Response payload returned by the API
├── entity/
│   ├── Supplier.java                 # JPA entity
│   └── SupplierStatus.java           # ACTIVE | INACTIVE | SUSPENDED
├── event/
│   └── SupplierEvent.java            # Kafka event model
├── exception/
│   ├── DuplicateSupplierException.java
│   ├── GlobalExceptionHandler.java
│   └── SupplierNotFoundException.java
├── kafka/
│   └── SupplierKafkaProducer.java    # Async Kafka publisher
├── repository/
│   └── SupplierRepository.java
├── service/
│   └── SupplierService.java          # Service interface
└── serviceImpl/
    └── SupplierServiceImpl.java      # @Service implementation
```

---

## Environment Variables (Docker)

The `docker-compose.yml` overrides `application.yaml` at runtime via these environment variables:

| Variable                        | Value                                  | Description                    |
|---------------------------------|----------------------------------------|--------------------------------|
| `SPRING_DATASOURCE_URL`         | `jdbc:postgresql://postgres:5432/...`  | Internal Docker DB URL         |
| `SPRING_DATASOURCE_USERNAME`    | `postgres`                             | DB username                    |
| `SPRING_DATASOURCE_PASSWORD`    | `postgres`                             | DB password                    |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS`| `kafka:29092`                          | Internal Kafka listener        |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update`                               | Auto-create/update DB schema   |
