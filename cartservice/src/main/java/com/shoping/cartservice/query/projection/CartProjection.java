package com.shoping.cartservice.query.projection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.cartservice.command.data.Cart;
import com.shoping.cartservice.command.data.CartRepository;
import com.shoping.cartservice.query.model.CartItemResponseModel;
import com.shoping.cartservice.query.model.CartResponseModel;
import com.shoping.cartservice.query.queries.GetCartByUserIdQuery;

@Component
public class CartProjection {

    @Autowired
    private CartRepository cartRepository;

    @QueryHandler
    public CartResponseModel handle(GetCartByUserIdQuery query) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(query.getUserId());
        if (optionalCart.isEmpty()) {
            return CartResponseModel.builder()
                    .userId(query.getUserId())
                    .items(new ArrayList<>())
                    .totalAmount(0.0)
                    .totalItems(0)
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        Cart cart = optionalCart.get();
        List<CartItemResponseModel> itemModels = new ArrayList<>();
        int totalItems = 0;
        double totalAmount = 0.0;

        if (cart.getListItems() != null) {
            itemModels = cart.getListItems().stream().map(item -> CartItemResponseModel.builder()
                    .id(item.getId())
                    .productDetailId(item.getProductDetailId())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .subtotal(item.getSubtotal())
                    .build()
            ).collect(Collectors.toList());

            totalItems = itemModels.stream().mapToInt(CartItemResponseModel::getQuantity).sum();
            totalAmount = itemModels.stream()
                    .mapToDouble(item -> item.getSubtotal() != null ? item.getSubtotal() : 0.0)
                    .sum();
        }

        return CartResponseModel.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(itemModels)
                .totalAmount(totalAmount)
                .totalItems(totalItems)
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}
