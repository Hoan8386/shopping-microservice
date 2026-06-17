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
import com.shoping.userservice.command.command.CreateRoleCommand;
import com.shoping.userservice.command.command.DeleteRoleCommand;
import com.shoping.userservice.command.command.UpdateRoleCommand;
import com.shoping.userservice.command.model.RoleRequestModel;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/role")
public class RoleCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ApiMessage("Create role")
    public ResponseEntity<ResponseId> createRole(@Valid @RequestBody RoleRequestModel roleRequestModel) {

        CreateRoleCommand roleCommand = new CreateRoleCommand(
                UUID.randomUUID().toString(),
                roleRequestModel.getName(),
                roleRequestModel.getDescription(),
                roleRequestModel.isActive());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(roleCommand));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseId);
    }

    @PostMapping("/{roleId}")
    @ApiMessage("Update role")
    public ResponseEntity<ResponseId> updateRole(@PathVariable String roleId,
            @RequestBody RoleRequestModel roleRequestModel) {
        UpdateRoleCommand updateRoleCommand = new UpdateRoleCommand(
                roleId,
                roleRequestModel.getName(),
                roleRequestModel.getDescription(),
                roleRequestModel.isActive());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(updateRoleCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

    @DeleteMapping("/{roleId}")
    @ApiMessage("Delete role")
    public ResponseEntity<ResponseId> deleteRole(@PathVariable String roleId) {
        DeleteRoleCommand deleteRoleCommand = new DeleteRoleCommand(roleId);
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(deleteRoleCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

}
