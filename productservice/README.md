# Product Service

## 1. Tổng quan

`productservice` là service quản lý dữ liệu danh mục sản phẩm trong hệ thống thương mại điện tử. Service này chịu trách nhiệm cho các nghiệp vụ liên quan đến:

- Quản lý sản phẩm (`Product`)
- Quản lý danh mục (`Category`)
- Quản lý kích thước (`Size`)
- Quản lý chi tiết variant sản phẩm (`ProductDetail`)

Service này được xây dựng theo mô hình CQRS (Command Query Responsibility Segregation) với Axon Framework, tách biệt rõ giữa:

- `Command side`: xử lý tạo/sửa/xóa dữ liệu
- `Query side`: đọc dữ liệu phục vụ API
- `Read model`: dữ liệu JPA lưu trong database để query nhanh

---

## 2. Mục tiêu chức năng

Service hỗ trợ các chức năng chính sau:

1. Tạo, cập nhật, xóa sản phẩm
2. Tạo, cập nhật, xóa danh mục sản phẩm
3. Tạo, cập nhật, xóa kích thước
4. Tạo, cập nhật, xóa chi tiết sản phẩm theo product + size
5. Lấy danh sách và chi tiết sản phẩm
6. Lọc sản phẩm theo tên, danh mục, khoảng giá
7. Đồng bộ dữ liệu qua Axon Event Store và Event Bus

---

## 3. Công nghệ sử dụng

- Java 17
- Spring Boot 4.0.1
- Spring Web MVC
- Spring Data JPA
- Spring Cloud Netflix Eureka Client
- Axon Framework 4.9.3
- H2 Database (database in-memory cho dev/test)
- Kafka integration từ `commonservice`
- Maven

---

## 4. Kiến trúc hệ thống

Service này tuân theo cấu trúc CQRS theo Axon:

### 4.1 Command side

- `controller/`: nhận request từ client
- `command/`: chứa command objects và aggregate
- `event/`: chứa event objects
- `aggergate/`: aggregate xử lý nghiệp vụ

Flow xử lý command:

1. Client gọi API REST
2. Controller tạo command
3. `CommandGateway` gửi command đến Axon
4. `ProductAggregate` / `CategoryAggregate` / `SizeAggregate` / `ProductDetailAggregate` xử lý command
5. Aggregate gọi `AggregateLifecycle.apply(event)`
6. Event được lưu vào Event Store và phát ra Event Bus
7. Projection cập nhật database read model

### 4.2 Query side

- `query/controller/`: API query đọc dữ liệu
- `query/projection/`: nơi xử lý `@QueryHandler`
- `query/queries/`: các query class
- `query/specification/`: filter specification cho JPA
- `command/data/`: JPA entity và repository

Flow xử lý query:

1. Client gọi API GET
2. Controller tạo Query object
3. `QueryGateway` gửi query đến Axon
4. Projection xử lý query và trả về DTO
5. Dữ liệu đọc từ JPA repository hoặc specification

---

## 5. Cấu trúc thư mục chính

```text
productservice/
├── src/
│   ├── main/
│   │   ├── java/com/shoping/productservice/
│   │   │   ├── ProductserviceApplication.java
│   │   │   ├── command/
│   │   │   │   ├── aggergate/
│   │   │   │   ├── command/
│   │   │   │   ├── controller/
│   │   │   │   ├── data/
│   │   │   │   ├── event/
│   │   │   │   └── model/
│   │   │   └── query/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── projection/
│   │   │       ├── queries/
│   │   │       └── specification/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── HELP.md
└── README.md
```

---

## 6. Domain model

### 6.1 Product

Entity `Product` lưu thông tin sản phẩm:

- `id`: UUID hoặc string id duy nhất
- `name`: tên sản phẩm
- `description`: mô tả
- `price`: giá
- `quantity`: số lượng tồn kho
- `idCategory`: danh mục sản phẩm
- `imageUrl`: địa chỉ ảnh

### 6.2 Category

Entity `Category`:

- `id`
- `name`
- `description`
- `status`

### 6.3 Size

Entity `Size`:

- `id`
- `name`
- `description`
- `status`

### 6.4 ProductDetail

Entity `ProductDetail` lưu variant sản phẩm theo `product + size`:

- `id`
- `product`: liên kết tới product
- `size`: liên kết tới size
- `quantity`
- `price`
- `status`

---

## 7. API endpoints

## 7.1 Product APIs

### Command APIs

- `POST /api/v1/products`
    - Tạo sản phẩm mới
    - Body: `ProductRequestModel`

- `PUT /api/v1/products/{productId}`
    - Cập nhật sản phẩm theo id

- `DELETE /api/v1/products/{productId}`
    - Xóa sản phẩm

### Query APIs

- `GET /api/v1/products`
    - Lấy danh sách tất cả sản phẩm

- `GET /api/v1/products/{ProductId}`
    - Lấy chi tiết sản phẩm theo id

- `GET /api/v1/products/filter`
    - Lọc sản phẩm theo tham số:
        - `name`
        - `category`
        - `minPrice`
        - `maxPrice`

---

## 7.2 Category APIs

### Command APIs

- `POST /api/v1/category`
- `POST /api/v1/category/{categoryId}`
- `DELETE /api/v1/category/{categoryId}`

### Query APIs

- `GET /api/v1/category`
- `GET /api/v1/category/{categoryId}`

---

## 7.3 Size APIs

### Command APIs

- `POST /api/v1/size`
- `PUT /api/v1/size/{sizeId}`
- `DELETE /api/v1/size/{sizeId}`

### Query APIs

- `GET /api/v1/size`
- `GET /api/v1/size/{sizeId}`

---

## 7.4 Product Detail APIs

### Command APIs

- `POST /api/v1/product-details`
- `PUT /api/v1/product-details/{productDetailId}`
- `DELETE /api/v1/product-details/{productDetailId}`

### Query APIs

- `GET /api/v1/product-details`
- `GET /api/v1/product-details/{id}`

---

## 8. Ví dụ payload để paste vào Postman

### 8.1 Tạo Category

Endpoint:

```http
POST http://localhost:9002/api/v1/category
```

Body:

```json
{
    "name": "Nam",
    "description": "Danh mục thời trang nam",
    "status": true
}
```

### 8.2 Cập nhật Category

```http
POST http://localhost:9002/api/v1/category/{categoryId}
```

Body:

```json
{
    "name": "Nam - Updated",
    "description": "Danh mục thời trang nam đã cập nhật",
    "status": true
}
```

### 8.3 Xóa Category

```http
DELETE http://localhost:9002/api/v1/category/{categoryId}
```

---

### 8.4 Tạo Size

Endpoint:

```http
POST http://localhost:9002/api/v1/size
```

Body:

```json
{
    "name": "M",
    "description": "Size trung bình",
    "status": true
}
```

### 8.5 Cập nhật Size

```http
PUT http://localhost:9002/api/v1/size/{sizeId}
```

Body:

```json
{
    "name": "L",
    "description": "Size lớn",
    "status": true
}
```

### 8.6 Xóa Size

```http
DELETE http://localhost:9002/api/v1/size/{sizeId}
```

---

### 8.7 Tạo Product

Endpoint:

```http
POST http://localhost:9002/api/v1/products
```

Body:

```json
{
    "name": "Áo thun nam",
    "description": "Áo thun cotton 100%",
    "price": 250000,
    "quantity": 100,
    "idCategory": "cat-001",
    "imageUrl": "https://example.com/images/ao-thun-nam.jpg"
}
```

### 8.8 Cập nhật Product

```http
PUT http://localhost:9002/api/v1/products/{productId}
```

Body:

```json
{
    "name": "Áo thun nam premium",
    "description": "Áo thun nam premium, chất liệu cotton 100%",
    "price": 290000,
    "quantity": 80,
    "idCategory": "cat-001",
    "imageUrl": "https://example.com/images/ao-thun-nam-premium.jpg"
}
```

### 8.9 Xóa Product

```http
DELETE http://localhost:9002/api/v1/products/{productId}
```

---

### 8.10 Tạo Product Detail

Endpoint:

```http
POST http://localhost:9002/api/v1/product-details
```

Body:

```json
{
    "productId": "prod-001",
    "sizeId": "size-001",
    "quantity": 20,
    "price": 260000,
    "status": true
}
```

### 8.11 Cập nhật Product Detail

```http
PUT http://localhost:9002/api/v1/product-details/{productDetailId}
```

Body:

```json
{
    "productId": "prod-001",
    "sizeId": "size-002",
    "quantity": 15,
    "price": 270000,
    "status": true
}
```

### 8.12 Xóa Product Detail

```http
DELETE http://localhost:9002/api/v1/product-details/{productDetailId}
```

---

### 8.13 Query: lấy tất cả sản phẩm

```http
GET http://localhost:9002/api/v1/products
```

### 8.14 Query: lấy chi tiết sản phẩm theo id

```http
GET http://localhost:9002/api/v1/products/{productId}
```

### 8.15 Query: lọc sản phẩm

```http
GET http://localhost:9002/api/v1/products/filter?name=thun&category=Nam&minPrice=100000&maxPrice=500000
```

### 8.16 Query: lấy tất cả danh mục

```http
GET http://localhost:9002/api/v1/category
```

### 8.17 Query: lấy chi tiết danh mục

```http
GET http://localhost:9002/api/v1/category/{categoryId}
```

### 8.18 Query: lấy tất cả size

```http
GET http://localhost:9002/api/v1/size
```

### 8.19 Query: lấy chi tiết size

```http
GET http://localhost:9002/api/v1/size/{sizeId}
```

### 8.20 Query: lấy tất cả product detail

```http
GET http://localhost:9002/api/v1/product-details
```

---

## 9. Cấu hình ứng dụng

File cấu hình chính:

`src/main/resources/application.properties`

Các cấu hình quan trọng:

```properties
spring.application.name=productservice
server.port = 9002

eureka.client.service-url.defaultZone = http://localhost:8761/eureka

spring.datasource.url=jdbc:h2:mem:productservice
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update

axon.axonserver.servers=localhost:8124
```

### Ý nghĩa

- `port 9002`: service chạy trên cổng 9002
- `H2 in-memory`: DB local cho dev/test
- `Eureka`: đăng ký vào discovery server
- `Axon Server`: dùng Axon Server tại `localhost:8124`
- `ddl-auto=update`: tự cập nhật schema JPA

---

## 10. Luồng xử lý nghiệp vụ thực tế

### 10.1 Tạo sản phẩm

```text
HTTP POST /api/v1/products
        ↓
ProductCommandController
        ↓
CreateProductCommand
        ↓
ProductAggregate
        ↓
ProductCreateEvent
        ↓
Event Store + Event Bus
        ↓
Projection/Repository
        ↓
products table updated
```

### 10.2 Query sản phẩm

```text
HTTP GET /api/v1/products
        ↓
BookQueryController
        ↓
GetAllProductQuery
        ↓
BookProjection
        ↓
ProductRepository
        ↓
List<ProductResponseModel>
```

---

## 11. Các lớp chính trong service

### Command layer

- `CreateProductCommand`
- `UpdateProductCommand`
- `DeleteProductCommand`
- `CreateCategoryCommand`
- `UpdateCategoryCommand`
- `DeleteCategoryCommand`
- `SizeCreateCommand`
- `SizeUpdateCommand`
- `SizeDeleteCommand`
- `CreateProductDetailCommand`
- `UpdateProductDetailCommand`
- `DeleteProductDetailCommand`

### Aggregate layer

- `ProductAggregate`
- `CategoryAggregate`
- `SizeAggregate`
- `ProductDetailAggregate`

### Query layer

- `GetAllProductQuery`
- `GetProductDetailQuery`
- `FilterProductQuery`
- `GetAllCategoryQuery`
- `GetCategoryDetailQuery`
- `GetAllSizeQuery`
- `GetSizeDetailQuery`

---

## 12. Cách chạy service

### Yêu cầu

- JDK 17+
- Maven
- Axon Server đang chạy
- Eureka Server đang chạy

### Chạy local

Từ thư mục `productservice`:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

Nếu muốn chạy bằng IDE, chọn class:

`com.shoping.productservice.ProductserviceApplication`

---

## 13. Thứ tự nên tạo đối tượng trong Product Service

Khi triển khai service này, cần ưu tiên theo logic nghiệp vụ để tránh tạo sản phẩm trước khi dữ liệu phụ trợ đã sẵn sàng. Thứ tự nên làm như sau:

### 13.1 Ưu tiên 1: `Category`

`Category` nên được tạo trước vì sản phẩm thuộc về một danh mục cụ thể.

- `Product` có trường `idCategory`
- Nếu chưa có category, product thiếu thông tin phân loại
- API sản phẩm sẽ dễ bị lỗi hoặc không hoàn chỉnh khi truy vấn theo danh mục

Vì vậy, khởi tạo flow đúng nên là:

```text
Category -> Product -> ProductDetail
```

### 13.2 Ưu tiên 2: `Size`

`Size` nên tạo ngay sau category vì khi product detail muốn xác định variant theo size, cần có size đã tồn tại.

- `ProductDetail` lưu `productId` và `sizeId`
- `ProductDetail` không thể hoạt động tốt nếu `size` chưa có sẵn

Flow hợp lý:

```text
Category -> Size -> Product -> ProductDetail
```

### 13.3 Ưu tiên 3: `Product`

Sau khi có category và size, mới tạo product.

- Product là entity chính của catalog
- Product có thể tồn tại độc lập nhưng hầu hết flow thực tế sẽ cần category
- ProductDetail dựa vào `productId`

### 13.4 Ưu tiên 4: `ProductDetail`

`ProductDetail` nên tạo sau cùng vì đây là đối tượng phụ thuộc vào cả product và size.

- Một product có thể có nhiều size khác nhau
- Một size có thể áp dụng cho nhiều product
- ProductDetail là “biến thể chi tiết” của sản phẩm, không phải entity gốc nhất

Flow thực tế nên là:

```text
Category
   ↓
Size
   ↓
Product
   ↓
ProductDetail
```

### 13.5 Khuyến nghị triển khai trong dự án

Nếu đang xây dựng từ đầu, nên tạo theo thứ tự:

1. `Category` API + entity + aggregate
2. `Size` API + entity + aggregate
3. `Product` API + entity + aggregate
4. `ProductDetail` API + entity + aggregate
5. Query và filter
6. Test và tích hợp với gateway

### 13.6 Lý do vì sao không nên tạo Product trước

Vì trong business logic thực tế:

- Product thuộc về Category
- ProductDetail thuộc về Product + Size
- Nếu tạo Product trước mà không có Category/Size, dữ liệu sẽ thiếu quan hệ và dễ phát sinh lỗi khi join/filter/query

---

## 14. Lưu ý kỹ thuật

- Service đang dùng `H2` memory database nên dữ liệu sẽ mất khi restart ứng dụng.
- Dữ liệu product/category/size/detail chủ yếu được xây dựng theo mô hình projection từ event.
- Một số class tên gọi còn chưa đồng nhất (ví dụ `BookQueryController`, `BookProjection`), nhưng chức năng thực tế vẫn thuộc về product query logic.
- Trong phần filter, cần kiểm tra đồng bộ field giữa `ProductSpecification` và entity để đảm bảo lọc theo danh mục hoạt động chính xác trong môi trường production.

---

## 14. Kết luận

`productservice` là service trung tâm quản lý catalog của hệ thống thương mại điện tử, tập trung vào các nghiệp vụ:

- sản phẩm
- danh mục
- size
- variant theo size

Với kiến trúc CQRS + Axon, service có khả năng mở rộng tốt, tách biệt rõ command và query, phù hợp cho các hệ thống có yêu cầu rõ ràng về event-driven architecture và xử lý đồng bộ dữ liệu giữa các service.

---

Nếu muốn, tôi có thể tiếp tục viết thêm cho bạn các phần sau:

1. Document chi tiết từng API bằng Postman collection format
2. Sequence diagram cho flow create/update/delete product
3. File OpenAPI / Swagger spec cho service này
4. Bản tóm tắt kiến trúc cho team và reviewer
