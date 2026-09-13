package com.shoping.cartservice.command.service;

import java.util.Optional;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoping.commonservice.exception.InsufficientStockException;
import com.shoping.commonservice.model.response.ProductDetailResponseCommonModel;
import com.shoping.commonservice.queries.GetDetailProductQuery;
import com.shoping.commonservice.util.anotation.ResponseId;
import com.shoping.cartservice.command.command.AddItemToCartCommand;
import com.shoping.cartservice.command.command.ClearCartCommand;
import com.shoping.cartservice.command.command.CreateCartCommand;
import com.shoping.cartservice.command.command.RemoveItemFromCartCommand;
import com.shoping.cartservice.command.command.UpdateCartItemCommand;
import com.shoping.cartservice.command.data.Cart;
import com.shoping.cartservice.command.data.CartItem;
import com.shoping.cartservice.command.data.CartItemRepository;
import com.shoping.cartservice.command.data.CartRepository;
import com.shoping.cartservice.command.model.CartItemRequestModel;
import com.shoping.cartservice.command.model.UpdateCartItemRequestModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartApplicationService {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private QueryGateway queryGateway;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public String getOrCreateCartId(String userId) {
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        if (existingCart.isPresent()) {
            return existingCart.get().getId();
        }
        String cartId = UUID.randomUUID().toString();
        log.info("Creating new cart for userId: {}, generated cartId: {}", userId, cartId);
        commandGateway.sendAndWait(new CreateCartCommand(cartId, userId));
        return cartId;
    }

    public ResponseId addItemToCart(String userId, CartItemRequestModel requestModel) {
        GetDetailProductQuery query = new GetDetailProductQuery(requestModel.getProductDetailId());
        ProductDetailResponseCommonModel productDetail = queryGateway
                .query(query, ResponseTypes.instanceOf(ProductDetailResponseCommonModel.class)).join();

        if (productDetail == null) {
            throw new RuntimeException("Sản phẩm không tồn tại: " + requestModel.getProductDetailId());
        }

        String cartId = getOrCreateCartId(userId);

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductDetailId(cartId, requestModel.getProductDetailId());
        int totalRequestedQuantity = requestModel.getQuantity() + (existingItem.map(CartItem::getQuantity).orElse(0));
        if (productDetail.getQuantity() < totalRequestedQuantity) {
            throw new InsufficientStockException("Số lượng sản phẩm trong kho không đủ");
        }

        AddItemToCartCommand command = new AddItemToCartCommand(
                cartId,
                userId,
                requestModel.getProductDetailId(),
                requestModel.getQuantity(),
                productDetail.getPrice()
        );

        commandGateway.sendAndWait(command);
        return new ResponseId(cartId);
    }

    public ResponseId updateItemQuantity(String userId, String productDetailId, UpdateCartItemRequestModel requestModel) {
        GetDetailProductQuery query = new GetDetailProductQuery(productDetailId);
        ProductDetailResponseCommonModel productDetail = queryGateway
                .query(query, ResponseTypes.instanceOf(ProductDetailResponseCommonModel.class)).join();

        if (productDetail == null) {
            throw new RuntimeException("Sản phẩm không tồn tại: " + productDetailId);
        }

        if (productDetail.getQuantity() < requestModel.getQuantity()) {
            throw new InsufficientStockException("Số lượng sản phẩm trong kho không đủ");
        }

        String cartId = getOrCreateCartId(userId);

        UpdateCartItemCommand command = new UpdateCartItemCommand(
                cartId,
                productDetailId,
                requestModel.getQuantity()
        );

        commandGateway.sendAndWait(command);
        return new ResponseId(cartId);
    }

    public ResponseId removeItemFromCart(String userId, String productDetailId) {
        Optional<Cart> optional = cartRepository.findByUserId(userId);
        if (optional.isPresent()) {
            String cartId = optional.get().getId();
            RemoveItemFromCartCommand command = new RemoveItemFromCartCommand(cartId, productDetailId);
            commandGateway.sendAndWait(command);
            return new ResponseId(cartId);
        }
        return null;
    }

    public ResponseId clearCart(String userId) {
        Optional<Cart> optional = cartRepository.findByUserId(userId);
        if (optional.isPresent()) {
            String cartId = optional.get().getId();
            ClearCartCommand command = new ClearCartCommand(cartId);
            commandGateway.sendAndWait(command);
            return new ResponseId(cartId);
        }
        return null;
    }
}
