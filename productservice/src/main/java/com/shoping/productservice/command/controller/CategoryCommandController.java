package com.shoping.productservice.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.commonservice.util.anotation.ResponseId;
import com.shoping.productservice.command.command.CreateCategoryCommand;
import com.shoping.productservice.command.command.DeleteCategoryCommand;
import com.shoping.productservice.command.command.DeleteProductCommand;
import com.shoping.productservice.command.command.UpdateCategoryCommand;
import com.shoping.productservice.command.model.CategoryRequestModel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ApiMessage("Create category")
    public ResponseEntity<ResponseId> createCategory(@RequestBody CategoryRequestModel categoryRequestModel) {

        CreateCategoryCommand categoryCommand = new CreateCategoryCommand(
                UUID.randomUUID().toString(),
                categoryRequestModel.getName(),
                categoryRequestModel.getDescription(),
                categoryRequestModel.getStatus());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(categoryCommand));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseId);
    }

    @PostMapping("/{categoryId}")
    @ApiMessage("Update category")
    public ResponseEntity<ResponseId> postMethodName(@PathVariable String categoryId,
            @RequestBody CategoryRequestModel categoryRequestModel) {
        UpdateCategoryCommand updateCategoryCommand = new UpdateCategoryCommand(
                categoryId.toString(),
                categoryRequestModel.getName(),
                categoryRequestModel.getDescription(),
                categoryRequestModel.getStatus());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(updateCategoryCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

    @DeleteMapping("/{categoryId}")
    @ApiMessage("Delete category")
    public ResponseEntity<ResponseId> deleteCategory(@PathVariable String categoryId) {
        DeleteCategoryCommand deleteCategoryCommand = new DeleteCategoryCommand(categoryId.toString());
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(deleteCategoryCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

}
