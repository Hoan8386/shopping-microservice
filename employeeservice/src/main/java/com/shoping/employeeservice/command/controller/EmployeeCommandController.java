package com.shoping.employeeservice.command.controller;

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
import com.shoping.employeeservice.command.command.CreateEmployeeCommand;
import com.shoping.employeeservice.command.command.DeleteEmployeeCommand;
import com.shoping.employeeservice.command.command.UpdateEmployeeCommand;
import com.shoping.employeeservice.command.model.EmployeeRequestModel;

import jakarta.validation.Valid;

// CQRS Flow:
// 1. Controller creates command and sends via CommandGateway.
// 2. Axon routes command to @CommandHandler in EmployeeAggregate.
// 3. @CommandHandler calls AggregateLifecycle.apply(event).
// 4. Event is written to Event Store and published to Event Bus.
// 5. @EventSourcingHandler in aggregate updates in-memory state.
// 6. @EventHandler in EmployeeEventsHandler persists to the read-side DB.

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ApiMessage("Create employee")
    public ResponseEntity<ResponseId> createEmployee(@Valid @RequestBody EmployeeRequestModel model) {
        CreateEmployeeCommand command = new CreateEmployeeCommand(
                UUID.randomUUID().toString(),
                model.getFirstName(),
                model.getLastName(),
                model.getKin(),
                model.getIsDisciplined(),
                model.getEmail(),
                model.getAddress(),
                model.getAvatar());
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(command));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseId);
    }

    @PutMapping("/{employeeId}")
    @ApiMessage("Update employee")
    public ResponseEntity<ResponseId> updateEmployee(
            @PathVariable String employeeId,
            @Valid @RequestBody EmployeeRequestModel model) {
        UpdateEmployeeCommand command = new UpdateEmployeeCommand(
                employeeId,
                model.getFirstName(),
                model.getLastName(),
                model.getKin(),
                model.getIsDisciplined(),
                model.getEmail(),
                model.getAddress(),
                model.getAvatar());
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(command));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }

    @DeleteMapping("/{employeeId}")
    @ApiMessage("Delete employee")
    public ResponseEntity<ResponseId> deleteEmployee(@PathVariable String employeeId) {
        DeleteEmployeeCommand command = new DeleteEmployeeCommand(employeeId);
        ResponseId responseId = new ResponseId(commandGateway.sendAndWait(command));
        return ResponseEntity.status(HttpStatus.OK).body(responseId);
    }
}
