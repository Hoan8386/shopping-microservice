package com.shoping.productservice.command.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailCreateEvent {
    private String id;

    private String productId;

    private String sizeId;

    private Integer quantity;

    private Double price;

    private Boolean status;
}
