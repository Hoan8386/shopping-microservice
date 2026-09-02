# Order Service

## 1. Tổng quan

`orderservice` là service chịu trách nhiệm quản lý đơn hàng trong hệ thống thương mại điện tử. Service này tập trung vào các nghiệp vụ liên quan đến:

- Tạo đơn hàng (`Order`)
- Cập nhật đơn hàng
- Xóa đơn hàng
- Quản lý các mục hàng hóa trong đơn (`OrderItem`)
- Theo dõi trạng thái đơn hàng (`OrderStatus`)
- Kết nối với `productservice` để lấy thông tin chi tiết sản phẩm

Service này được xây dựng theo mô hình CQRS + Axon Framework, với xu hướng tách rõ:

- `Command side`: xử lý tạo/sửa/xóa đơn hàng
- `Event side`: phát sinh event từ aggregate
- `Application service`: orchestrate logic nghiệp vụ và gọi các query khác

---

## 2. Mục tiêu chức năng

Service hỗ trợ các chức năng chính sau:

1. Tạo đơn hàng mới
2. Cập nhật thông tin giao hàng và trạng thái đơn
3. Xóa đơn hàng
4. Quản lý danh sách sản phẩm trong mỗi đơn
5. Tính tổng giá trị đơn hàng dựa trên sản phẩm và số lượng
6. Gọi query đến `productservice` để lấy thông tin chi tiết sản phẩm
7. Ghi event vào Axon cho các service khác theo mô hình event-driven

---

## 3. Công nghệ sử dụng

- Java 17
- Spring Boot 4.0.1
- Spring Web MVC
- Spring Data JPA
- Spring Cloud Netflix Eureka Client
- Axon Framework 4.9.3
- H2 Database (in-memory)
- Maven
- Kafka/commonservice integration

---

## 4. Kiến trúc hệ thống

### 4.1 Command side

Các lớp chính:

- `command/controller/`: controller REST cho tạo/sửa/xóa order
- `command/aggregate/`: `OrderAggregate` xử lý command
- `command/command/`: các command class
- `command/event/`: các event class
- `command/service/`: `OrderApplicationService` xử lý business flow

Flow xử lý command:

1. Client gửi request HTTP
2. Controller nhận request và tạo command
3. `CommandGateway` gửi command đến Axon
4. `OrderAggregate` xử lý command
5. `AggregateLifecycle.apply(event)` phát ra event
6. Event được lưu trong Event Store
7. Dữ liệu read model được cập nhật qua projection hoặc repository

### 4.2 Application service layer

`OrderApplicationService` là nơi xử lý logic nghiệp vụ chính:

- đọc danh sách `productId` từ request body
- gọi `QueryGateway` đến `productservice` thông qua `GetDetailProductQuery`
- lấy thông tin chi tiết từng sản phẩm
- tạo `OrderItemCommand` từ dữ liệu đó
- tạo `OrderCreateCommand` và gửi đi

Đây là phần rất quan trọng trong service vì nó liên kết order với product.

### 4.3 Query side

Theo code hiện tại, phần query hiện chưa hoàn thiện:

- `orderQueryController` có endpoint GET nhưng body trả về `null`
- Chưa có query model rõ ràng, chưa có `@QueryHandler` thực thi

Nói cách khác, service này hiện đang tập trung nhiều vào command path hơn là query/read path.

---

## 5. Cấu trúc thư mục chính

```text
orderservice/
├── src/
│   ├── main/
│   │   ├── java/com/shoping/orderservice/
│   │   │   ├── OrderserviceApplication.java
│   │   │   ├── command/
│   │   │   │   ├── aggregate/
│   │   │   │   ├── command/
│   │   │   │   ├── controller/
│   │   │   │   ├── data/
│   │   │   │   ├── event/
│   │   │   │   ├── model/
│   │   │   │   ├── saga/
│   │   │   │   └── service/
│   │   │   └── querry/
│   │   │       └── controller/
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

### 6.1 Order

Entity `Order` lưu thông tin đơn hàng:

- `id`: id của order
- `orderId`: mã đơn hàng dạng chuỗi
- `userId`: mã người dùng
- `status`: trạng thái của đơn hàng
- `totalAmount`: tổng giá trị
- `listItems`: danh sách item trong order
- `address`: địa chỉ giao hàng
- `phone`: số điện thoại giao hàng
- `createdAt`: thời điểm tạo đơn

### 6.2 OrderItem

Entity `OrderItem` lưu chi tiết từng mặt hàng trong đơn:

- `id`
- `productId`: id sản phẩm
- `quantity`: số lượng
- `unitPrice`: giá đơn vị
- `subtotal`: tổng tiền item
- `order`: quan hệ ManyToOne tới `Order`

### 6.3 OrderStatus

Enum hiện tại:

```java
CREATED,
PENDING,
CONFIRMED,
CANCELLED
```

---

## 7. API endpoints

## 7.1 Order APIs

### Command APIs

- `POST /api/v1/order`
    - Tạo đơn hàng mới

- `PUT /api/v1/order/p/{id}`
    - Cập nhật đơn hàng

- `DELETE /api/v1/order`
    - Xóa đơn hàng

### Query APIs

- `GET /api/v1/order`
    - Lấy danh sách đơn hàng (hiện đang placeholder)

- `GET /api/v1/order/{orderId}`
    - Lấy chi tiết đơn hàng (hiện đang placeholder)

> Lưu ý: trong code hiện tại, các endpoint query chưa được implement đầy đủ và đang trả về `null`.

---

## 8. Ví dụ payload để paste vào Postman

### 8.1 Tạo đơn hàng

Endpoint:

```http
POST http://localhost:9004/api/v1/order
```

Body:

```json
{
    "userId": "user-001",
    "items": [
        {
            "productId": "prod-001",
            "unitPrice": 250000,
            "quantity": 2
        },
        {
            "productId": "prod-002",
            "unitPrice": 320000,
            "quantity": 1
        }
    ],
    "shipAddress": "123 Lê Lợi, Quận 1, TP.HCM",
    "shipPhone": "0909123456"
}
```

### 8.2 Cập nhật đơn hàng

Endpoint:

```http
PUT http://localhost:9004/api/v1/order/p/{orderId}
```

Body:

```json
{
    "userId": "user-001",
    "items": [
        {
            "productId": "prod-001",
            "unitPrice": 250000,
            "quantity": 2
        }
    ],
    "shipAddress": "456 Nguyễn Huệ, Quận 1, TP.HCM",
    "shipPhone": "0909000111"
}
```

### 8.3 Xóa đơn hàng

Endpoint:

```http
DELETE http://localhost:9004/api/v1/order
```

Nếu muốn xóa theo id thì cần bổ sung path param phù hợp vì hiện code đang không đọc `@PathVariable` trong method delete.

### 8.4 Lấy danh sách đơn hàng

```http
GET http://localhost:9004/api/v1/order
```

### 8.5 Lấy chi tiết đơn hàng

```http
GET http://localhost:9004/api/v1/order/{orderId}
```

---

## 9. Cấu hình ứng dụng

File cấu hình chính:

`src/main/resources/application.properties`

```properties
spring.application.name=orderservice
server.port = 9004

eureka.client.service-url.defaultZone = http://localhost:8761/eureka

spring.datasource.url=jdbc:h2:mem:orderservice
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update

axon.axonserver.servers=localhost:8124
```

### Ý nghĩa

- `port 9004`: service chạy trên cổng 9004
- `H2 in-memory`: database local cho dev/test
- `Eureka`: đăng ký vào discovery server
- `Axon Server`: dùng Axon Server tại `localhost:8124`

---

## 10. Luồng xử lý nghiệp vụ thực tế

### 10.1 Tạo đơn hàng

```text
HTTP POST /api/v1/order
        ↓
OrderCommandController
        ↓
OrderApplicationService.createOrder()
        ↓
Lấy ProductDetail từ productservice qua QueryGateway
        ↓
Tạo OrderItemCommand list
        ↓
OrderCreateCommand
        ↓
OrderAggregate
        ↓
CreateOrderEvent
        ↓
Event Store + Axon Bus
```

### 10.2 Cập nhật đơn hàng

```text
HTTP PUT /api/v1/order/p/{id}
        ↓
OrderCommandController
        ↓
OrderApplicationService.updateOrder()
        ↓
OrderUpdateCommand
        ↓
OrderAggregate
        ↓
UpdateOrderEvent
```

### 10.3 Xóa đơn hàng

```text
HTTP DELETE /api/v1/order
        ↓
OrderCommandController
        ↓
OrderApplicationService.deleteOrder()
        ↓
OrderDeleteCommand
        ↓
OrderAggregate
        ↓
DeleteOrderEvent
```

---

## 11. Các lớp chính trong service

### Command layer

- `OrderCreateCommand`
- `OrderUpdateCommand`
- `OrderDeleteCommand`
- `OrderItemCommand`

### Aggregate layer

- `OrderAggregate`

### Event layer

- `CreateOrderEvent`
- `UpdateOrderEvent`
- `DeleteOrderEvent`

### Data layer

- `Order`
- `OrderItem`
- `OrderRepository`
- `OrderStatus`

### Application layer

- `OrderApplicationService`

### Controller layer

- `OrderCommandController`
- `orderQueryController`

---

## 12. Cách chạy service

### Yêu cầu

- JDK 17+
- Maven
- Axon Server đang chạy
- Eureka Server đang chạy

### Chạy local

Từ thư mục `orderservice`:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

Nếu chạy bằng IDE, chọn class:

`com.shoping.orderservice.OrderserviceApplication`

---

## 13. Thứ tự nên tạo dữ liệu trong Order Service

Với logic thực tế, nên xây dựng theo thứ tự sau:

1. `Order` - đơn hàng chính
2. `OrderItem` - các mục hàng trong đơn
3. `OrderStatus` - trạng thái đơn
4. `OrderCreateCommand` / `OrderUpdateCommand` / `OrderDeleteCommand`
5. `OrderAggregate`
6. `OrderApplicationService`
7. API controller
8. Query API sau khi core logic đã ổn định

Lý do:

- Đơn hàng cần có item để có nghĩa
- `OrderItem` phụ thuộc vào `Order`
- Trạng thái và business flow phải tạo trước khi xây query lấy dữ liệu

---

## 14. Lưu ý kỹ thuật

- `OrderApplicationService` đang gọi `productservice` để lấy `ProductDetailResponseCommonModel` trước khi tạo đơn hàng. Đây là cách service này liên kết giữa order và product.
- Query endpoint hiện chưa được triển khai hoàn chỉnh; class `orderQueryController` đang trả về `null`.
- `OrderCommandController` có một số route chưa đồng nhất, ví dụ:
    - `PUT /api/v1/order/p/{id}` không đúng chuẩn tên param như `@PathVariable String orderId`
    - `DELETE /api/v1/order` không nhận `id` rõ ràng như `/{orderId}`
- `OrderAggregate` tạo event trong `OrderCreateCommand` nhưng không copy full dữ liệu vào event như các service khác, nên cần kiểm tra lại nếu muốn làm event-driven đầy đủ hơn.
- `Order` entity đang dùng `@OneToMany(mappedBy = "order")` nên cấu trúc quan hệ giữa Order và OrderItem cần xác nhận lại khi integrate DB thật.

---

## 15. Kết luận

`orderservice` là service quản lý đơn hàng của hệ thống, có vai trò trung tâm trong quy trình mua hàng: nhận sản phẩm cần đặt, kiểm tra thông tin sản phẩm từ product service, tạo đơn, cập nhật trạng thái và xử lý xóa.

Mặc dù kiến trúc CQRS + Axon đã được áp dụng, phần query và một số route controller hiện vẫn còn chưa hoàn thiện. Vì vậy, nếu làm tiếp theo, nên ưu tiên:

1. fix route API
2. hoàn thiện query side
3. hoàn thiện event handling và read model
4. kiểm tra tính đồng bộ với product service

---

Nếu muốn, tôi có thể tiếp tục làm thêm cho bạn:

1. tạo file Swagger/OpenAPI cho order service
2. viết sequence diagram cho flow tạo đơn hàng
3. bổ sung Postman collection theo đúng từng request
4. fix lại các endpoint chưa chuẩn trong order service








tạo email templates để gửi khi thành công 
từ saga gọi tới user service để lấy thông tin 
maping data để gủi email 

chuyển sang api gateway để đăng nhập 
lấy thông tin user để giải mã để gửi email 