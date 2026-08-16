Producer
   │
   │ gửi message
   ▼
┌───────────────────────────┐
│        Kafka Cluster      │
│                           │
│  Broker 1   Broker 2      │
│    │          │           │
│    └──── Topic ───────────┤
│          │                │
│      Partitions           │
│      P0  P1  P2           │
└────────────┬──────────────┘
             │
             ▼
       Consumer Group
        ├── Consumer 1
        ├── Consumer 2
        └── Consumer 3

Kafka — ngắn gọn
Kafka: hệ thống trung gian truyền/lưu trữ message giữa các service.
Producer: gửi message vào Kafka.
Topic: nơi chứa message.
Partition: chia Topic thành nhiều phần để xử lý song song.
Consumer: đọc và xử lý message từ Kafka.
Consumer Group: nhóm các Consumer cùng xử lý một Topic.
group.id: tên của Consumer Group, dùng để Kafka biết Consumer nào thuộc cùng nhóm.
Broker: server Kafka lưu trữ và xử lý message.
Kafka tự động assign Partition cho Consumer trong cùng Group và rebalance khi Consumer thay đổi.
Quan hệ chính
Producer
   ↓
 Topic
 ├── Partition 0 ──┐
 ├── Partition 1 ──┼──→ Consumer Group
 └── Partition 2 ──┘       │
                            ├── Consumer 1
                            ├── Consumer 2
                            └── Consumer 3

Nhớ: Producer gửi → Topic chứa → Partition chia → Consumer đọc → group.id xác định nhóm → Kafka tự chia Partition.


vd
                         Cart Service
                           Producer
                              │
                    "OrderCreatedEvent"
                              │
                              ▼
                 ┌─────────────────────────┐
                 │      Kafka Topic        │
                 │    order-created        │
                 │                         │
                 │  ┌─────┐ ┌─────┐ ┌─────┐│
                 │  │ P0  │ │ P1  │ │ P2  ││
                 │  └─────┘ └─────┘ └─────┘│
                 └────────────┬────────────┘
                              │
              ┌───────────────┴────────────────┐
              │                                │
              ▼                                ▼
      Consumer Group                    Consumer Group
       "order-group"                "notification-group"
              │                                │
       ┌──────┼──────┐                   ┌─────┼─────┐
       ▼      ▼      ▼                   ▼     ▼     ▼
   Consumer  Consumer  Consumer       Consumer Consumer Consumer
      1        2        3                1       2       3
       │        │        │                │       │       │
       └────┬───┘        │                └───┬───┘       │
            │            │                    │           │
            ▼            ▼                    ▼           ▼
       Order Service #1 #2 #3          Notification #1 #2 #3

Khi mà service bị disconnect thì kafka vẫn lưu lại message sau khi service chạy lại thì service sẽ chạy lại những message đó (kafka gữi message tới topic đó khi service chạy lại thì sẽ kiểm ra xem có message nào trên topic đó không để chạy)

Error handling 
   lỗi chia làm 2 loại
      Lỗi có thể retry 
         - Blocking retry (simple retry)
         - Non-Blocking retry (separate retry queue)
      Lối không thể retry

Retry Topic dùng khi Consumer đã nhận được message nhưng xử lý message bị lỗi. Message được chuyển sang Retry Topic để chờ một khoảng thời gian, sau đó Consumer lấy ra xử lý lại. Nếu vẫn lỗi nhiều lần thì chuyển vào DLQ.

ảnh cơ chế hoạt động
({6561D4AA-0E5E-4AE9-9D12-8909FAF8F1BD}.png)

Ví dụ
Ví dụ thực tế trong hệ thống Shopping của bạn:

Khi khách hàng đặt hàng, Order Service gửi sự kiện OrderCreated vào Kafka.

Order Service
      │
      │ OrderCreated
      ↓
order-topic
      │
      ↓
Notification Service

Notification Service nhận được message và cần gửi email xác nhận đơn hàng.

Nhưng lúc này Email Service/SMTP bị lỗi:

Notification Service
      │
      │ Gửi email ❌
      │ "SMTP server không phản hồi"
      ↓
Retry Topic 1

Message OrderCreated được đưa vào Retry Topic 1 để chờ, ví dụ 5 giây.

Sau 5 giây:

Retry Topic 1
      │
      │ OrderCreated
      ↓
Notification Service
      │
      │ Gửi email lại
      ↓
Email thành công ✅

Nếu email vẫn lỗi:

Retry Topic 1
      ↓
Retry Topic 2
      ↓
Retry Topic 3
      ↓
DLQ ❌
Hiểu đơn giản

Khách đặt hàng → Notification Service nhận được sự kiện → gửi email thất bại → đưa sự kiện vào Retry Topic để chờ → lấy ra thử gửi lại → thành công thì xong, thất bại nhiều lần thì đưa vào DLQ để xử lý sau.

Điểm quan trọng: Message không bị mất chỉ vì Notification Service tạm thời không gửi được email.