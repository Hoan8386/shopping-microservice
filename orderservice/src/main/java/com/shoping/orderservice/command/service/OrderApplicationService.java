package com.shoping.orderservice.command.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shoping.commonservice.model.response.ProductDetailResponseCommonModel;
import com.shoping.commonservice.queries.GetDetailProductQuery;
import com.shoping.commonservice.util.anotation.ResponseId;
import com.shoping.orderservice.command.command.OrderCreateCommand;
import com.shoping.orderservice.command.command.OrderDeleteCommand;
import com.shoping.orderservice.command.command.OrderItemCommand;
import com.shoping.orderservice.command.command.OrderUpdateCommand;
import com.shoping.orderservice.command.data.Order;
import com.shoping.orderservice.command.data.OrderRepository;
import com.shoping.orderservice.command.data.OrderStatus;
import com.shoping.orderservice.command.model.OrderRequestModel;

@Service
public class OrderApplicationService {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private QueryGateway queryGateway;

    @Autowired
    private OrderRepository orderRepository;

    public ResponseId createOrder(OrderRequestModel requestModel) {
        List<String> productIds = requestModel.getItems().stream().map(item -> item.getProductId()).toList();
        List<ProductDetailResponseCommonModel> listProductDetail = new ArrayList<>();
        for (String ItemId : productIds) {
            GetDetailProductQuery query = new GetDetailProductQuery(ItemId);
            ProductDetailResponseCommonModel productDetail = queryGateway
                    .query(query, ResponseTypes.instanceOf(ProductDetailResponseCommonModel.class)).join();
            listProductDetail.add(productDetail);
        }

        List<OrderItemCommand> listOrderItemCommands = new ArrayList<>();
        
        for (ProductDetailResponseCommonModel responseDetail : listProductDetail) {
            OrderItemCommand orderItemCommand = new OrderItemCommand(responseDetail.getProductId(),
                    responseDetail.getPrice(), responseDetail.getQuantity());
            listOrderItemCommands.add(orderItemCommand);
        }

        String id = UUID.randomUUID().toString();
        OrderCreateCommand orderCreateCommand = new OrderCreateCommand(
                id,
                "Order_" + id,
                requestModel.getUserId(),
                OrderStatus.PENDING,
                0F,
                listOrderItemCommands,
                requestModel.getShipAddress(),
                requestModel.getShipPhone(),
                LocalDateTime.now());
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(orderCreateCommand));
        return responseId;
    }

    public ResponseId updateOrder(String orderId, OrderRequestModel requestModel) {
        ResponseId responseId = null;
        Optional<Order> optional = orderRepository.findById(orderId);
        if (optional.isPresent()) {
            Order curOrder = optional.get();
            OrderUpdateCommand orderUpdateCommand = new OrderUpdateCommand();
            orderUpdateCommand.setId(orderId);
            orderUpdateCommand.setStatus(curOrder.getStatus());
            orderUpdateCommand.setShipPhone(requestModel.getShipPhone());
            orderUpdateCommand.setShipAddress(requestModel.getShipAddress());

            responseId = new ResponseId(commandGateway.sendAndWait(orderUpdateCommand));
        }
        return responseId;
    }

    public ResponseId deleteOrder(String orderId) {
        ResponseId responseId = null;
        Optional<Order> optional = orderRepository.findById(orderId);
        if (optional.isPresent()) {
            Order curOrder = optional.get();
            OrderDeleteCommand orderDeleteCommand = new OrderDeleteCommand();
            orderDeleteCommand.setId(orderId);

            responseId = new ResponseId(commandGateway.sendAndWait(orderDeleteCommand));
        }
        return responseId;
    }

}
