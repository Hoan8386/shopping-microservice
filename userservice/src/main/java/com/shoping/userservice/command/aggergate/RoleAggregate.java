package com.shoping.userservice.command.aggergate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.shoping.userservice.command.command.CreateRoleCommand;
import com.shoping.userservice.command.command.DeleteRoleCommand;
import com.shoping.userservice.command.command.UpdateRoleCommand;
import com.shoping.userservice.command.event.RoleCreateEvent;
import com.shoping.userservice.command.event.RoleDeleteEvent;
import com.shoping.userservice.command.event.RoleUpdateEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@NoArgsConstructor
@Getter
@Setter
public class RoleAggregate {
    @AggregateIdentifier

    private String id;

    private String name;

    private String description;

    private boolean active;

    @CommandHandler
    public RoleAggregate(CreateRoleCommand command) {
        RoleCreateEvent roleCreateEvent = new RoleCreateEvent();
        BeanUtils.copyProperties(command, roleCreateEvent);
        AggregateLifecycle.apply(roleCreateEvent);
    }

    @CommandHandler
    public RoleAggregate(UpdateRoleCommand command) {
        RoleUpdateEvent roleUpdateEvent = new RoleUpdateEvent();
        BeanUtils.copyProperties(command, roleUpdateEvent);
        AggregateLifecycle.apply(roleUpdateEvent);
    }

    @CommandHandler
    public RoleAggregate(DeleteRoleCommand command) {
        RoleDeleteEvent roleDeleteEvent = new RoleDeleteEvent();
        BeanUtils.copyProperties(command, roleDeleteEvent);
        AggregateLifecycle.apply(roleDeleteEvent);
    }

    @EventSourcingHandler
    public void on(RoleCreateEvent roleCreateEvent) {
        this.id = roleCreateEvent.getId();
        this.name = roleCreateEvent.getName();
        this.description = roleCreateEvent.getDescription();
        this.active = roleCreateEvent.isActive();
    }

    @EventSourcingHandler
    public void on(RoleUpdateEvent roleUpdateEvent) {
        this.id = roleUpdateEvent.getId();
        this.name = roleUpdateEvent.getName();
        this.description = roleUpdateEvent.getDescription();
        this.active = roleUpdateEvent.isActive();
    }

    @EventSourcingHandler
    public void on(RoleDeleteEvent roleDeleteEvent) {
        this.id = roleDeleteEvent.getId();
    }
}
