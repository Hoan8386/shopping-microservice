package com.shoping.cartservice.query.controller;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.cartservice.query.model.CartResponseModel;
import com.shoping.cartservice.query.queries.GetCartByUserIdQuery;

@RestController
@RequestMapping("/api/v1/cart")
public class CartQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    @ApiMessage("Get Cart for Current User")
    public ResponseEntity<CartResponseModel> getCart(@RequestHeader("X-User-Id") String userId) {
        GetCartByUserIdQuery query = new GetCartByUserIdQuery(userId);
        CartResponseModel result = queryGateway
                .query(query, ResponseTypes.instanceOf(CartResponseModel.class))
                .join();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
