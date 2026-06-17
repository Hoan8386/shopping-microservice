package com.shoping.userservice.command.aggergate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.shoping.userservice.command.command.CreatePermissionCommand;
import com.shoping.userservice.command.command.DeletePermissionCommand;
import com.shoping.userservice.command.command.UpdatePermissionCommand;
import com.shoping.userservice.command.event.PermissionCreateEvent;
import com.shoping.userservice.command.event.PermissionDeleteEvent;
import com.shoping.userservice.command.event.PermissionUpdateEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@NoArgsConstructor
@Getter
@Setter
public class PermissionAggregate {
    @AggregateIdentifier

    private String id;

    private String code;

    private String name;

    private String module;

    private String apiPath;

    private String method;

    @CommandHandler
    public PermissionAggregate(CreatePermissionCommand command) {
        PermissionCreateEvent permissionCreateEvent = new PermissionCreateEvent();
        BeanUtils.copyProperties(command, permissionCreateEvent);
        AggregateLifecycle.apply(permissionCreateEvent);
    }

    @CommandHandler
    public PermissionAggregate(UpdatePermissionCommand command) {
        PermissionUpdateEvent permissionUpdateEvent = new PermissionUpdateEvent();
        BeanUtils.copyProperties(command, permissionUpdateEvent);
        AggregateLifecycle.apply(permissionUpdateEvent);
    }

    @CommandHandler
    public PermissionAggregate(DeletePermissionCommand command) {
        PermissionDeleteEvent permissionDeleteEvent = new PermissionDeleteEvent();
        BeanUtils.copyProperties(command, permissionDeleteEvent);
        AggregateLifecycle.apply(permissionDeleteEvent);
    }

    @EventSourcingHandler
    public void on(PermissionCreateEvent permissionCreateEvent) {
        this.id = permissionCreateEvent.getId();
        this.code = permissionCreateEvent.getCode();
        this.name = permissionCreateEvent.getName();
        this.module = permissionCreateEvent.getModule();
        this.apiPath = permissionCreateEvent.getApiPath();
        this.method = permissionCreateEvent.getMethod();
    }

    @EventSourcingHandler
    public void on(PermissionUpdateEvent permissionUpdateEvent) {
        this.id = permissionUpdateEvent.getId();
        this.code = permissionUpdateEvent.getCode();
        this.name = permissionUpdateEvent.getName();
        this.module = permissionUpdateEvent.getModule();
        this.apiPath = permissionUpdateEvent.getApiPath();
        this.method = permissionUpdateEvent.getMethod();
    }

    @EventSourcingHandler
    public void on(PermissionDeleteEvent permissionDeleteEvent) {
        this.id = permissionDeleteEvent.getId();
    }
}
