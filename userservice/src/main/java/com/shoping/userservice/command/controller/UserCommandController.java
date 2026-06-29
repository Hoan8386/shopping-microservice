package com.shoping.userservice.command.controller;

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
import com.shoping.userservice.command.command.CreateUserCommand;
import com.shoping.userservice.command.command.DeleteUserCommand;
import com.shoping.userservice.command.command.UpdateUserCommand;
import com.shoping.userservice.command.model.UserRequestModel;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ApiMessage("Create user")
    public ResponseEntity<ResponseId> createUser(@Valid @RequestBody UserRequestModel userRequestModel) {
        CreateUserCommand createUserCommand = new CreateUserCommand(
                UUID.randomUUID().toString(),
                userRequestModel.getKeycloakId(),
                userRequestModel.getFullName(),
                userRequestModel.getPhone(),
                userRequestModel.getAddress(),
                userRequestModel.getAvatar(),
                userRequestModel.getGender(),
                userRequestModel.getBirthday(),
                userRequestModel.getStatus());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(createUserCommand));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseId);
    }

    @PutMapping("/{userId}")
    @ApiMessage("Update user")
    public ResponseEntity<ResponseId> updateUser(@PathVariable String userId,
            @RequestBody UserRequestModel userRequestModel) {
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(
                userId,
                userRequestModel.getKeycloakId(),
                userRequestModel.getFullName(),
                userRequestModel.getPhone(),
                userRequestModel.getAddress(),
                userRequestModel.getAvatar(),
                userRequestModel.getGender(),
                userRequestModel.getBirthday(),
                userRequestModel.getStatus());

        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(updateUserCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

    @DeleteMapping("/{userId}")
    @ApiMessage("Delete user")
    public ResponseEntity<ResponseId> deleteUser(@PathVariable String userId) {
        DeleteUserCommand deleteUserCommand = new DeleteUserCommand(userId);
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(deleteUserCommand));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }
}
