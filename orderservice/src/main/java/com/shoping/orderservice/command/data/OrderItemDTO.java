package com.shoping.orderservice.command.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {


    private String productId;

    private int quantity;

    private Double unitPrice;

    private Double subtotal;
}