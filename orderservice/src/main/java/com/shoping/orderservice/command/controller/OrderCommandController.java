package com.shoping.orderservice.command.controller;

import java.util.List;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.commonservice.util.anotation.ResponseId;
import com.shoping.orderservice.command.data.Order;
import com.shoping.orderservice.command.model.OrderRequestModel;

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

    @PostMapping()
    @ApiMessage("Create Order")
    public ResponseEntity<ResponseId> postMethodName(@RequestBody  OrderRequestModel requestModel) {
        // TODO: process POST request

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("p/{id}")
    @ApiMessage("Update Order")
    public ResponseEntity<ResponseId> updateOrder(@PathVariable Long orderId,
            @RequestBody OrderRequestModel requestModel) {
        // TODO: process PUT request

        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    @DeleteMapping()
    @ApiMessage("Delete Order")
    public ResponseEntity<ResponseId> updateOrder(@PathVariable Long orderId) {
        // TODO: process PUT request

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
