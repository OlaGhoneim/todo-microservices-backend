# Todo Service

A microservice responsible for all Todo CRUD operations, secured via JWT token
validation through the **User Service**.

## Database Schema

### Items Table
| Column | Type |
|---|---|
| id | BIGINT (PK) |
| title | VARCHAR |
| user_id | BIGINT (FK) |
| item_details_id | BIGINT (FK) |

### Item_Details Table
| Column | Type |
|---|---|
| id | BIGINT (PK) |
| description | TEXT |
| created_at | DATETIME |
| priority | ENUM |
| status | ENUM |

## API Endpoints

| Method | Endpoint | Body | Header | Description |
|---|---|---|---|---|
| POST | `/add` | Item details (JSON) | JWT Token | Add new todo item |
| DELETE | `/delete/{id}` | - | JWT Token | Delete item by ID |
| PUT | `/update/{id}` | Item details (JSON) | JWT Token | Update item by ID |
| GET | `/search/{id}` | - | JWT Token | Search item by ID |

>  Every API call validates the JWT token by calling `/checkToken` on the **User Service** first.

## Security Flow
Client Request → Todo Service → calls /checkToken on User Service

↓

Token valid? → Proceed

Token invalid? → 401 Unauthorized

## Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tododb
spring.datasource.username=root
spring.datasource.password=yourpassword
server.port=8082

# User Service URL (for token validation)
user.service.url=http://localhost:8081
```

## Testing

Unit tests written with **JUnit 5 + Mockito** covering all controller endpoints.

```bash
mvn test
```

## API Documentation

Swagger UI available at:
http://localhost:8082/swagger-ui/index.html

##  Run Locally

```bash
# Make sure User Service is running first on port 8081
mvn spring-boot:run
```
