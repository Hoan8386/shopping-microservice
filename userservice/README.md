# User Service — Tài liệu kỹ thuật

> **Microservice**: `userservice`  
> **Port**: `9005` (yml) / `9002` (properties)  
> **Spring Boot**: `4.0.1` | **Java**: `17` | **Spring Cloud**: `2025.1.0`

---

## 1. Tổng quan

`userservice` là microservice quản lý người dùng trong hệ thống shopping. Service này chịu trách nhiệm:

- **Đăng ký người dùng** — tạo user đồng thời trong Keycloak và database local
- **Đăng nhập** — ủy thác xác thực cho Keycloak, trả về JWT tokens
- **CRUD user** — quản lý thông tin người dùng trong database local

### Kiến trúc tổng quan

```
Client
  │
  ▼
API Gateway
  │
  ├─► POST /api/v1/public/login    ──► Keycloak (OAuth2 Password Grant)
  │
  └─► /api/v1/users/**  [🔒 JWT]  ──► UserController
                                          │
                                          ▼
                                      UserServiceImpl
                                          │
                                  ┌───────┴───────┐
                                  ▼               ▼
                             UserRepository   IdentityClient (Feign)
                                  │               │
                             MySQL / H2       Keycloak Admin API
```

---

## 2. Tech Stack & Dependencies

| Dependency | Phiên bản | Mục đích |
|---|---|---|
| Spring Boot Starter Web MVC | 4.0.1 | REST API |
| Spring Boot Starter Data JPA | 4.0.1 | Database access |
| Spring Boot Starter Validation | 4.0.1 | Bean validation |
| Spring Cloud Netflix Eureka Client | 2025.1.0 | Service discovery |
| Spring Cloud OpenFeign | 2025.1.0 | HTTP client gọi Keycloak |
| Axon Framework | 4.9.3 | Event sourcing / CQRS |
| H2 Database | runtime | In-memory DB (dev/test) |
| MySQL | (external) | Production database |
| Lombok | optional | Boilerplate reduction |
| `commonservice` | 0.0.1-SNAPSHOT | Shared module nội bộ |

---

## 3. Cấu hình

### `application.yml` (chính)

```yaml
server:
  port: 9005

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

spring:
  application:
    name: userservice
  datasource:
    url: jdbc:mysql://localhost:3306/ltfullstack?useSSL=false&serverTimezone=UTC
    username: root
    password: 123456
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8083/auth/realms/baeldung
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

idp:
  url: http://localhost:8180         # Keycloak base URL
  client-id: microservices_app       # OAuth2 Client ID
  client-secret: LDAcE733NH68B4gdG0TZwbTQvEcbsVKU
  realm: microservices
```

> **⚠️ Lưu ý:** File `application.properties` có cấu hình override dùng H2 in-memory database (`jdbc:h2:mem:userservice`) với port `9002`. Cần kiểm tra lại để tránh conflict với `application.yml`.

---

## 4. API Endpoints

### 4.1 Public Endpoints — Không yêu cầu authentication

Base path: `/api/v1/public`

#### `POST /api/v1/public/login`

Đăng nhập và lấy JWT tokens thông qua Keycloak OAuth2 Password Grant.

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "password123"
}
```

**Response `200 OK`:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": "300",
  "refresh_expires_in": "1800",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "id_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "scope": "openid profile email"
}
```

**Lỗi có thể xảy ra:**
- `401 Unauthorized` — username hoặc password không đúng (thrown từ Keycloak qua Feign)

---

### 4.2 Protected Endpoints — Yêu cầu Bearer Token

Base path: `/api/v1/users`  
Header bắt buộc: `Authorization: Bearer {access_token}`

#### `POST /api/v1/users` — Tạo user mới

**Request Body:**
```json
{
  "email": "johndoe@example.com",
  "username": "johndoe",
  "firstName": "John",
  "lastName": "Doe",
  "dob": "1990-05-15",
  "name": "John Doe",
  "password": "SecureP@ss123"
}
```

**Response `200 OK`** — `UserResponseDTO` (không chứa password):
```json
{
  "id": 1,
  "userId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "email": "johndoe@example.com",
  "username": "johndoe",
  "firstName": "John",
  "lastName": "Doe",
  "dob": "1990-05-15",
  "name": "John Doe"
}
```

> **ℹ️ Ghi chú:** `userId` là UUID được tạo bởi Keycloak, dùng để mapping giữa local DB và Keycloak.  
> Password được hash và lưu trong Keycloak — **KHÔNG** lưu trong database local.

---

#### `GET /api/v1/users` — Lấy danh sách tất cả users

**Response `200 OK`:** `List<UserResponseDTO>`

---

#### `GET /api/v1/users/{id}` — Lấy user theo ID

| Parameter | Kiểu | Mô tả |
|---|---|---|
| `id` | `Long` | ID trong database local (auto-increment) |

**Response `200 OK`:** `UserResponseDTO`  
**Lỗi:** `500` nếu không tìm thấy user (`RuntimeException`)

---

#### `PUT /api/v1/users/{id}` — Cập nhật user

> **⚠️ Quan trọng:** Chỉ cập nhật trong database local. Thông tin trong Keycloak **KHÔNG** được cập nhật.

**Request Body:** `CreateUserRequestDTO` (field `password` bị bỏ qua)  
**Response `200 OK`:** `UserResponseDTO`

---

#### `DELETE /api/v1/users/{id}` — Xóa user

> **⚠️ Cảnh báo:** Chỉ xóa khỏi database local. User vẫn tồn tại trong Keycloak và **CÓ THỂ** tiếp tục đăng nhập.

**Response `204 No Content`**

---

## 5. Cấu trúc Package

```
com.shoping.userservice
├── UserserviceApplication.java          # Entry point
│
├── controller/
│   ├── PublicController.java            # /api/v1/public/* (no auth)
│   └── UserController.java             # /api/v1/users/* (requires JWT)
│
├── service/
│   ├── IUserService.java               # Service interface
│   └── impl/
│       └── UserServiceImpl.java        # Business logic implementation
│
├── repository/
│   ├── UserRepository.java             # JPA repository (local DB)
│   └── IdentityClient.java             # Feign client → Keycloak
│
├── entity/
│   └── User.java                       # JPA entity (bảng `users`)
│
└── dto/
    ├── CreateUserRequestDTO.java        # Request: tạo/cập nhật user
    ├── LoginRequestDto.java             # Request: đăng nhập
    ├── UserResponseDTO.java             # Response: thông tin user
    └── identity/
        ├── Credential.java              # Keycloak credential (password)
        ├── TokenExchangeParam.java      # Params: client credentials grant
        ├── TokenExchangeResponse.java   # Response: JWT tokens từ Keycloak
        ├── UserCreationParam.java       # Params: tạo user trong Keycloak
        ├── UserTokenExchangeParam.java  # Params: password grant (login)
        └── KeyCloakError.java           # Error response từ Keycloak
```

---

## 6. Luồng xử lý chi tiết

### 6.1 Đăng ký user (`createUser`)

```
Client ──POST /api/v1/users──► UserController
                                    │
                                    ▼
                               UserServiceImpl.createUser()
                                    │
                  ┌─────────────────┼─────────────────────┐
                  │ Bước 1          │ Bước 2               │ Bước 3
                  ▼                 ▼                      ▼
          exchangeClientToken   createUser(Keycloak)   Extract userId
          (Client Credentials)  POST /admin/realms/    từ Location header
          → access_token        .../users              (UUID)
                                                           │
                  ┌────────────────────────────────────────┘
                  │ Bước 4
                  ▼
          userRepository.save(user)   → lưu vào DB local
                  │
                  ▼
          return UserResponseDTO
```

### 6.2 Đăng nhập (`login`)

```
Client ──POST /api/v1/public/login──► PublicController
                                            │
                                            ▼
                                    UserServiceImpl.login()
                                            │
                                            ▼
                                  IdentityClient.exchangeUserToken()
                                  POST /realms/microservices/protocol
                                       /openid-connect/token
                                  (OAuth2 Password Grant)
                                            │
                                            ▼
                                  return TokenExchangeResponse
                                  (access_token, refresh_token, id_token)
```

---

## 7. Tích hợp Keycloak

Service giao tiếp với Keycloak thông qua `IdentityClient` (Spring Cloud OpenFeign).

| Method | Keycloak Endpoint | Grant Type | Mục đích |
|---|---|---|---|
| `exchangeClientToken()` | `POST /realms/microservices/protocol/openid-connect/token` | `client_credentials` | Lấy admin token để gọi Admin API |
| `createUser()` | `POST /admin/realms/microservices/users` | — (dùng Bearer admin token) | Tạo user trong Keycloak |
| `exchangeUserToken()` | `POST /realms/microservices/protocol/openid-connect/token` | `password` | Đăng nhập user |

**Cấu hình Keycloak cần thiết:**
- Realm: `microservices`
- Client: `microservices_app`
- Client secret: được cấu hình qua `idp.client-secret`
- Client phải có role `manage-users` trong realm `microservices`

---

## 8. Data Model

### Bảng `users` (database local)

| Column | Kiểu | Constraint | Mô tả |
|---|---|---|---|
| `id` | `BIGINT` | PK, AUTO_INCREMENT | Primary key nội bộ |
| `user_id` | `VARCHAR` | — | UUID từ Keycloak (mapping key) |
| `email` | `VARCHAR` | UNIQUE, NOT NULL | Địa chỉ email |
| `username` | `VARCHAR` | — | Tên đăng nhập |
| `first_name` | `VARCHAR` | — | Tên đệm |
| `last_name` | `VARCHAR` | — | Họ |
| `dob` | `DATE` | — | Ngày sinh |
| `name` | `VARCHAR` | NOT NULL | Họ và tên đầy đủ |

> **ℹ️ Ghi chú:** Password **KHÔNG được lưu** trong bảng này. Tất cả thông tin xác thực được quản lý bởi Keycloak.

---

## 9. Vấn đề đã biết & Ghi chú

> **⚠️ Bug tiềm ẩn** trong `UserServiceImpl.createUser()` — field `lastName` bị set 2 lần:
> ```java
> .lastName(dto.getLastName())   // dòng 92
> .lastName(dto.getLastName())   // dòng 93 — trùng lặp, nên là firstName
> ```
> Cần kiểm tra lại `UserCreationParam.builder()` để đảm bảo `firstName` được set đúng.

> **⚠️ Conflict cấu hình:** `application.yml` dùng MySQL (port 9005), `application.properties` dùng H2 in-memory (port 9002). Spring Boot sẽ merge cả hai — cần làm rõ môi trường nào dùng file nào.

> **🚨 DELETE không đồng bộ:** `DELETE /api/v1/users/{id}` chỉ xóa trong local DB. User vẫn có thể đăng nhập vào Keycloak. Cần bổ sung logic gọi Keycloak Admin API để xóa hoàn toàn.

> **🚨 UPDATE không đồng bộ:** `PUT /api/v1/users/{id}` chỉ cập nhật local DB. Thông tin trong Keycloak (email, tên...) không được đồng bộ.

---

## 10. Chạy service

```bash
# Yêu cầu:
#   - Keycloak đang chạy tại http://localhost:8180
#   - Eureka Server đang chạy tại http://localhost:8761
#   - MySQL tại localhost:3306 (nếu dùng yml config)

cd userservice
./mvnw spring-boot:run
```

Service sẽ tự đăng ký với Eureka dưới tên `userservice`.
