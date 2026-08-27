package com.shoping.orderservice.command.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.shoping.orderservice.command.data.OrderItem;
import com.shoping.orderservice.command.data.OrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;

public class CreateProductEvent {
     private Long id;

    private String orderId;
    
    private String userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalAmount;

    @OneToMany (mappedBy = "order")
    private List<OrderItem> listItems;

    private LocalDateTime createdAt;
}
