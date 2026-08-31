package com.shoping.orderservice.command.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCommand {
    private String productDetailId;

    private Double unitPrice;

    private int quantity;
    
    private Double subTotal;

}
