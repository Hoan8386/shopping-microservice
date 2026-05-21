package com.shoping.productservice.query.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponseModel {
    private String id;

    private String productId;

    private String sizeId;

    private Integer quantity;

    private Double price;

    private Boolean status;
}
