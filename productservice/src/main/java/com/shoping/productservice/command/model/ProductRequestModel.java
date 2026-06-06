package com.shoping.productservice.command.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestModel {

    @NotBlank(message = "Product name must not be blank")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;

    private String idCategory;

    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;
}