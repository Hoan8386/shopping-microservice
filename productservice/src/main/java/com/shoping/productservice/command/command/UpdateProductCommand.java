package com.shoping.productservice.command.command;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UpdateProductCommand {
    @TargetAggregateIdentifier
    private String Id;

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String idCategory;

    private String imageUrl;
}
