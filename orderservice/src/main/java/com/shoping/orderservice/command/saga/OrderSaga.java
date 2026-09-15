package com.shoping.orderservice.command.saga;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.shoping.commonservice.command.ClearCartCommand;
import com.shoping.commonservice.command.RollBackCartCommand;
import com.shoping.commonservice.command.RollbackProductDetailCommand;
import com.shoping.commonservice.command.UpdateProductDetailCommand;
import com.shoping.commonservice.exception.InsufficientStockException;
import com.shoping.commonservice.model.response.ProductDetailResponseCommonModel;
import com.shoping.commonservice.model.response.DTO.OrderItemDTO;
import com.shoping.commonservice.model.response.DTO.OrderNotification;
import com.shoping.commonservice.queries.GetDetailProductQuery;
import com.shoping.commonservice.service.EmailService;
import com.shoping.commonservice.service.KafkaService;
import com.shoping.orderservice.command.command.OrderDeleteCommand;
import com.shoping.orderservice.command.event.CreateOrderEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Saga
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @Autowired
    private KafkaService kafkaService;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    private void handle(CreateOrderEvent event) {
        List<OrderItemDTO> processedItems = new ArrayList<>();
        Double totalPrice = 0D;
        try {
            List<OrderItemDTO> listOrderItems = event.getListItems();
            for (OrderItemDTO item : listOrderItems) {
                GetDetailProductQuery getDetailProductQuery = new GetDetailProductQuery(item.getProductDetailId());
                ProductDetailResponseCommonModel bookResponseCommonModel = queryGateway.query(getDetailProductQuery,
                        ResponseTypes.instanceOf(ProductDetailResponseCommonModel.class)).join();
                if (bookResponseCommonModel.getQuantity() >= item.getQuantity()) {

                    int quantity = bookResponseCommonModel.getQuantity() - item.getQuantity();
                    boolean status = true;
                    if (quantity == 0) {
                        status = false;
                    }
                    UpdateProductDetailCommand command = new UpdateProductDetailCommand();
                    command.setId(bookResponseCommonModel.getId());
                    command.setProductId(bookResponseCommonModel.getProductId());
                    command.setSizeId(bookResponseCommonModel.getSizeId());
                    command.setPrice(bookResponseCommonModel.getPrice());
                    command.setQuantity(quantity);
                    command.setStatus(status);

                    commandGateway.sendAndWait(command);

                    processedItems.add(item);

                     totalPrice += item.getUnitPrice() * item.getQuantity();

                    System.out.println("check command" + command);

                } else {
                    throw new InsufficientStockException(
                            "Sản phẩm " + bookResponseCommonModel.getId() + "không còn đủ số lượng");
                }
            }
            
            ClearCartCommand clearCartCommand = new ClearCartCommand(event.getUserId());
            commandGateway.send(clearCartCommand);

            OrderNotification orderNotification = new OrderNotification();
            orderNotification.setOrderId(event.getId());
            orderNotification.setEmail(event.getEmail());
            orderNotification.setFistName(event.getFirstName());
            orderNotification.setLastName(event.getLastName());
            orderNotification.setItems(event.getListItems());
            orderNotification.setTotalPrice(totalPrice);
            kafkaService.sendMessage("confirmOrder", orderNotification);
            SagaLifecycle.end();
        } catch (Exception e) {
            rollbackProducts(processedItems);
            rollBackOrderRecord(event.getId());
            rollBackCart(event.getId(),event.getUserId(), processedItems);
        }
    }

    private void rollbackProducts(List<OrderItemDTO> processedItems) {
        for (OrderItemDTO item : processedItems) {
            try {
                GetDetailProductQuery query = new GetDetailProductQuery(item.getProductDetailId());
                ProductDetailResponseCommonModel product = queryGateway.query(
                        query,
                        ResponseTypes.instanceOf(
                                ProductDetailResponseCommonModel.class))
                        .join();

                RollbackProductDetailCommand command = new RollbackProductDetailCommand(
                        product.getId(),
                        item.getQuantity());

                commandGateway.sendAndWait(command);

                log.info(
                        "Rollback ProductDetail {} +{}",
                        product.getId(),
                        item.getQuantity());

            } catch (Exception e) {
                log.error(
                        "Rollback ProductDetail {} thất bại: {}",
                        item.getProductDetailId(),
                        e.getMessage());
            }
        }
    }

    private void rollBackOrderRecord(String id) {

        OrderDeleteCommand command = new OrderDeleteCommand(id);

        commandGateway.sendAndWait(command);

        log.info(
                "Đã rollback Order: {}",
                id);
    }

    private  void rollBackCart( String id, String userId ,List<OrderItemDTO> processedItems ) {
        RollBackCartCommand rollBackCartCommand = new RollBackCartCommand(id,userId,processedItems); 
        commandGateway.sendAndWait(rollBackCartCommand);
    }
}
