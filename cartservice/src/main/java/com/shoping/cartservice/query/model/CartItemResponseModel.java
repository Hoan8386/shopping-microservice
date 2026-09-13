package com.shoping.cartservice.query.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseModel {

    private String id;

    private String productDetailId;

    private int quantity;

    private Double unitPrice;

    private Double subtotal;
}
