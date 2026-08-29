package com.shoping.orderservice.command.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.shoping.orderservice.command.data.OrderItem;
import com.shoping.orderservice.command.data.OrderStatus;
import com.shoping.orderservice.command.model.OrderItemRequestModel;
import com.shoping.orderservice.command.model.OrderRequestModel;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateCommand {

    @TargetAggregateIdentifier
    private String id;

    private String orderId;

    private String userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Float totalAmount;

    private List<OrderItemCommand> listItems;

    private String shipAddress;

    private String shipPhone;

    private LocalDateTime createdAt;

}
