package com.shoping.productservice.command.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailRequestModel {

    @NotBlank(message = "Product ID must not be blank")
    private String productId;

    @NotBlank(message = "Size ID must not be blank")
    private String sizeId;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;

    private Double price;

    private Boolean status;
}