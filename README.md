# Todo Service

A microservice responsible for all Todo CRUD operations, secured via JWT token
validation through the **User Service**.

## Database Schema

### Items Table
| Column | Type |
|---|---|
| id | BIGINT (PK) |
| title | VARCHAR |
| user_id | BIGINT  |
| item_details_id | BIGINT (FK) |

### Item_Details Table
| Column | Type |
|---|---|
| id | BIGINT (PK) |
| description | TEXT |
| created_at | DATETIME |
| priority | VARCHAR (LOW / MEDIUM / HIGH) |
| status | VARCHAR (PENDING / IN_PROGRESS / DONE) |

##  API Endpoints

| Method | Endpoint | Body | Header | Description |
|---|---|---|---|---|
| POST | `/add` | ItemRequest (JSON) | Authorization (JWT) | Add new todo item |
| DELETE | `/delete/{id}` | - | Authorization (JWT) | Delete item by ID |
| PUT | `/update/{id}` | ItemRequest (JSON) | Authorization (JWT) | Update item by ID |
| GET | `/search/{id}` | - | Authorization (JWT) | Search item by ID |
| GET | `/search?title=` | - | Authorization (JWT) | Search items by title |

>  Every API call validates the JWT token by calling `/checkToken` on the **User Service** before processing.

## Request Body (ItemRequest)

```json
{
  "title": "Buy groceries",
  "userId": 1,
  "description": "Milk, eggs, bread",
  "priority": "HIGH",
  "status": "PENDING"
}
```

## Response Body (ItemResponse)

```json
{
  "id": 1,
  "title": "Buy groceries",
  "userId": 1,
  "description": "Milk, eggs, bread",
  "createdAt": "2024-01-01T10:00:00",
  "priority": "HIGH",
  "status": "PENDING"
}
```

## Validation Rules

| Field | Rule |
|---|---|
| title | Required, not blank |
| userId | Required, not null |
| description | Required, not blank |
| priority | Must be: `LOW`, `MEDIUM`, or `HIGH` |
| status | Must be: `PENDING`, `IN_PROGRESS`, or `DONE` |

## Security Flow
Client Request

↓

Todo Service receives request

↓

Calls /checkToken on User Service (port 8080)

↓

Token valid?   → process request

Token invalid? → 401 Unauthorized

## Project Structure
src/main/java/org/example/todoservice/

├── entity/

│   ├── Item.java

│   └── ItemDetails.java

├── model/

│   ├── request/ItemRequest.java

│   └── response/ItemResponse.java

├── repository/

│   ├── ItemRepository.java

│   └── ItemDetailsRepository.java

├── service/

│   ├── TodoService.java

│   └── TokenValidationService.java

├── controller/

│   └── TodoController.java

├── exception/

│   ├── GlobalExceptionHandler.java

│   └── ResourceNotFoundException.java

├── SecurityConfig.java

├── SwaggerConfig.java

└── TodoServiceApplication.java

## Configuration

```properties
spring.application.name=TodoService

spring.datasource.url=jdbc:mysql://localhost:3306/todo_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

server.port=8081

# User Service URL (for token validation)
user.service.url=http://localhost:8080

# Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## Testing

Unit tests written with **JUnit 5 + Mockito** covering all controller endpoints.

```bash
mvn test
```

##  API Documentation

Swagger UI available at:
http://localhost:8081/swagger-ui/index.html

##  Run Locally

```bash
# 1. Make sure MySQL is running and todo_db database exists
# 2. Make sure User Service is running on port 8080
mvn spring-boot:run
```
