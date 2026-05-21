package com.shoping.productservice.command.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailRequestModel {
    private String productId;

    private String sizeId;

    private Integer quantity;

    private Double price;

    private Boolean status;
}
