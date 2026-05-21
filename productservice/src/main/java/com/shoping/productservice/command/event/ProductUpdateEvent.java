package com.shoping.productservice.command.event;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductUpdateEvent {
    private String Id;

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String idCategory;

    private String imageUrl;
}
