Now I have your full user-service code. Here is the complete accurate README:

```markdown
# User Service

A microservice responsible for user authentication, authorization, and user
management using JWT and OTP-based email verification.

## Database Schema

### Users Table
| Column | Type |
|---|---|
| id | BIGINT (PK) |
| email | VARCHAR |
| password | VARCHAR (BCrypt hashed) |
| enabled | BOOLEAN |
| role | VARCHAR (ENUM: USER / ADMIN) |

### OTP Table
| Column | Type |
|---|---|
| id | BIGINT (PK) |
| otp | VARCHAR |
| expiration_time | DATETIME |
| user_id | BIGINT (FK → users.id) |

### JWT_Token Table
| Column | Type |
|---|---|
| id | BIGINT (PK) |
| token | TEXT |
| token_type | VARCHAR (ENUM: BEARER) |
| revoked | BOOLEAN |
| expired | BOOLEAN |
| user_id | BIGINT (FK → users.id) |

## Auth API Endpoints

| Method | Endpoint | Body | Header | Description |
|---|---|---|---|---|
| POST | `/api/auth/register` | `{ email, password }` | - | Register user, set enabled=false, send OTP to email |
| POST | `/api/auth/login` | `{ email, password }` | - | Authenticate and return JWT token |
| GET | `/api/auth/activate` | - `?email=` `?otp=` | - | Verify OTP and activate account |
| POST | `/api/auth/checkToken` | `?token=` | - | Validate JWT token (used by Todo Service) |
| POST | `/api/auth/forgetPassword` | - | Authorization (JWT) | Extract email from token, generate and send OTP |
| POST | `/api/auth/changePassword` | `{ newPassword, otp }` | Authorization (JWT) | Change password after OTP verification |
| GET | `/api/auth/regenerateOtp` | `?email=` | - | Generate and resend new OTP to email |

## Request / Response Examples

### Register
```json
// Request
{ "email": "user@example.com", "password": "123456" }

// Response
"Registered! Check your email for OTP."
```

### Login
```json
// Request
{ "email": "user@example.com", "password": "123456" }

// Response
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "user@example.com"
}
```

### Change Password
```json
// Request Body
{ "newPassword": "newpass123", "otp": "482910" }
// Header: Authorization: Bearer <token>
```

## Auth Flow

```
/register → user saved (enabled=false) → OTP sent to email
      ↓
/activate?email=&otp= → enabled=true
      ↓
/login → JWT token returned
      ↓
Use token in requests → Todo Service calls /checkToken to validate
```

## Password Reset Flow

```
/forgetPassword (Bearer token) → OTP generated → sent to email
      ↓
/changePassword (Bearer token + { newPassword, otp }) → password updated
      ↓
(if OTP expired) → /regenerateOtp?email= → new OTP sent
```

## Security

- Passwords hashed with **BCrypt**
- JWT tokens signed with **HMAC-SHA** key
- Token expiry: **30 minutes**
- Public endpoints (no auth needed):
  - `/api/auth/register`
  - `/api/auth/login`
  - `/api/auth/activate`
  - `/api/auth/checkToken`
  - `/api/auth/regenerateOtp`
  - `/swagger-ui/**`
- All other endpoints require a valid JWT in the `Authorization` header

## Project Structure

```
src/main/java/org/example/userservice/
├── entity/
│   ├── User.java
│   ├── Otp.java
│   ├── JwtToken.java
│   ├── Role.java
│   └── TokenType.java
├── model/
│   ├── request/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── ChangePasswordRequest.java
│   │   └── AuthenticationResponse.java
│   └── response/
│       └── LoginRes.java
├── repository/
│   ├── UserRepository.java
│   ├── OtpRepository.java
│   └── TokenRepository.java
├── service/
│   ├── AuthService.java
│   ├── JwtService.java
│   ├── OtpService.java
│   ├── EmailService.java
│   └── UserDetailsServiceImpl.java
├── controllers/
│   ├── AuthController.java
│   └── HomeController.java
├── auth/
│   ├── JwtAuthorizationFilter.java
│   └── SecurityConfig.java
└── UserServiceApplication.java
```

## Configuration

```properties
spring.application.name=UserService

spring.datasource.url=jdbc:mysql://localhost:3306/user_service_db
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

server.port=8080

# Mail (SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your@email.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
```

## Testing

Unit tests written with **JUnit 5 + Mockito** covering all controller endpoints.

```bash
mvn test
```

## API Documentation

Swagger UI available at:

```
http://localhost:8080/swagger-ui/index.html
```

## Run Locally

```bash
# 1. Make sure MySQL is running
# 2. Create database: user_service_db
# 3. Configure your mail credentials in application.properties
mvn spring-boot:run
```
```

---

This README now reflects your **exact code** — correct port `8080`, real endpoints with params, actual DB schema with `role` and `revoked/expired` columns, real request/response bodies, and accurate project structure. Paste this into your `user-service` branch.
