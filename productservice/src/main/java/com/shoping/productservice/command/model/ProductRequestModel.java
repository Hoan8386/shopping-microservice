package com.shoping.productservice.command.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestModel {
    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String idCategory;

    private String imageUrl;
}
