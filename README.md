# 📦 Todo Microservices Backend

A microservices-based backend built with **Spring Boot 3.x**, featuring JWT authentication,
OTP email verification, and full Todo management.

## 🧩 Services

| Service | Branch | Description | Port |
|---|---|---|---|
| user-service | [`user-service`](../../tree/user-service) | Registration, OTP, JWT Auth | 8081 |
| todo-service | [`todo-service`](../../tree/todo-service) | Todo CRUD operations | 8082 |

## 🛠 Tech Stack
- Java 17 + Spring Boot 3.x
- Spring Security (JWT)
- MySQL + Spring Data JPA
- Swagger / OpenAPI 3
- JUnit 5 + Mockito

## 🚀 Getting Started

```bash
# Clone the repo
git clone https://github.com/OlaGhoneim/todo-microservices-backend.git

# Switch to a service
git checkout user-service   # or todo-service

# Run
mvn spring-boot:run
```

## 📁 Branch Structure
```
master        → project overview (this page)
user-service  → UserService source code
todo-service  → TodoService source code
```
