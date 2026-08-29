package com.shoping.orderservice.command.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoping.commonservice.model.response.ProductDetailResponseCommonModel;
import com.shoping.commonservice.queries.GetDetailProductQuery;
import com.shoping.orderservice.command.command.OrderCreateCommand;
import com.shoping.orderservice.command.command.OrderItemCommand;
import com.shoping.orderservice.command.data.OrderStatus;
import com.shoping.orderservice.command.model.OrderRequestModel;

@Service
public class OrderApplicationService {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired 
    private QueryGateway queryGateway;

    public String createOrder (OrderRequestModel requestModel) {
        List<String> productIds = requestModel.getItems().stream().map(item ->item.getProductId()).toList();
        List<ProductDetailResponseCommonModel> listProductDetail = new ArrayList<>() ;
        for (String ItemId : productIds) {
            GetDetailProductQuery query = new GetDetailProductQuery(ItemId);
            ProductDetailResponseCommonModel productDetail = queryGateway.query(query, ResponseTypes.instanceOf(ProductDetailResponseCommonModel.class)).join();
            listProductDetail.add(productDetail);
        }

        List<OrderItemCommand> listOrderItemCommands = new ArrayList<>();

        for (ProductDetailResponseCommonModel responseDetail : listProductDetail) {
            OrderItemCommand orderItemCommand = new OrderItemCommand(responseDetail.getProductId(),responseDetail.getPrice(), responseDetail.getQuantity());
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
        return null;
    }
    
}
