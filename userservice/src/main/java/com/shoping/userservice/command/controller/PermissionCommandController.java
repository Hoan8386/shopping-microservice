package com.shoping.userservice.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.commonservice.util.anotation.ResponseId;
import com.shoping.userservice.command.command.CreatePermissionCommand;
import com.shoping.userservice.command.command.DeletePermissionCommand;
import com.shoping.userservice.command.command.UpdatePermissionCommand;
import com.shoping.userservice.command.model.PermissionRequestModel;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/permission")
public class PermissionCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ApiMessage("Create permission")
    public ResponseEntity<ResponseId> createPermission(
            @Valid @RequestBody PermissionRequestModel permissionRequestModel) {

        CreatePermissionCommand permissionCommand = new CreatePermissionCommand(
                UUID.randomUUID().toString(),
                permissionRequestModel.getCode(),
                permissionRequestModel.getName(),
                permissionRequestModel.getModule(),
                permissionRequestModel.getApiPath(),
                permissionRequestModel.getMethod());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(permissionCommand));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseId);
    }

    @PostMapping("/{permissionId}")
    @ApiMessage("Update permission")
    public ResponseEntity<ResponseId> updatePermission(@PathVariable String permissionId,
            @RequestBody PermissionRequestModel permissionRequestModel) {
        UpdatePermissionCommand updatePermissionCommand = new UpdatePermissionCommand(
                permissionId,
                permissionRequestModel.getCode(),
                permissionRequestModel.getName(),
                permissionRequestModel.getModule(),
                permissionRequestModel.getApiPath(),
                permissionRequestModel.getMethod());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(updatePermissionCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

    @DeleteMapping("/{permissionId}")
    @ApiMessage("Delete permission")
    public ResponseEntity<ResponseId> deletePermission(@PathVariable String permissionId) {
        DeletePermissionCommand deletePermissionCommand = new DeletePermissionCommand(permissionId);
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(deletePermissionCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

}
