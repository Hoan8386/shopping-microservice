package com.shoping.orderservice.command.aggregate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.shoping.commonservice.model.response.DTO.OrderItemDTO;
import com.shoping.orderservice.command.command.OrderCreateCommand;
import com.shoping.orderservice.command.command.OrderDeleteCommand;
import com.shoping.orderservice.command.command.OrderUpdateCommand;
import com.shoping.orderservice.command.data.OrderStatus;
import com.shoping.orderservice.command.event.CreateOrderEvent;
import com.shoping.orderservice.command.event.DeleteOrderEvent;
import com.shoping.orderservice.command.event.UpdateOrderEvent;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Aggregate
@NoArgsConstructor
@Getter
@Setter

public class OrderAggregate {

    @AggregateIdentifier
    private String id;

    private String orderId;

    private String userId;

    private OrderStatus status;

    private Double totalAmount;

    private List<OrderItemDTO> listItems;

    private String address;

    private String phone;

    private LocalDateTime createdAt;

    @CommandHandler
    public OrderAggregate(OrderCreateCommand command) {
        List<OrderItemDTO> items = command.getListItems().stream().map(
                item -> {
                    OrderItemDTO dto = new OrderItemDTO();
                    dto.setProductId(item.getProductDetailId());
                    dto.setQuantity(item.getQuantity());
                    dto.setSubtotal(item.getSubTotal());
                    dto.setUnitPrice(item.getUnitPrice());
                    return dto;
                }).collect(Collectors.toList());
        CreateOrderEvent createOrderEvent = new CreateOrderEvent();
        createOrderEvent.setId(command.getId());
        createOrderEvent.setOrderId(command.getOrderId());
        createOrderEvent.setUserId(command.getUserId());
        createOrderEvent.setStatus(command.getStatus());

        // Double -> Float
        createOrderEvent.setTotalAmount(command.getTotalAmount());

        createOrderEvent.setListItems(items);

        // shipAddress -> address
        createOrderEvent.setShipAddress(command.getShipAddress());

        // shipPhone -> phone
        createOrderEvent.setShipPhone(command.getShipPhone());

        createOrderEvent.setCreatedAt(command.getCreatedAt());
        AggregateLifecycle.apply(createOrderEvent);
    }

    @CommandHandler
    public OrderAggregate(OrderUpdateCommand command) {
        UpdateOrderEvent updateOrderEvent = new UpdateOrderEvent();
        BeanUtils.copyProperties(command, updateOrderEvent);
        AggregateLifecycle.apply(updateOrderEvent);
    }

    @CommandHandler
    public OrderAggregate(OrderDeleteCommand command) {
        DeleteOrderEvent deleteOrderEvent = new DeleteOrderEvent();
        BeanUtils.copyProperties(command, deleteOrderEvent);
        AggregateLifecycle.apply(deleteOrderEvent);
    }

    @EventSourcingHandler
    public void on(CreateOrderEvent createOrderEvent) {
        this.id = createOrderEvent.getId();
        this.orderId = createOrderEvent.getOrderId();
        this.userId = createOrderEvent.getUserId();
        this.status = createOrderEvent.getStatus();
        this.totalAmount = createOrderEvent.getTotalAmount();
        this.listItems = createOrderEvent.getListItems();
        this.address = createOrderEvent.getShipAddress();
        this.phone = createOrderEvent.getShipPhone();
        this.createdAt = createOrderEvent.getCreatedAt();
    }

    @EventSourcingHandler
    public void on(UpdateOrderEvent updateOrderEvent) {
        this.id = updateOrderEvent.getId();
        this.status = updateOrderEvent.getStatus();
        this.phone = updateOrderEvent.getPhone();
        this.address = updateOrderEvent.getAddress();
    }

    @EventSourcingHandler
    public void on(DeleteOrderEvent deleteOrderEvent) {
        this.id = deleteOrderEvent.getId();

    }
}
