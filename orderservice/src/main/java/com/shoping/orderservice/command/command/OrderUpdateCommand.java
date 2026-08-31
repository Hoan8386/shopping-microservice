package com.shoping.orderservice.command.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.shoping.orderservice.command.data.OrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateCommand {

    @TargetAggregateIdentifier
    private String id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String shipAddress;

    private String shipPhone;
}
