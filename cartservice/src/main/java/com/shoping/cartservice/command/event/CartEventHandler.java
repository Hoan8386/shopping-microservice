package com.shoping.cartservice.command.event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.shoping.cartservice.command.data.Cart;
import com.shoping.cartservice.command.data.CartItem;
import com.shoping.cartservice.command.data.CartItemRepository;
import com.shoping.cartservice.command.data.CartRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CartEventHandler {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @EventHandler
    @Transactional
    public void on(CartCreatedEvent event) {
        log.info("Handling CartCreatedEvent for cartId: {}, userId: {}", event.getId(), event.getUserId());
        Optional<Cart> existing = cartRepository.findById(event.getId());
        if (existing.isEmpty()) {
            Cart cart = Cart.builder()
                    .id(event.getId())
                    .userId(event.getUserId())
                    .totalAmount(0.0)
                    .listItems(new ArrayList<>())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            cartRepository.save(cart);
        }
    }

    @EventHandler
    @Transactional
    public void on(ItemAddedToCartEvent event) {
        log.info("Handling ItemAddedToCartEvent for cartId: {}, productDetailId: {}", event.getId(), event.getProductDetailId());
        Cart cart = cartRepository.findById(event.getId()).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .id(event.getId())
                    .userId(event.getUserId())
                    .totalAmount(0.0)
                    .listItems(new ArrayList<>())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            return cartRepository.save(newCart);
        });

        Optional<CartItem> optionalItem = cartItemRepository.findByCartIdAndProductDetailId(cart.getId(), event.getProductDetailId());
        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            item.setQuantity(item.getQuantity() + event.getQuantity());
            if (event.getUnitPrice() != null) {
                item.setUnitPrice(event.getUnitPrice());
            }
            item.setSubtotal(item.getUnitPrice() * item.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem item = CartItem.builder()
                    .id(UUID.randomUUID().toString())
                    .productDetailId(event.getProductDetailId())
                    .quantity(event.getQuantity())
                    .unitPrice(event.getUnitPrice())
                    .subtotal(event.getUnitPrice() * event.getQuantity())
                    .cart(cart)
                    .build();
            cartItemRepository.save(item);
        }

        recalculateCartTotal(cart.getId());
    }

    @EventHandler
    @Transactional
    public void on(CartItemUpdatedEvent event) {
        log.info("Handling CartItemUpdatedEvent for cartId: {}, productDetailId: {}", event.getId(), event.getProductDetailId());
        Optional<CartItem> optionalItem = cartItemRepository.findByCartIdAndProductDetailId(event.getId(), event.getProductDetailId());
        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            item.setQuantity(event.getQuantity());
            item.setSubtotal(item.getUnitPrice() * event.getQuantity());
            cartItemRepository.save(item);
            recalculateCartTotal(event.getId());
        }
    }

    @EventHandler
    @Transactional
    public void on(ItemRemovedFromCartEvent event) {
        log.info("Handling ItemRemovedFromCartEvent for cartId: {}, productDetailId: {}", event.getId(), event.getProductDetailId());
        Optional<CartItem> optionalItem = cartItemRepository.findByCartIdAndProductDetailId(event.getId(), event.getProductDetailId());
        if (optionalItem.isPresent()) {
            cartItemRepository.delete(optionalItem.get());
            recalculateCartTotal(event.getId());
        }
    }

    @EventHandler
    @Transactional
    public void on(CartClearedEvent event) {
        log.info("Handling CartClearedEvent for cartId: {}", event.getId());
        Optional<Cart> optionalCart = cartRepository.findById(event.getId());
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            if (cart.getListItems() != null) {
                cart.getListItems().clear();
            }
            cart.setTotalAmount(0.0);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }
    }

    private void recalculateCartTotal(String cartId) {
        cartRepository.findById(cartId).ifPresent(cart -> {
            double total = 0.0;
            if (cart.getListItems() != null) {
                for (CartItem item : cart.getListItems()) {
                    if (item.getSubtotal() != null) {
                        total += item.getSubtotal();
                    }
                }
            }
            cart.setTotalAmount(total);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        });
    }
}
