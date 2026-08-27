package com.shoping.orderservice.command.model;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class OrderRequestModel {
    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequestModel> items;
}
