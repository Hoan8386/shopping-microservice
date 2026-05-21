package com.shoping.productservice.query.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SizeResponseModel {
    private String id;

    private String name;

    private String description;

    private Boolean status;
}
