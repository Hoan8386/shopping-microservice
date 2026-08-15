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