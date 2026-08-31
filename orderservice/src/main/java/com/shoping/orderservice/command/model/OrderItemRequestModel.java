package com.shoping.orderservice.command.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestModel {
    @NotBlank(message = "Product ID cannot be blank")
    private String productDetailId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

}
