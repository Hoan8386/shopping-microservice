package com.shoping.orderservice.command.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.shoping.orderservice.command.data.OrderItem;
import com.shoping.orderservice.command.data.OrderItemDTO;
import com.shoping.orderservice.command.data.OrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderEvent {
    private String id;
    private String orderId;

    private String userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double  totalAmount;

    private List<OrderItemDTO> listItems;

    private String shipAddress;

    private String shipPhone;

    private LocalDateTime createdAt;
}
