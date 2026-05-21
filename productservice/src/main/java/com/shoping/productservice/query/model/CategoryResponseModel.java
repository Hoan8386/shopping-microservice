package com.shoping.productservice.query.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseModel {
    private String id;

    private String name;

    private String description;

    private Boolean status;
}
