package com.shoping.orderservice.command.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.commonservice.util.anotation.ResponseId;

import com.shoping.orderservice.command.model.OrderRequestModel;
import com.shoping.orderservice.command.service.OrderApplicationService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/order")
public class OrderCommandController {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @PostMapping
    @ApiMessage("Create Order")
    public ResponseEntity<ResponseId> postMethodName(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-First-Name") String firstName,
            @RequestHeader("X-Last-Name") String lastName,
            @RequestBody OrderRequestModel orderRequestModel) {
        ResponseId orderId = orderApplicationService.createOrder(userId, email , firstName , lastName , orderRequestModel);
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
