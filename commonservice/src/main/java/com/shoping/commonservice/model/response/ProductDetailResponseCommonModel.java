package com.shoping.commonservice.model.response;

import org.axonframework.modelling.command.AggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponseCommonModel {
    private String id;

    private String productId;

    private String sizeId;

    private int quantity;

    private Double price;

    private Boolean status;
}
