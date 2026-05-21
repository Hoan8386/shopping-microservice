package com.shoping.productservice.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.commonservice.util.anotation.ResponseId;
import com.shoping.productservice.command.command.CreateProductDetailCommand;
import com.shoping.productservice.command.command.DeleteProductDetailCommand;
import com.shoping.productservice.command.command.UpdateProductDetailCommand;
import com.shoping.productservice.command.model.ProductDetailRequestModel;

@RestController
@RequestMapping("/api/v1/product-details")
public class ProductDetailCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ApiMessage("Create product detail")
    public ResponseEntity<ResponseId> createProductDetail(@RequestBody ProductDetailRequestModel requestModel) {
        CreateProductDetailCommand command = new CreateProductDetailCommand(
                UUID.randomUUID().toString(),
                requestModel.getProductId(),
                requestModel.getSizeId(),
                requestModel.getQuantity(),
                requestModel.getPrice(),
                requestModel.getStatus());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(command));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseId);
    }

    @PutMapping("/{productDetailId}")
    @ApiMessage("Update product detail")
    public ResponseEntity<ResponseId> updateProductDetail(@RequestBody ProductDetailRequestModel requestModel,
            @PathVariable String productDetailId) {
        UpdateProductDetailCommand command = new UpdateProductDetailCommand(
                productDetailId,
                requestModel.getProductId(),
                requestModel.getSizeId(),
                requestModel.getQuantity(),
                requestModel.getPrice(),
                requestModel.getStatus());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(command));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

    @DeleteMapping("/{productDetailId}")
    @ApiMessage("Delete product detail")
    public ResponseEntity<ResponseId> deleteProductDetail(@PathVariable String productDetailId) {
        DeleteProductDetailCommand command = new DeleteProductDetailCommand(productDetailId);
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(command));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }
}
