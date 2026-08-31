package com.shoping.orderservice.command.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    private String id;

    private String orderId;

    private String userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Float totalAmount;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> listItems;

    private String address;

    private String phone;

    private LocalDateTime createdAt;
}
