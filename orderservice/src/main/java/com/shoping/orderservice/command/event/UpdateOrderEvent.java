package com.shoping.orderservice.command.event;

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
public class UpdateOrderEvent {
    private String id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
