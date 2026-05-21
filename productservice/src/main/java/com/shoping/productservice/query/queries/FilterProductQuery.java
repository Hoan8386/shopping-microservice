package com.shoping.productservice.query.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterProductQuery {
    private String name;
    private String category;
    private Double minPrice;
    private Double maxPrice;
}