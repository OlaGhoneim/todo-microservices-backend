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

> ⚠️ Every API call validates the JWT token by calling `/checkToken` on the **User Service** first.

## 🔐 Security Flow
