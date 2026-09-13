package com.shoping.cartservice.command.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.commonservice.util.anotation.ResponseId;
import com.shoping.cartservice.command.model.CartItemRequestModel;
import com.shoping.cartservice.command.model.UpdateCartItemRequestModel;
import com.shoping.cartservice.command.service.CartApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cart")
public class CartCommandController {

    @Autowired
    private CartApplicationService cartApplicationService;

    @PostMapping("/items")
    @ApiMessage("Add item to cart")
    public ResponseEntity<ResponseId> addItem(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CartItemRequestModel requestModel) {
        ResponseId responseId = cartApplicationService.addItemToCart(userId, requestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseId);
    }

    @PutMapping("/items/{productDetailId}")
    @ApiMessage("Update cart item quantity")
    public ResponseEntity<ResponseId> updateItemQuantity(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String productDetailId,
            @Valid @RequestBody UpdateCartItemRequestModel requestModel) {
        ResponseId responseId = cartApplicationService.updateItemQuantity(userId, productDetailId, requestModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

    @DeleteMapping("/items/{productDetailId}")
    @ApiMessage("Remove item from cart")
    public ResponseEntity<ResponseId> removeItem(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String productDetailId) {
        ResponseId responseId = cartApplicationService.removeItemFromCart(userId, productDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

    @DeleteMapping
    @ApiMessage("Clear cart")
    public ResponseEntity<ResponseId> clearCart(
            @RequestHeader("X-User-Id") String userId) {
        ResponseId responseId = cartApplicationService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }
}
