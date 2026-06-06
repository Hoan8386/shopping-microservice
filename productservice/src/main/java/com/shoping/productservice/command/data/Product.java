package com.shoping.productservice.command.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    private String Id;

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String idCategory;

    private String imageUrl;
}