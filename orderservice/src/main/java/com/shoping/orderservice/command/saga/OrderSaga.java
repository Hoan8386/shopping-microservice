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
                GetDetailProductQuery getDetailProductQuery = new GetDetailProductQuery(item.getProductId());
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

                } else {
                    throw new InsufficientStockException(
                            "Sản phẩm " + bookResponseCommonModel.getId() + "không còn đủ số lượng");
                }
            }

            SagaLifecycle.end();
            OrderNotification orderNotification = new OrderNotification();
            OrderNotification notification = new OrderNotification();

            notification.setOrderId(event.getId());
            notification.setEmail(event.getEmail());
            notification.setFistName(event.getFirstName());
            notification.setLastName(event.getLastName());
            notification.setItems(event.getListItems());
            notification.setTotalPrice(totalPrice);
            kafkaService.sendMessage("confirmOrder", orderNotification);
        } catch (Exception e) {
            rollbackProducts(processedItems);
            rollBackOrderRecord(event.getId());
        }
    }

    private void rollbackProducts(List<OrderItemDTO> processedItems) {
        for (OrderItemDTO item : processedItems) {
            try {
                GetDetailProductQuery query = new GetDetailProductQuery(item.getProductId());
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
                        item.getProductId(),
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
}
