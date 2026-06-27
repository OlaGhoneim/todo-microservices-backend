# User Service

Handles user registration, OTP email verification, and JWT-based authentication.

##  Endpoints
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/verify-otp | Verify OTP code |
| POST | /api/auth/login | Login and get JWT |

##  Configuration
Create `application.properties` based on these required variables:
