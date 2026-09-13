package com.shoping.cartservice.command.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemAddedToCartEvent {
    private String id;
    private String userId;
    private String productDetailId;
    private int quantity;
    private Double unitPrice;
}
