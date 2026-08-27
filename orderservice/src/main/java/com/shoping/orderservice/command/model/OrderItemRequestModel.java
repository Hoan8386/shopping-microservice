package com.shoping.orderservice.command.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderItemRequestModel {
    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}
