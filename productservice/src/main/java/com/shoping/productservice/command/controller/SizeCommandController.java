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
import com.shoping.productservice.command.command.SizeCreateCommand;
import com.shoping.productservice.command.command.SizeDeleteCommand;
import com.shoping.productservice.command.command.SizeUpdateCommand;
import com.shoping.productservice.command.model.SizeRequestModel;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/size")
public class SizeCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping()
    @ApiMessage("Create size")
    public ResponseEntity<ResponseId> createSize(@Valid @RequestBody SizeRequestModel sizeRequestModel) {

        SizeCreateCommand sizeCreateCommand = new SizeCreateCommand(
                UUID.randomUUID().toString(),
                sizeRequestModel.getName(),
                sizeRequestModel.getDescription(),
                sizeRequestModel.getStatus());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(sizeCreateCommand));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseId);
    }

    @PutMapping("/{sizeId}")
    @ApiMessage("Update size")
    public ResponseEntity<ResponseId> updateSize(@RequestBody SizeRequestModel sizeRequestModel,
            @PathVariable String sizeId) {
        SizeUpdateCommand sizeUpdateCommand = new SizeUpdateCommand(
                sizeId,
                sizeRequestModel.getName(),
                sizeRequestModel.getDescription(),
                sizeRequestModel.getStatus());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(sizeUpdateCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

    @DeleteMapping("/{sizeId}")
    @ApiMessage("Delete size")
    public ResponseEntity<ResponseId> deleteSize(@PathVariable String sizeId) {
        SizeDeleteCommand sizeDeleteCommand = new SizeDeleteCommand(sizeId);
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(sizeDeleteCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

}
