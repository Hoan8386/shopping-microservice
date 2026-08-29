package com.shoping.orderservice.command.event;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.shoping.orderservice.command.data.Order;
import com.shoping.orderservice.command.data.OrderRepository;

public class OrderEventHandler {
    @Autowired
    private OrderRepository orderRepository;

    @EventHandler
    public void on(CreateOrderEvent event) {
        Order order = new Order();
        BeanUtils.copyProperties(event, order);
        orderRepository.save(order);
    }

    @EventHandler
    public void on(UpdateOrderEvent event) {
        Optional<Order> oldOrder = orderRepository.findById(event.getId());
        if (oldOrder.isPresent()) {
            Order order = oldOrder.get();
            order.setStatus(event.getStatus());
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
