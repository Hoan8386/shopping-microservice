package com.shoping.orderservice.command.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.orderservice.command.data.Order;
import com.shoping.orderservice.command.data.OrderItem;
import com.shoping.orderservice.command.data.OrderItemDTO;
import com.shoping.orderservice.command.data.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderEventHandler {
    @Autowired
    private OrderRepository orderRepository;

    @EventHandler
    public void on(CreateOrderEvent event) {
        Order order = new Order();
        order.setId(event.getId());
        order.setOrderId(event.getOrderId());
        order.setUserId(event.getUserId());
        order.setStatus(event.getStatus());
        order.setTotalAmount(event.getTotalAmount());
        order.setShipAddress(event.getShipAddress());
        order.setShipPhone(event.getShipPhone());
        order.setCreatedAt(event.getCreatedAt());
        List<OrderItem> orderItems = new ArrayList<>();
        if (event.getListItems() != null) {
            for (OrderItemDTO itemDTO : event.getListItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(UUID.randomUUID().toString());
                orderItem.setProductId(itemDTO.getProductId());
                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setUnitPrice(itemDTO.getUnitPrice());
                orderItem.setSubtotal(itemDTO.getSubtotal());
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            }
        }
        order.setListItems(orderItems);
        orderRepository.save(order);

    }

    @EventHandler
    public void on(UpdateOrderEvent event) {
        Optional<Order> oldOrder = orderRepository.findById(event.getId());
        if (oldOrder.isPresent()) {
            Order order = oldOrder.get();
            order.setStatus(event.getStatus());
            order.setShipPhone(event.getPhone());
            order.setShipAddress(event.getAddress());
            orderRepository.save(order);
        }
    }

    @EventHandler
    public void on(DeleteOrderEvent event) {
        Optional<Order> oldOrder = orderRepository.findById(event.getId());
        if (oldOrder.isPresent()) {
            orderRepository.deleteById(event.getId());
        }
    }
}
