package com.shoping.productservice.query.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseModel {
    private String Id;

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String category;

    private String imageUrl;
}
