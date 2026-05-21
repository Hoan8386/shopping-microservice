package com.shoping.productservice.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.commonservice.util.anotation.ResponseId;
import com.shoping.productservice.command.command.CreateProductCommand;
import com.shoping.productservice.command.command.DeleteProductCommand;
import com.shoping.productservice.command.command.UpdateProductCommand;
import com.shoping.productservice.command.model.ProductRequestModel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

// flow 
// 1 Controller tạo CreateProductCommand và gửi qua CommandGateway.
// 2 Axon route command đến @CommandHandler trong ProductAggregate (trường hợp create thì thường là constructor handler).
// 3 Trong @CommandHandler, gọi AggregateLifecycle.apply(event).
// 4 Khi apply:
//  Event được ghi vào Event Store.
//  Event được publish lên Event Bus/Event Processor.
// 5 @EventSourcingHandler trong aggregate được gọi để cập nhật state in-memory của aggregate từ event.
// 6 @EventHandler (projection/handler bên ngoài aggregate) nhận event và lưu xuống database read model (ở bạn là bảng products).

@RestController
@RequestMapping("/api/v1/products")
public class ProductCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ApiMessage("Create product")
    public ResponseEntity<ResponseId> createProduct(@RequestBody ProductRequestModel productRequestModel) {
        CreateProductCommand command = new CreateProductCommand(
                UUID.randomUUID().toString(),
                productRequestModel.getName(),
                productRequestModel.getDescription(),
                productRequestModel.getPrice(),
                productRequestModel.getQuantity(),
                productRequestModel.getIdCategory(),
                productRequestModel.getImageUrl());
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(command));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseId);
    }

    @PutMapping("/{productId}")
    @ApiMessage("Update product")
    public ResponseEntity<ResponseId> updateProduct(@RequestBody ProductRequestModel productRequestModel,
            PathVariable productId) {
        // TODO: process PUT request
        UpdateProductCommand updateProductCommand = new UpdateProductCommand(productId.toString(),
                productRequestModel.getName(),
                productRequestModel.getDescription(),
                productRequestModel.getPrice(),
                productRequestModel.getQuantity(),
                productRequestModel.getIdCategory(),
                productRequestModel.getImageUrl());
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(updateProductCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

    @DeleteMapping("/{productId}")
    @ApiMessage("Delete product")
    public ResponseEntity<ResponseId> deleteProduct(PathVariable productId) {
        // TODO: process PUT request
        DeleteProductCommand deleteProductCommand = new DeleteProductCommand(productId.toString());
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(deleteProductCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

}
