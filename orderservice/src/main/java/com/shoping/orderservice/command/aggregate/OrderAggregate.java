package com.shoping.orderservice.command.aggregate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.shoping.orderservice.command.command.CreateOrderCommand;
import com.shoping.orderservice.command.data.OrderItem;
import com.shoping.orderservice.command.data.OrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@NoArgsConstructor
@Getter
@Setter
public class OrderAggregate {
    @AggregateIdentifier

    private String id;
    
    private String orderId;
    
    private String userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalAmount;

    @OneToMany (mappedBy = "order")
    private List<OrderItem> listItems;

    private LocalDateTime createdAt;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
            
    }
}
