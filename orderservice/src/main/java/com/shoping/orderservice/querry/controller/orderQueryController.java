package com.shoping.orderservice.querry.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.orderservice.command.data.Order;

@RestController
@RequestMapping("/api/v1/order")
public class orderQueryController {
    @GetMapping()
    @ApiMessage("Get All Order")
    public ResponseEntity<List<Order>> getALlOrder() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/{orderId}")
    @ApiMessage("Get Detail Order")
    public ResponseEntity<Order> getOrderDetail(@RequestParam String orderId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
