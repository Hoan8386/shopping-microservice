package com.shoping.cartservice.query.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseModel {

    private String id;

    private String userId;

    private List<CartItemResponseModel> items;

    private Double totalAmount;

    private int totalItems;

    private LocalDateTime updatedAt;
}
