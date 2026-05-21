package com.shoping.productservice.query.specification;

import org.springframework.data.jpa.domain.Specification;

import com.shoping.productservice.command.data.Product;

public class ProductSpecification {

    public static Specification<Product> filterProducts(
            String name,
            String category,
            Double minPrice,
            Double maxPrice) {

        return (root, query, criteriaBuilder) -> {

            var predicate = criteriaBuilder.conjunction();

            // filter name
            if (name != null && !name.isEmpty()) {

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"));
            }

            // filter category
            if (category != null && !category.isEmpty()) {

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("category"),
                                category));
            }

            // filter min price
            if (minPrice != null) {

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("price"),
                                minPrice));
            }

            // filter max price
            if (maxPrice != null) {

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("price"),
                                maxPrice));
            }

            return predicate;
        };
    }
}