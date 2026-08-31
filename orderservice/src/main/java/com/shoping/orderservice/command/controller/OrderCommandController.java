package com.shoping.orderservice.command.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.commonservice.util.anotation.ResponseId;
import com.shoping.orderservice.OrderserviceApplication;
import com.shoping.orderservice.command.command.OrderCreateCommand;
import com.shoping.orderservice.command.command.OrderItemCommand;
import com.shoping.orderservice.command.data.Order;
import com.shoping.orderservice.command.data.OrderItem;
import com.shoping.orderservice.command.data.OrderStatus;
import com.shoping.orderservice.command.model.OrderRequestModel;
import com.shoping.orderservice.command.service.OrderApplicationService;

import io.axoniq.axonserver.grpc.command.Command;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/order")
public class OrderCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private OrderApplicationService orderApplicationService;

    @PostMapping
    @ApiMessage("Create Order")
    public ResponseEntity<ResponseId> postMethodName(@RequestBody OrderRequestModel requestModel) {
        ResponseId orderId = orderApplicationService.createOrder(requestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    @PutMapping("p/{id}")
    @ApiMessage("Update Order")
    public ResponseEntity<ResponseId> updateOrder(@PathVariable String orderId,
            @RequestBody OrderRequestModel requestModel) {
        ResponseId id = orderApplicationService.updateOrder(orderId, requestModel);
        return ResponseEntity.status(HttpStatus.OK).body(id);

    }

    @DeleteMapping()
    @ApiMessage("Delete Order")
    public ResponseEntity<ResponseId> deleteOrder(@PathVariable String orderId) {
        orderApplicationService.deleteOrder(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
