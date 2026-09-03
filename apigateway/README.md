# API Gateway

API Gateway la cong vao duy nhat cua cac microservice. Gateway tiep nhan request tu frontend, xac thuc JWT voi Keycloak, dinh tuyen request den service phu hop va bo sung thong tin user vao header.

## 1. Thanh phan chinh

- **Spring Cloud Gateway WebFlux**: dinh tuyen request.
- **Keycloak**: cap phat va cung cap thong tin xac thuc JWT.
- **Spring Security OAuth2 Resource Server**: kiem tra JWT tai gateway.
- **Eureka**: tim microservice qua `lb://service-name`.
- **Redis**: ho tro rate limiting.

## 2. Flow xu ly request

```text
Frontend
   |
   |  Authorization: Bearer <access-token>
   v
API Gateway :8080
   |
   |-- /api/v1/public/** -> cho phep khong can JWT
   |
   |-- Cac route khac -> Spring Security kiem tra JWT
   |       |
   |       |-- JWT hop le -> tiep tuc
   |       |-- JWT khong hop le/thieu -> HTTP 401
   |
   |-- JwtHeaderFilter doc claim sub, preferred_username
   |       va them X-User-Id, X-Username
   |
   |-- KeyAuthFilter / RequestRateLimiter (neu route co cau hinh)
   v
Microservice dich
```

## 3. Xac thuc JWT voi Keycloak

Gateway khong gui JWT sang Keycloak de giai ma. Khi khoi dong, Spring doc cau hinh tu `issuer-uri`:

```text
http://localhost:8180/realms/shopping
```

Spring se tu dong:

1. Goi OIDC discovery cua realm `shopping`.
2. Lay public key JWKS tu Keycloak.
3. Kiem tra chu ky, issuer va thoi han cua JWT.
4. Tao `JwtAuthenticationToken` neu token hop le.

JWT chi duoc decode payload tai gateway. Public key cua Keycloak dung de xac minh token co that hay khong.

## 4. Cac route hien tai

| Path                  | Service           | Xu ly                                       |
| --------------------- | ----------------- | ------------------------------------------- |
| `/api/v1/public/**`   | `userservice`     | Khong can JWT                               |
| `/api/v1/users/**`    | `userservice`     | Can JWT, them header user                   |
| `/api/v1/product/**`  | `productservice`  | Can JWT theo security global, co rate limit |
| `/api/v1/employee/**` | `employeeservice` | Can JWT theo security global, co rate limit |

Service dich duoc tim qua Eureka, vi du `lb://userservice`.

## 5. Vi du goi API

Lay access token tu Keycloak:

```http
POST http://localhost:8180/realms/shopping/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=password&client_id=<client-id>&client_secret=<client-secret>&username=<username>&password=<password>
```

Goi API qua gateway:

```http
GET http://localhost:8080/api/v1/users/profile
Authorization: Bearer <access-token>
```

## 6. Header user

Voi route co `JwtHeaderFilter`, gateway doc cac claim trong JWT va them:

```http
X-User-Id: <sub>
X-Username: <preferred_username>
```

Microservice co the doc hai header nay de biet user dang dang nhap.

## 7. Luu y hien tai

- `KeyAuthFilter` dang duoc khai bao nhung phan kiem tra API key dang bi comment, nen hien tai filter nay chua thuc su validate API key.
- Vi `SecurityConfig` dung `.anyExchange().authenticated()`, cac route khong nam trong `/api/v1/public/**` van can JWT.
- Can khoi dong Keycloak voi realm `shopping`, Eureka va Redis truoc khi su dung day du gateway.
- Khong nen tin header `X-User-Id` tu client; gateway can ghi de header sau khi JWT da duoc xac thuc.
